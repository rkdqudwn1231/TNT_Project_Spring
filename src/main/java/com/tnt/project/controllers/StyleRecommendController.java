package com.tnt.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.tnt.project.dto.ClosetDTO;
import com.tnt.project.dto.StyleRecommendDTO;
import com.tnt.project.services.ClosetService;
import com.tnt.project.services.StyleRecommendService;

@RestController
@RequestMapping("/recommend")
public class StyleRecommendController {

	@Autowired
	private ClosetService closetService;

	@Autowired
	private StyleRecommendService styleRecommendService;

	@PostMapping("/saveRecommend")
	public ResponseEntity<?> saveRecommend(@RequestBody ClosetDTO closetDTO , Authentication authentication) {

	    // 로그인 여부 체크
	    if (authentication == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("NEED_LOGIN");
	    }

	    // 로그인된 사용자라면 MemberId 세팅
	    String loginId = authentication.getName();
	    closetDTO.setMemberId(loginId);

	    int result = closetService.insertClosetFromRecommend(closetDTO);
	    return result > 0 ? ResponseEntity.ok("SUCCESS") : ResponseEntity.badRequest().body("FAIL");
	}

	@GetMapping("/list")
	public List<StyleRecommendDTO> getRecommendList(
			@RequestParam String body_type,
			@RequestParam String gender,
			@RequestParam String cloth_type  // upper / lower
			) {
		return styleRecommendService.getRecommendList(body_type, gender, cloth_type);
	}
	

}


