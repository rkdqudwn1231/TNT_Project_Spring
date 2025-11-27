package com.tnt.project.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnt.project.services.AuthService;
import com.tnt.project.utils.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private JwtUtil jwt;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String,String> user){
		String id = user.get("id");
        String pw = user.get("pw");

		try {
			// 로그인 처리 + ROLE 반환
			List<String> roles = authService.login(id, pw);
			// JWT 생성
			String token = jwt.createToken(id, roles);

			return ResponseEntity.ok(Map.of(
				"token", token,
				"id", id,
				"roles", String.join(",", roles)
				
			));
		
		} catch (Exception e) {
			// 로그인 실패(아이디 또는 비번 불일치)
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("error", e.getMessage()));
		}
	}
}
