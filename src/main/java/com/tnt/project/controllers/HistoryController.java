package com.tnt.project.controllers;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import com.tnt.project.dto.HistoryDTO;
import com.tnt.project.services.FileService;
import com.tnt.project.services.HistoryService;



@RestController
@RequestMapping("/history")
public class HistoryController {

	@Autowired
	private HistoryService historyService;

	@Autowired
	private FileService fileService;

	@GetMapping("/list")
	public List<HistoryDTO> getHistoryList(@RequestParam String memberId) {

		HistoryDTO dto = new HistoryDTO();

		List<HistoryDTO> list = historyService.getHistoryList(memberId);

		return list;

	}


	@GetMapping("/download")
	public ResponseEntity<byte[]> downloadHistory(@RequestParam int seq) throws UnsupportedEncodingException {
		
		
		HistoryDTO history = historyService.getHistoryBySeq(seq);

		if (history == null) {
			return ResponseEntity.notFound().build();
		}

		// GCP URL에서 파일 이름 추출
		String fileUrl = history.getResultUrl(); // 예: 결과 이미지 다운로드
		String fileName = history.getName();   // 브라우저 저장용 이름

		// URL에서 실제 GCP object 이름 추출
		// https://storage.googleapis.com/{bucket}/{objectName} 형태
		String objectName = fileUrl.substring(fileUrl.indexOf("/", 31) + 1); // "closet/upper/xxx.jpg"

		// FileService로 GCP에서 다운로드
		byte[] data = fileService.download(objectName);
		
		String encodedFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8);

		    return ResponseEntity.ok()
		            .header("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName)
		            .contentType(MediaType.APPLICATION_OCTET_STREAM)
		            .body(data);
	}



	@DeleteMapping("/delete")
	public int deleteHistory(@RequestParam int seq) {

		int del = historyService.deleteHistory(seq);

		return del;
	}


}
