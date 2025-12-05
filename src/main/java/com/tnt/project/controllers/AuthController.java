package com.tnt.project.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnt.project.dto.MemberDTO;
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
			// 1) 로그인 검증 + 회원 정보 획득
			MemberDTO member = authService.login(id, pw);

			// 2) 권한 생성
			List<String> roles = new ArrayList<>();
			if ("admin".equals(member.getId())) {
				roles.add("ADMIN");
			} else {
				roles.add("MEMBER");
			}

			// 3) JWT 생성 (id 기준)
			String token = jwt.createToken(member.getId(), roles);

			// 4) 프론트로 내려줄 정보에 nickname 추가
			return ResponseEntity.ok(
				Map.of(
					"token", token,
					"id", member.getId(),
					"nickname", member.getNickname(),      // ← 여기
					"roles", String.join(",", roles)
				)
			);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("error", e.getMessage()));
		}
	}
}
