package com.tnt.project.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tnt.project.dao.ChatbotDAO;
import com.tnt.project.dto.ChatHistoryDTO;

@Service
public class ChatbotService {

	@Autowired
	private ChatbotDAO chatbotDAO;

	@Value("${gemini.api.key}")
	private String apiKey;  // application.properties에서 가져옴

	// ★ 유저별 대화 히스토리 메모리 저장소 ( 메모리 저장 방식인데 db 방식이 좋을거 같아서 수정 중 )
	//private Map<String, List<Map<String, String>>> memoryHistory = new HashMap<>();

	// ★ Gemini API 정보
	private final String URL =
			"https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-lite:generateContent?key=";


	// 전문가 프롬프트 (히스토리 prefix로 계속 삽입)
	//	private final String SYSTEM_PROMPT =  """
	//			너의 역할은 ‘퍼스널 컬러 & 체형 기반 스타일 추천 전문가’이다. 아래 규칙을 반드시 지킨다.
	//
	//			[역할 규칙]
	//			너는 전문 스타일리스트다. 일반적인 AI처럼 정보성 설명을 하지 않는다.
	//			답변은 간결하지만 전문적으로, 단정형 어조로 존댓말을 사용해 작성한다.
	//			분석 근거 또는 이론 설명을 늘어놓지 않는다. 결과와 추천만 제시한다.
	//
	//			[분석 규칙]
	//			사용자의 퍼스널 컬러를 4계절 톤(봄웜/여름쿨/가을웜/겨울쿨) 기준으로 분석한다.
	//			사용자의 체형(스트레이트/웨이브/내추럴 등)을 진단한다.
	//			정보가 부족하면 먼저 필요한 정보를 질문해 수집한다.
	//
	//			[추천 규칙]
	//			컬러, 핏, 스타일, 패브릭, 디테일, 악세서리 와 같은 패션 스타일을 구체적으로 추천한다.
	//			문장은 자연스러운 문장 형태로만 작성한다.
	//
	//			[금지 규칙]
	//			마크다운 문법(**, *, -, #, 리스트, 표 등) 절대 사용 금지.
	//			설명형 어투 금지. “아마”, “일 수도 있다”, “생각된다” 등의 추측형 금지.
	//			패션과 무관한 질문에는 다음처럼 답한다: 
	//			“현재 대화 주제는 패션 분석입니다. 퍼스널 컬러나 체형 관련 질문을 해주세요.”
	//			""";

	public Map<String, Object> ask(String userId,String prompt, List<Map<String, String>> history) throws Exception{
		// 1) 기존 히스토리 가져오기
		//		List<ChatHistoryDTO> history =
		//				chatbotDAO.getHistory(userId);

		// 2) Gemini가 요구하는 "contents" 배열 형태로 변환
		List<Map<String, Object>> contents = new ArrayList<>();

		//		//시스템 프롬프트 삽입. 굳이 제미나이 기능을 제한할 필요가 없다고 해서 뺌.
		//		contents.add(0, Map.of(
		//				"role", "model",
		//				"parts", List.of(Map.of("text", SYSTEM_PROMPT))
		//				));

		//		if (history.size() > 10) { //창을 껐다가 다시 키면 초기화 되는 형태로 할거라 요약 필요 없어짐.
		//			// ⭐ 여기에서 오래된 히스토리를 요약해줌
		//			String summary = createSummary(history);
		//			
		//			chatbotDAO.insertSummary(userId,summary);
		//			chatbotDAO.removeOldHistory(userId); // 오래된 기록 삭제
		//				
		//			System.out.println("서머리 : " + summary);
		//
		//			contents.add(Map.of(
		//					"role", "model",
		//					"parts", List.of(Map.of("text", "지금까지의 요약:\n" + summary))
		//					));
		//		}

		// 이전 멀티턴 히스토리 추가
		//		for (int i = 0; i < history.size(); i++) {
		//			ChatHistoryDTO h = history.get(i);
		//			contents.add(Map.of(
		//					"role", h.getRole(),
		//					"parts", List.of(Map.of("text", h.getMessage()))
		//					));
		//		}

		//프론트에서 히스토리 받아오는 방법.
		for (Map<String, String> h : history) {
			String role = h.get("sender").equals("user") ? "user" : "assistant";
			String msg = h.get("text");
			System.out.println(msg);
			contents.add(Map.of(
					"role", role,
					"parts", List.of(Map.of("text", msg))
					));
		}

		// 이번 사용자 입력 추가 프론트단에서 받아오기 때문에 또 넣어줄 필요가 없어짐.
//		contents.add(Map.of(
//				"role", "user",
//				"parts", List.of(Map.of("text", prompt))
//				));

		//프론트에서 히스토리 받아오는 방법. contents 에 있는 parts 내용 확인.
//		for (Map<String, Object> h : contents) {
//			String role = h.get("role").equals("user") ? "user" : "assistant";
//			List<Map<String, String>> parts = (List<Map<String, String>>) h.get("parts");
//		    String msg = parts.get(0).get("text"); // 첫 번째 메시지
//			System.out.println(msg);
//		}

		// 최종 JSON Body
		Map<String, Object> requestBody = Map.of("contents", contents);

		// 3) REST API 호출 준비
		RestTemplate rest = new RestTemplate();
		ObjectMapper mapper = new ObjectMapper();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity =
				new HttpEntity<>(mapper.writeValueAsString(requestBody), headers);

		// 4) Gemini API 호출 (POST)
		ResponseEntity<String> response =
				rest.exchange(URL + apiKey, HttpMethod.POST, entity, String.class);

		// 5) Gemini 응답 파싱
		JsonNode root = mapper.readTree(response.getBody());
		String answer = root
				.path("candidates")
				.get(0)
				.path("content")
				.path("parts")
				.get(0)
				.path("text")
				.asText();

		// 6) 히스토리 업데이트
//		chatbotDAO.insertHistory(userId, "user" , prompt);
//		chatbotDAO.insertHistory(userId, "assistant" , answer);

		// 7) 프론트로 응답
		return Map.of("answer", answer);
	}

	public int removeHistory(String userId){
		return chatbotDAO.removeHistory(userId);
	}

	//	//createSummary 비용 문제로 넣을지 말지 고민하다가 로그아웃 혹은 페이지를 닫았을 때 삭제되도록 틀어서 제거함.
	//	private String createSummary(List<ChatHistoryDTO> history) throws Exception {
	//
	//		// 히스토리 문자열로 합치기
	//		StringBuilder sb = new StringBuilder();
	//		for (ChatHistoryDTO h : history) {
	//			sb.append(h.getRole()).append(": ").append(h.getMessage()).append("\n");
	//		}
	//
	//		// 요약용 프롬프트
	//		String SUMMARY_PROMPT = """
	//				아래는 과거 대화 기록이다.
	//				사용자 정보(퍼스널 컬러, 체형, 스타일 취향)만 요약하고 모델의 답변 요약은 포함하지 않는다.
	//				퍼스널 컬러, 체형, 스타일 취향만 유지한다.
	//				불필요한 정보 제거.
	//				3줄 이하.
	//				""";
	//
	//		// Gemini 관련 contents 구성
	//		List<Map<String, Object>> contents = List.of(
	//				Map.of(
	//						"role", "model",
	//						"parts", List.of(Map.of("text", SUMMARY_PROMPT))
	//						),
	//				Map.of(
	//						"role", "user",
	//						"parts", List.of(Map.of("text", sb.toString()))
	//						)
	//				);
	//
	//		Map<String, Object> requestBody = Map.of("contents", contents);
	//
	//		RestTemplate rest = new RestTemplate();
	//		ObjectMapper mapper = new ObjectMapper();
	//
	//		HttpHeaders headers = new HttpHeaders();
	//		headers.setContentType(MediaType.APPLICATION_JSON);
	//
	//		HttpEntity<String> entity =
	//				new HttpEntity<>(mapper.writeValueAsString(requestBody), headers);
	//
	//		ResponseEntity<String> response =
	//				rest.exchange(URL + apiKey, HttpMethod.POST, entity, String.class);
	//
	//		// 응답에서 요약 텍스트만 추출
	//		JsonNode root = mapper.readTree(response.getBody());
	//		String summary = root
	//				.path("candidates")
	//				.get(0)
	//				.path("content")
	//				.path("parts")
	//				.get(0)
	//				.path("text")
	//				.asText();
	//
	//		
	//		
	//		return summary;
	//
	//
	//	}
}
