package com.tnt.project.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnt.project.services.ChatbotService;

@RestController
@RequestMapping("/chatbot")
public class ChatbotController {

	@Autowired
	private ChatbotService chatbotService;

	@PostMapping("/ask")
	public ResponseEntity<Map<String, Object>> ask(@RequestBody Map<String, Object> body,  Authentication authentication) {
		String loginId="";   
		// 로그인 사용자 아이디(JWT에서 복원된 username)
		if(authentication != null) {
			loginId = authentication.getName();   


			String prompt =(String) body.get("prompt"); // 사용자가 입력한 문장
			List<Map<String, String>> history =  (List<Map<String, String>>) body.get("history");

			System.out.println(loginId);

			// service에서 처리해서 답변 보내줌.
			try {
				return ResponseEntity.ok(chatbotService.ask(loginId, prompt,history));
			} catch (Exception e) {
				System.out.println(e);
				return ResponseEntity.badRequest().body(Map.of("error", "요청 처리 중 오류 발생"));
			}
		}
		else
		{
			return ResponseEntity.status(401)
			        .body(Map.of("error", "로그인 필요"));
		}
	}

	@GetMapping
	public ResponseEntity<?> checkToken( Authentication authentication) {
		String loginId="";   
		// 로그인 사용자 아이디(JWT에서 복원된 username)
		if(authentication != null) {
			System.out.println("여기까지옴");
			loginId = authentication.getName();   
			return ResponseEntity.ok("true");
		}
		else
		{
			System.out.println("여기까지옴1");
			return ResponseEntity.status(401)
			        .body(Map.of("error", "false"));
		}
	}
	
	// ★ 대화 초기화 이것도 프론트엔드에서 받으면 필요가 없어졌다..
	@DeleteMapping
	public ResponseEntity<String> clear(@RequestBody Map<String, String> body) {
		if (chatbotService.removeHistory(body.get("userId")) > 0) {
			return ResponseEntity.ok("success");
		}

		return ResponseEntity.ok("no delete data");
	}

}
