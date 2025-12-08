package com.tnt.project.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
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

@RestController
@RequestMapping("/bodyAnalyze")
public class BodyAnalyzerController {

	@Value("${google.api-key}")
	private String apiKey;

	@Value("${google.model}")
	private String model;

	private final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 이미지와 텍스트를 보내 Gemini 모델에서 분석 결과를 받는 엔드포인트
	 */
	@PostMapping
	public Map<String, Object> callGemini(@RequestParam("image") MultipartFile file) throws Exception {

		// Gemini 모델 호출 URL
		String url = String.format(
				"https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent",
				model
				);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("x-goog-api-key", apiKey);

		// 이미지 Base64 인코딩 (inline_data 구조)
		String base64Image = java.util.Base64.getEncoder().encodeToString(file.getBytes());
		Map<String, Object> imagePart = Map.of(
				"inline_data", Map.of(
						"mime_type", file.getContentType(),
						"data", base64Image
						)
				);

		// 텍스트 파트
		Map<String, Object> textPart = Map.of(
				"text", "너는 체형 분석과 스타일링 전문가야. 사진을 보고 결과를 아래 형식으로 작성해주는데 특수문자는 사용하지 말 것:\n" +
						"-- 체형 분석 내용\n" +
						"여기에 체형 분석 내용을 작성 체형 유형( 삼각형, 역삼각형, 원형, 모래시계형, 직사각형 ) 을 무조건 포함하고 체형 이야기만 넣을 것\n" +
						"-- 상의 추천\n" +
						"여기에 추천 내용을 작성\n" +
						"-- 하의 추천\n" +
						"여기에 추천 내용을 작성"
				);

		// contents 구성
		Map<String, Object> content = Map.of(
				"parts", List.of(imagePart, textPart)
				);

		Map<String, Object> body = Map.of("contents", List.of(content));

		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
		RestTemplate rest = new RestTemplate();

		// REST 호출
		ResponseEntity<String> response = rest.exchange(url, HttpMethod.POST, entity, String.class);

		// 응답 JSON 파싱
		JsonNode root = objectMapper.readTree(response.getBody());
		String answer = root
				.path("candidates")
				.get(0)
				.path("content")
				.path("parts")
				.get(0)
				.path("text")
				.asText();

		   // "--" 기준으로 분리
        String[] sections = answer.split("--"); 


        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("bodyAnalysis", sections.length > 1 ? sections[1].trim() : "");
        resultMap.put("topRecommendation", sections.length > 2 ? sections[2].trim() : "");
        resultMap.put("bottomRecommendation", sections.length > 3 ? sections[3].trim() : "");
        
        System.out.println("체형 분석: " + resultMap.get("bodyAnalysis"));
        System.out.println("상의 추천: " + resultMap.get("topRecommendation"));
        System.out.println("하의 추천: " + resultMap.get("bottomRecommendation"));
        
		return Map.of("answer", resultMap);
	}

}
