package com.tnt.project.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import com.tnt.project.dto.MemberDTO;
import com.tnt.project.services.MemberService;
import com.tnt.project.services.FileService;

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
    
    // 체형 진단 결과 후 결과 저장하기 버튼 눌렀을 때 member에 body_shape에 업데이트
    @PostMapping("/bodyShape")
    public ResponseEntity<?> updateBodyShape(@RequestBody Map<String, String> request,Authentication authentication ) {
    	
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("NEED_LOGIN");
        }

        String loginId = authentication.getName();
        String body_type = request.get("body_type");

        int result = memberService.updateBodyShape(loginId, body_type);
        return result > 0 ? ResponseEntity.ok("SUCCESS") : ResponseEntity.badRequest().body("FAIL");
    }

}
