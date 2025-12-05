package com.tnt.project.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

import com.tnt.project.dto.BoardDTO;
import com.tnt.project.services.BoardService;
import com.tnt.project.services.BoardTagService;
import com.tnt.project.services.FileService;

@RestController
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private FileService fileService;

    @Autowired
    private BoardTagService boardTagService;

    // 게시글 + 파일 업로드
    @PostMapping("/write")
    public ResponseEntity<?> write(
        @RequestPart("photo") MultipartFile photo,
        @RequestPart("board") BoardDTO dto
    ) {

        try {
            // 0) 작성자 ID 필수 체크 (member.id)
            if (dto.getId() == null || dto.getId().isBlank()) {
                return ResponseEntity.badRequest().body("작성자 ID가 누락되었습니다.");
            }

            // 1) 이미지 UUID 생성
            String uuid = UUID.randomUUID().toString();
            String fileName = "board/" + uuid + "_" + photo.getOriginalFilename();

            // 2) GCP 업로드
            String url = fileService.upload(
                photo.getBytes(),
                fileName,
                photo.getContentType()
            );

            // 3) DTO에 이미지 정보 저장
            dto.setImage_uuid(uuid);
            dto.setImage_original(photo.getOriginalFilename());
            dto.setImage_url(url);

            // 4) 게시글 저장 → seq 반환
            int boardSeq = boardService.insert(dto);

            // 5) 태그 저장
            if (dto.getTag() != null && !dto.getTag().isBlank()) {

                // 공백 기준으로 다중 태그 분리: "OOTD 여름룩 데일리룩"
                String[] tags = dto.getTag().split("\\s+");

                for (String tg : tags) {
                    if (!tg.isBlank()) {
                        boardTagService.insertTag(boardSeq, tg.trim());
                    }
                }
            }

            return ResponseEntity.ok("OK");

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("파일 업로드 실패");
        }
    }
    
    
    @PutMapping("/update/{seq}")
    public ResponseEntity<?> update(
            @PathVariable("seq") int seq,
            @RequestBody BoardDTO dto
    ) {
        dto.setSeq(seq); // path 로 받은 seq를 DTO에 세팅

        int result = boardService.update(dto);
        if (result == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("OK");
    }
    
    // 게시글 목록 조회
    @GetMapping("/list")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(boardService.findAll());
    }

    // 게시글 상세 조회
    // React: GET /board/detail/{seq}
    // - BoardDetail.jsx 에서 사용
    // - 조회수(read_count)는 서비스에서 증가 처리
    @GetMapping("/detail/{seq}")
    public ResponseEntity<?> detail(@PathVariable("seq") int seq) {
        BoardDTO dto = boardService.getDetail(seq);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    // 게시글 삭제
    // React: DELETE /board/delete/{seq}
    // - 작성자 체크는 프론트 또는 서비스/인터셉터에서 처리 가능
    @DeleteMapping("/delete/{seq}")
    public ResponseEntity<?> delete(@PathVariable("seq") int seq) {
        int result = boardService.delete(seq);
        if (result == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("OK");
    }

    // 좋아요 토글
    // React: POST /board/like/{seq}?memberId=xxx
    // - 아무 반응 없음     → LIKE 추가
    // - 이미 LIKE 상태     → 반응 삭제 (좋아요 취소)
    // - DISLIKE 상태에서   → LIKE 로 변경
    @PostMapping("/like/{seq}")
    public ResponseEntity<?> like(
            @PathVariable("seq") int seq,
            @RequestParam("memberId") String memberId
    ) {
        if (memberId == null || memberId.isBlank()) {
            return ResponseEntity.badRequest().body("memberId가 필요합니다.");
        }

        boardService.reactLike(seq, memberId);
        return ResponseEntity.ok("OK");
    }

    // 싫어요 토글
    // React: POST /board/dislike/{seq}?memberId=xxx
    // - 아무 반응 없음     → DISLIKE 추가
    // - 이미 DISLIKE 상태 → 반응 삭제 (싫어요 취소)
    // - LIKE 상태에서     → DISLIKE 로 변경
    @PostMapping("/dislike/{seq}")
    public ResponseEntity<?> dislike(
            @PathVariable("seq") int seq,
            @RequestParam("memberId") String memberId
    ) {
        if (memberId == null || memberId.isBlank()) {
            return ResponseEntity.badRequest().body("memberId가 필요합니다.");
        }

        boardService.reactDislike(seq, memberId);
        return ResponseEntity.ok("OK");
    }

    // 좋아요 상위 Top 10 조회
    // React: GET /board/top10
    // - 나중에 메인 상단 고정용으로 사용
    @GetMapping("/top10")
    public ResponseEntity<?> top10() {
        return ResponseEntity.ok(boardService.findTopByLikeCount(10));
    }

 // BoardController.java

    @GetMapping("/{seq}/reaction")
    public ResponseEntity<?> getReaction(
            @PathVariable("seq") int seq,
            @RequestParam("memberId") String memberId
    ) {
        if (memberId == null || memberId.isBlank()) {
            return ResponseEntity.badRequest().body("memberId가 필요합니다.");
        }

        String reaction = boardService.getMyReaction(seq, memberId);

        Map<String, Object> body = new HashMap<>();
        body.put("reaction", reaction); // "LIKE" / "DISLIKE" / null

        return ResponseEntity.ok(body);
    }

    
}
