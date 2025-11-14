package com.tnt.project.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
	public ResponseEntity<Map<String,String>> login(@RequestBody Map<String,String> user){
		//보통은 dto를 리턴하는데 그게 귀찮으니 맵으로 가겠다.
		
		String id = user.get("id");
		String pw = user.get("pw");
		
		List<String> roles = authService.login(id, pw); // 권한을 받았다는 것은 로그인에 성공했다는 것.
		//이제 사용자 권한이랑 토큰을 만들어야하는데. jwt 가 필요함.
		
		String token = jwt.createToken(id, roles);
		return ResponseEntity.ok(Map.of("token",token));
	}

}
