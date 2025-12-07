package com.tnt.project.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;


@RestController
@RequestMapping("/bodyAnalyze")
public class BodyAnalyzerController {

	@Value("${google.api-key}")
	private String apiKey;

	@Value("${google.project-id}")
	private String projectId;

	@Value("${google.location}")
	private String location;

	@Value("${google.model}")
	private String model;
	
	 @Value("${spring.cloud.gcp.credentials.location}")
	    private Resource gcpResource;
	
	 @Value("${spring.cloud.gcp.bucket}")
	    private String bucketName;

	private final ObjectMapper objectMapper = new ObjectMapper();

	 @PostMapping
	    public Map<String, Object> analyzeBody(@RequestParam("image") MultipartFile file) throws Exception {

	        // -----------------------------
	        // 1) GCS에 업로드
	        // -----------------------------
		 Storage storage = StorageOptions.newBuilder()
			        .setProjectId(projectId)
			        .setCredentials(ServiceAccountCredentials.fromStream(
			        		gcpResource.getInputStream())) // resources 루트 기준
			        .build()
			        .getService();
		 

	        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

	        BlobId blobId = BlobId.of(bucketName, fileName);
	        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
	                .setContentType(file.getContentType())
	                .build();

	        storage.create(blobInfo, file.getBytes());

	        String gcsUri = "gs://" + bucketName + "/" + fileName;
	        System.out.println("GCS URI: " + gcsUri);

	        // -----------------------------
	        // 2) Vertex AI 호출
	        // -----------------------------
	        String url =
	        	    "https://" + location + "-aiplatform.googleapis.com/v1/projects/" + projectId +
	        	    "/locations/" + location +
	        	    "/publishers/google/models/" + model +
	        	    ":generateContent?key=" + apiKey;
	        
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        // Vertex AI 멀티모달 요청 Body
	        Map<String, Object> part1 = Map.of(
	                "fileData", Map.of(
	                        "fileUri", gcsUri,
	                        "mimeType", file.getContentType()
	                )
	        );

	        Map<String, Object> part2 = Map.of(
	                "text", "너는 체형 분석과 스타일링 전문가야. 사진을 보고 결과를 아래 형식으로 작성해줘:\n" +
	                        "-- 체형 분석 내용\n" +
	                        "여기에 분석 내용을 작성\n" +
	                        "-- 상의 추천\n" +
	                        "여기에 상의 추천 내용을 작성\n" +
	                        "-- 하의 추천\n" +
	                        "여기에 하의 추천 내용을 작성"
	        );

	        Map<String, Object> content = Map.of(
	                "role", "user",
	                "parts", List.of(part1, part2)
	        );

	        Map<String, Object> body = Map.of(
	                "contents", List.of(content)
	        );

	        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

	        RestTemplate rest = new RestTemplate();
	        ResponseEntity<String> response = rest.exchange(url, HttpMethod.POST, entity, String.class);

	        // -----------------------------
	        // 3) 응답 파싱
	        // -----------------------------
	        JsonNode root = objectMapper.readTree(response.getBody());
	        String answer = root
	                .path("candidates")
	                .get(0)
	                .path("content")
	                .path("parts")
	                .get(0)
	                .path("text")
	                .asText();

	        System.out.println("답변: " + answer);
	        boolean deleted = storage.delete(blobId);
	        
	     // "--" 기준으로 분리
	        String[] sections = answer.split("--");


	        Map<String, String> resultMap = new HashMap<>();
	        resultMap.put("bodyAnalysis", sections.length > 1 ? sections[1].trim() : "");
	        resultMap.put("topRecommendation", sections.length > 2 ? sections[2].trim() : "");
	        resultMap.put("bottomRecommendation", sections.length > 3 ? sections[3].trim() : "");
	        
	        System.out.println("체형 분석: " + resultMap.get("bodyAnalysis"));
	        System.out.println("상의 추천: " + resultMap.get("topRecommendation"));
	        System.out.println("하의 추천: " + resultMap.get("bottomRecommendation"));
	        
	        
	        return Map.of(
	        		"gcsUri", gcsUri,
	                "answer", resultMap,
	                "deleted", deleted
	        );
	    }
	}