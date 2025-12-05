package com.tnt.project.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tnt.project.dto.BoardCommentDTO;
import com.tnt.project.services.BoardCommentService;

@RestController
@RequestMapping("/board")
public class BoardCommentController {

    @Autowired
    private BoardCommentService boardCommentService;

    // 댓글 목록
    @GetMapping("/{boardSeq}/comments")
    public ResponseEntity<?> listComments(@PathVariable("boardSeq") int boardSeq) {
        List<BoardCommentDTO> list = boardCommentService.findByBoardSeq(boardSeq);
        return ResponseEntity.ok(list);
    }

    // 댓글 등록
    @PostMapping("/{boardSeq}/comments")
    public ResponseEntity<?> addComment(
            @PathVariable("boardSeq") int boardSeq,
            @RequestBody Map<String, String> body
    ) {

        String content = body.get("content");
        String memberId = body.get("member_id");             // 로그인한 회원 ID
        String memberNickname = body.get("member_nickname"); // 화면에 보여줄 닉네임

        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("댓글 내용은 필수입니다.");
        }
        if (memberId == null || memberId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("작성자 ID가 없습니다.");
        }
        if (memberNickname == null || memberNickname.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("작성자 닉네임이 없습니다.");
        }

        BoardCommentDTO dto = new BoardCommentDTO();
        dto.setBoard_seq(boardSeq);
        dto.setContent(content.trim());
        dto.setMember_id(memberId.trim());
        dto.setMember_nickname(memberNickname.trim());

        boardCommentService.insert(dto);

        return ResponseEntity.ok().build();
    }

    // 대댓글 등록
    @PostMapping("/{boardSeq}/comments/{parentSeq}")
    public ResponseEntity<?> addReply(
            @PathVariable int boardSeq,
            @PathVariable int parentSeq,
            @RequestBody Map<String, String> body
    ) {
        String content = body.get("content");
        String memberId = body.get("member_id");
        String nickname = body.get("member_nickname");

        if (content == null || content.isBlank()) {
            return ResponseEntity.badRequest().body("내용 없음");
        }
        if (memberId == null || memberId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("작성자 ID가 없습니다.");
        }
        if (nickname == null || nickname.isBlank()) {
            return ResponseEntity.badRequest().body("작성자 닉네임이 없습니다.");
        }

        // 🔥 서비스 시그니처에 맞게 호출
        boardCommentService.insertReply(
                boardSeq,
                parentSeq,
                memberId.trim(),
                nickname.trim(),
                content.trim()
        );

        return ResponseEntity.ok().build();
    }

    /**
     * 댓글 수정
     * PUT /board/{boardSeq}/comments/{commentSeq}
     * body: { "content": "...", "member_id": "로그인ID" }
     */
    @PutMapping("/{boardSeq}/comments/{commentSeq}")
    public ResponseEntity<?> updateComment(
            @PathVariable int boardSeq,
            @PathVariable int commentSeq,
            @RequestBody Map<String, String> body
    ) {
        String content = body.get("content");
        String memberId = body.get("member_id");

        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("댓글 내용은 필수입니다.");
        }
        if (memberId == null || memberId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("작성자 ID가 없습니다.");
        }

        boolean success = boardCommentService.updateContent(
                commentSeq,
                memberId.trim(),
                content.trim()
        );

        if (!success) {
            return ResponseEntity.status(403).body("댓글을 수정할 권한이 없습니다.");
        }

        return ResponseEntity.ok().build();
    }

    /**
     * 댓글 삭제 (soft delete)
     * DELETE /board/{boardSeq}/comments/{commentSeq}
     * body: { "member_id": "로그인ID" }
     */
    @DeleteMapping("/{boardSeq}/comments/{commentSeq}")
    public ResponseEntity<?> deleteComment(
            @PathVariable int boardSeq,
            @PathVariable int commentSeq,
            @RequestBody Map<String, String> body
    ) {
        String memberId = body.get("member_id");

        if (memberId == null || memberId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("작성자 ID가 없습니다.");
        }

        boolean success = boardCommentService.softDelete(
                commentSeq,
                memberId.trim()
        );

        if (!success) {
            return ResponseEntity.status(403).body("댓글을 삭제할 권한이 없습니다.");
        }

        return ResponseEntity.ok().build();
    }
}
