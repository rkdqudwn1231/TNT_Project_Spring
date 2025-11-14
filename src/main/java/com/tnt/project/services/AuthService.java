package com.tnt.project.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class AuthService {
	
	public List<String> login(String id, String pw){
		//dao 가서 로그인 처리 해주는 로직까지 있을거지만 예제니까 패스
		//아이디랑 비번은 컨트롤러에서 있으니까 규칙만 반환
		List<String> roles = new ArrayList();
		
		if(true) {
			if(id.equals("admin")) {
				roles.add("ADMIN");
			}
			else {
				roles.add("MEMBER");
			}

			return roles;
		}
		else {
			throw new RuntimeException();//예외를 만들어서 내보낸다.
		}
	}
}
