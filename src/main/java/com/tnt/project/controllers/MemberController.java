package com.tnt.project.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tnt.project.dto.MemberDTO;
import com.tnt.project.services.FileService;
import com.tnt.project.services.MemberService;

@RestController
@RequestMapping("/member")
public class MemberController {
	
    @Autowired
    private MemberService memberService;
    
    @Autowired
    private FileService fileService;

    // 회원가입
    @PostMapping("/signup")
    public String signup(@RequestBody MemberDTO member) {
        memberService.signup(member);
        return "{\"message\":\"회원가입 성공\"}";
    }
    
    

    // 아이디 중복 검사
    @PostMapping("/check-id")
    public ResponseEntity<?> checkId(@RequestBody Map<String, String> body) {
        String id = body.get("id");
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("available", false, "message", "아이디가 비었습니다."));
        }

        boolean exists = memberService.checkId(id);
        System.out.println("아이디췍");
        return ResponseEntity.ok(
                Map.of("available", !exists)
        );
    }
    // 닉네임 중복 검사

    @PostMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestBody Map<String, String> body) {
        String nickname = body.get("nickname");
        if (nickname == null || nickname.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("available", false, "message", "닉네임이 비었습니다."));
        }

        boolean exists = memberService.checkNickname(nickname);
        System.out.println("닉네임췍");
        return ResponseEntity.ok(
                Map.of("available", !exists)
        );
    }
    
    @GetMapping("/find-id")
    public ResponseEntity<Map<String, Object>> findIdByEmail(@RequestParam String email) {
        String id = memberService.findIdByEmail(email);

        Map<String, Object> body = new HashMap<>();
        body.put("id", id); // 찾으면 아이디, 없으면 null

        return ResponseEntity.ok(body);
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        String newPassword = req.get("newPassword");

        if (email == null || newPassword == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "이메일 또는 비밀번호가 누락되었습니다."));
        }

        boolean ok = memberService.updatePassword(email, newPassword);

        if (ok) {
            return ResponseEntity.ok(Map.of("message", "비밀번호 변경 완료"));
        } else {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body(Map.of("message", "해당 이메일로 가입된 계정을 찾을 수 없습니다."));
        }
    }
    
    // 마이페이지 - 내 정보 조회
    @GetMapping("/mypage/{id}")
    public MemberDTO getMyPage(@PathVariable("id") String id) {
        return memberService.findById(id);
    }

    // 마이페이지 - 내 정보 수정 (닉네임, 연락처, 퍼스널 컬러, 체형, 프로필 이미지 URL)
    @PutMapping("/mypage/{id}")
    public String updateMyPage(
            @PathVariable("id") String id,
            @RequestBody MemberDTO dto
    ) {
        // 어떤 회원을 수정할지 지정
        dto.setId(id);

        memberService.updateMyPage(dto);

        return "{\"message\":\"마이페이지 수정 완료\"}";
    }

    /**
     * 마이페이지 - 프로필 이미지 업로드
     * 프론트: POST /member/mypage/{id}/profile
     * body: multipart/form-data, key = file
     *
     * 응답 예시:
     * {
     *   "url": "https://.../profile/xxxx.jpg",
     *   "uuid": "xxxx-uuid",
     *   "original": "원본파일명.jpg"
     * }
     */
    @PostMapping("/mypage/{id}/profile")
    public ResponseEntity<?> uploadProfileImage(
            @PathVariable("id") String id,
            @RequestPart("file") MultipartFile file
    ) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("파일이 없습니다.");
        }

        try {
            // 1) UUID 생성
            String uuid = UUID.randomUUID().toString();
            String originalName = file.getOriginalFilename();

            // 2) GCP 업로드용 경로
            String objectName = "profile/" + uuid + "_" + originalName;

            // 3) GCP 업로드
            String url = fileService.upload(
                    file.getBytes(),
                    objectName,
                    file.getContentType()
            );

            // 4) 프론트로 넘겨줄 정보
            Map<String, Object> body = new HashMap<>();
            body.put("url", url);
            body.put("uuid", uuid);
            body.put("original", originalName);

            return ResponseEntity.ok(body);

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("프로필 이미지 업로드 실패");
        }
    }
}
