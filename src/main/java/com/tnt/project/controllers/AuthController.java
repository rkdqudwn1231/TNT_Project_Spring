package com.tnt.project.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tnt.project.dto.MemberDTO;
import com.tnt.project.dto.SessionLogDTO;
import com.tnt.project.services.AuthService;
import com.tnt.project.services.ManageService;
import com.tnt.project.utils.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


	@Autowired
	private ManageService manageService;
	
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
    
    
    // 아이디 찾기용 메일 발송
    @PostMapping("/send-verify-link/find-id")
    public ResponseEntity<?> sendFindIdVerifyLink(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "이메일이 없습니다."));
        }

        try {
            authService.sendFindIdVerifyLink(email);
            return ResponseEntity.ok(
                    Map.of("message", "아이디 찾기용 인증 링크가 이메일로 발송되었습니다.")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "아이디 찾기 인증 메일 발송 중 오류가 발생했습니다."));
        }
    }

    // 비밀번호 찾기용 메일 발송
    @PostMapping("/send-verify-link/reset-password")
    public ResponseEntity<?> sendResetPwVerifyLink(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "이메일이 없습니다."));
        }

        try {
            authService.sendResetPwVerifyLink(email);
            return ResponseEntity.ok(
                    Map.of("message", "비밀번호 재설정용 인증 링크가 이메일로 발송되었습니다.")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "비밀번호 재설정 인증 메일 발송 중 오류가 발생했습니다."));
        }
    }
    

    // 2) 이메일 인증 처리
    //    GET /auth/verify?token=..&type=signup/findId/resetPw
    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(
            @RequestParam("token") String token,
            @RequestParam(value = "type", required = false) String type
    ) {

        boolean ok = authService.verifyEmail(token);

        // type 기본값
        if (type == null || type.isBlank()) {
            type = "signup";
        }

        if (!ok) {
            String failHtml = """
                <!DOCTYPE html>
                <html lang="ko">
                <head>
                    <meta charset="UTF-8" />
                    <title>TNT FitRoom - 이메일 인증 실패</title>
                    <style>
                        * { box-sizing: border-box; margin: 0; padding: 0; }
                        body {
                            font-family: -apple-system, BlinkMacSystemFont, "Noto Sans KR", system-ui, sans-serif;
                            background: #f9fafb;
                            color: #111827;
                        }
                        .topbar {
                            width: 100%%;
                            height: 64px;
                            background: #ffc4da;
                            display: flex;
                            align-items: center;
                            padding: 0 40px;
                        }
                        .logo {
                            font-size: 26px;
                            font-weight: 800;
                            color: #ffffff;
                        }
                        .wrapper {
                            min-height: calc(100vh - 64px);
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            padding: 40px 16px;
                        }
                        .card {
                            max-width: 640px;
                            width: 100%%;
                            background: #ffffff;
                            border-radius: 24px;
                            padding: 32px 36px;
                            box-shadow: 0 18px 45px rgba(148, 163, 184, 0.35);
                        }
                        .title {
                            font-size: 22px;
                            font-weight: 700;
                            color: #b91c1c;
                            margin-bottom: 12px;
                        }
                        .subtitle {
                            font-size: 15px;
                            color: #4b5563;
                            line-height: 1.6;
                            margin-top: 16px;
                        }
                    </style>
                </head>
                <body>
                    <header class="topbar">
                        <div class="logo">TNT</div>
                    </header>
                    <main class="wrapper">
                        <section class="card">
                            <h1 class="title">이메일 인증에 실패했습니다.</h1>
                            <p class="subtitle">
                                유효하지 않거나 만료된 링크입니다.<br/>
                                다시 시도해 주세요.
                            </p>
                        </section>
                    </main>
                </body>
                </html>
                """;

            return ResponseEntity
                    .badRequest()
                    .header("Content-Type", "text/html; charset=UTF-8")
                    .body(failHtml);
        }

        // 성공 페이지: type 에 따라 문구만 다르게
        String headline;
        String text;
        String hint;

        switch (type) {
            case "findId" -> {
                headline = "아이디 찾기 이메일 인증이 완료되었습니다.";
                text = " 아이디 찾기 페이지로 돌아가\n"
                     + "인증 완료 버튼을 누르면 아이디를 확인하실 수 있습니다.";
                hint = "이 창은 닫으셔도 됩니다.";
            }
            case "resetPw" -> {
                headline = "비밀번호 재설정을 이메일 인증이 완료되었습니다.";
                text = "비밀번호 재설정 페이지로 돌아가\n"
                     + "새 비밀번호를 입력하고 저장해 주세요.";
                hint = "이 창은 닫으셔도 됩니다.";
            }
            default -> { // signup
                headline = "회원가입을 이메일 인증이 완료되었습니다.";
                text = "TNT 회원가입 페이지로 돌아가 나머지 정보를 입력한 뒤,\n"
                     + "가입을 마무리해 주세요.";
                hint = "이 창은 닫으셔도 됩니다.";
            }
        }

        String successHtml = """
            <!DOCTYPE html>
            <html lang="ko">
            <head>
                <meta charset="UTF-8" />
                <title>TNT FitRoom - 이메일 인증 완료</title>
                <style>
                    * { box-sizing: border-box; margin: 0; padding: 0; }
                    body {
                        font-family: -apple-system, BlinkMacSystemFont, "Noto Sans KR", system-ui, sans-serif;
                        background: #f9fafb;
                        color: #111827;
                    }
                    .topbar {
                        width: 100%%;
                        height: 64px;
                        background: #ffc4da;
                        display: flex;
                        align-items: center;
                        padding: 0 40px;
                    }
                    .logo {
                        font-size: 26px;
                        font-weight: 800;
                        color: #ffffff;
                    }
                    .wrapper {
                        min-height: calc(100vh - 64px);
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        padding: 40px 16px;
                    }
                    .card {
                        max-width: 720px;
                        width: 100%%;
                        background: #ffffff;
                        border-radius: 24px;
                        padding: 36px 40px;
                        box-shadow: 0 18px 45px rgba(148, 163, 184, 0.35);
                    }
                    .headline {
                        font-size: 24px;
                        font-weight: 700;
                        color: #111827;
                        margin-bottom: 16px;
                    }
                    .text {
                        font-size: 15px;
                        color: #4b5563;
                        line-height: 1.7;
                        white-space: pre-line;
                        margin-bottom: 18px;
                    }
                    .hint {
                        font-size: 13px;
                        color: #9ca3af;
                    }
                </style>
            </head>
            <body>
                <header class="topbar">
                    <div class="logo">TNT</div>
                </header>
                <main class="wrapper">
                    <section class="card">
                        <h1 class="headline">%s</h1>
                        <p class="text">%s</p>
                        <p class="hint">%s</p>
                    </section>
                </main>
            </body>
            </html>
            """.formatted(headline, text, hint);

        return ResponseEntity
                .ok()
                .header("Content-Type", "text/html; charset=UTF-8")
                .body(successHtml);
    }

    @GetMapping("/email-verified")
    public ResponseEntity<?> isEmailVerified(@RequestParam("email") String email) {
    	System.out.println("이메일옴");
        if (email == null || email.isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("verified", false, "message", "이메일이 없습니다."));
        }

        boolean verified = authService.isEmailVerified(email);
        
        return ResponseEntity.ok(
                Map.of("verified", verified)
        );
    }

    //  로그인

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> user) {
        String id = user.get("id");
        String pw = user.get("pw");

        try {
            // 로그인 검증 + 회원 정보 획득
            MemberDTO member = authService.login(id, pw);
            
            
            
            if(member.getBlack().equals("true")) {
            	System.out.println("테스트");
            	 return ResponseEntity
                         .status(HttpStatus.UNAUTHORIZED)
                         .body(Map.of("error","블랙유저"));
            }
            
            System.out.println("블랙 체크:"+ member.getBlack());
            
            // 권한 생성
            List<String> roles = new ArrayList<>();
            if ("admin".equals(member.getId())) {
                roles.add("ADMIN");
            } else {
                roles.add("MEMBER");
            }
            
            // JWT 생성 (id 기준)
            String token = jwt.createToken(member.getId(), roles);


			//디비에 로그인 접속시간 추가.
			manageService.login(member.getId());
            
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
    
    @PostMapping("/logout")
	public ResponseEntity<String> logout(Authentication authentication) {
	    String id = authentication.getName();
	    String type = "NORMAL"; // 기본 NORMAL
	    
	    SessionLogDTO sessionLogDTO = new SessionLogDTO();
	    sessionLogDTO.setId(id);
	    sessionLogDTO.setLogout_type(type);
	    manageService.logout(sessionLogDTO); 
	    return ResponseEntity.ok("logout recorded");
	}
	
    
}
