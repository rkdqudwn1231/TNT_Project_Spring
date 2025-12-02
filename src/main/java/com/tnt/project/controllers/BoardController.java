package com.tnt.project.controllers;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
            System.out.println(boardSeq);

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
    
    
 // 게시글 목록 조회
    @GetMapping("/list")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(boardService.findAll());
    }

    
    
}
