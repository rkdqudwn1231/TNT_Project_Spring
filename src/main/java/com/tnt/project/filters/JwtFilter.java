package com.tnt.project.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tnt.project.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwt;


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String header = request.getHeader("Authorization");
		if(header ==null || !header.startsWith("Bearer")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = header.substring(7); //Bearer 토큰뭐시기 여서 7글자 떼어냄.
		//validateToken에서 예외를 반환해서 try catch로 묶음
		try {
			if(jwt.validateToken(token)) { //토큰이 유효한 경우
				String id = jwt.getIdFromToken(token);
				List<String> roles = jwt.getRolesFromToken(token);
				
				List<SimpleGrantedAuthority> auths = new ArrayList<>(); // 권한 목록 저장용 리스트
				for(String role : roles) {
					auths.add(new SimpleGrantedAuthority("ROLE_"+role));	//스프링 싴큐리티가 요구하는 Prefix 와 데이터 타입.
				}

				UsernamePasswordAuthenticationToken authentication = 
						new UsernamePasswordAuthenticationToken(id,null,auths); //id, pw, 권한 목록
				
				SecurityContextHolder.getContext().setAuthentication(authentication); // S.C.H에 인증및 인가 정보를 저장
				
			}
		}
		catch(Exception e) {
			SecurityContextHolder.clearContext(); //저장된 컨텍스트 홀더를 clear 시킴
		}
		filterChain.doFilter(request, response);
	}
}