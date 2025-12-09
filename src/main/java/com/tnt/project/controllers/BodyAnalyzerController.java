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
			    "text", "너는 체형 분석과 스타일링 전문가다.\r\n"
			          + "사용자가 업로드한 전신 사진을 기준으로 체형 분석하여 결과를 작성한다."
			          + "아래 형식 외의 문장 문구 설명 주의 문장은 절대 출력하지 않는다.\r\n"
			          + "이모지와 불필요한 기호는 사용하지 않는다\r\n"
			          + "-- 체형 형태 분석\r\n"
			          + " (삼각형, 역삼각형, 원형, 모래시계형, 직사각형) 중 하나만 작성\r\n"
			          + "-- 체형 분석\r\n"
			          + "전반적인 체형 분석 결과를 5문장 이상으로 상세히 작성\r\n"
			          + "-- 상의 추천\r\n"
			          + "체형 분석 결과를 기반으로 상의 추천을 5문장 이상으로 상세히 추천\r\n"
			          + "-- 하의 추천\r\n"
			          + "체형 분석 결과를 기반으로 하의 추천을 5문장 이상으로 상세히 추천"
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

		System.out.println("답변:"+answer);
		// "--" 기준으로 분리
		String[] sections = answer.split("--");
		
		Map<String, String> resultMap = new HashMap<>();

		resultMap.put("bodyType", clean(sections.length > 1 ? sections[1] : ""));
		resultMap.put("bodyAnalysis", clean(sections.length > 2 ? sections[2] : ""));
		resultMap.put("topRecommendation", clean(sections.length > 3 ? sections[3] : ""));
		resultMap.put("bottomRecommendation", clean(sections.length > 4 ? sections[4] : ""));

		System.out.println("체형 형태: " + resultMap.get("bodyType"));
		System.out.println("체형 분석: " + resultMap.get("bodyAnalysis"));
		System.out.println("상의 추천: " + resultMap.get("topRecommendation"));
		System.out.println("하의 추천: " + resultMap.get("bottomRecommendation"));

		return Map.of("answer", resultMap);
	}
	
	private String clean(String text) {
	    if (text == null || text.isBlank()) return "";

	    // 첫 줄(제목) 제거
	    String[] lines = text.trim().split("\n", 2);
	    return lines.length > 1 ? lines[1].trim() : lines[0].trim();
	}

}
