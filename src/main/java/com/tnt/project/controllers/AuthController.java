package com.tnt.project.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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


    //  이메일 인증 링크 발송
    //    POST /auth/send-verify-link
    //    body: { "email": "user@example.com" }

    @PostMapping("/send-verify-link")
    public ResponseEntity<?> sendVerifyLink(@RequestBody Map<String, String> body) {

        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "이메일이 없습니다."));
        }

        try {
            authService.sendVerifyLink(email); // 토큰 생성 + DB 저장 + 메일 발송
            return ResponseEntity.ok(
                    Map.of("message", "인증 링크가 이메일로 발송되었습니다.")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "인증 링크 발송 중 오류가 발생했습니다."));
        }
    }

    // 2) 이메일 인증 처리
    //    GET /auth/verify?token=..
    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {

        try {
            boolean ok = authService.verifyEmail(token);

            if (!ok) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("message", "유효하지 않거나 만료된 인증 링크입니다."));
            }

            return ResponseEntity.ok(
                    Map.of("message", "이메일 인증이 완료되었습니다.")
            );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "인증 처리 중 오류가 발생했습니다."));
        }
    }


    //  로그인

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> user) {
        String id = user.get("id");
        String pw = user.get("pw");

        try {
            // 로그인 검증 + 회원 정보 획득
            MemberDTO member = authService.login(id, pw);

            // 권한 생성
            List<String> roles = new ArrayList<>();
            if ("admin".equals(member.getId())) {
                roles.add("ADMIN");
            } else {
                roles.add("MEMBER");
            }

            // JWT 생성 (id 기준)
            String token = jwt.createToken(member.getId(), roles);

            // 응답
            return ResponseEntity.ok(
                    Map.of(
                            "token", token,
                            "id", member.getId(),
                            "nickname", member.getNickname(),
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
