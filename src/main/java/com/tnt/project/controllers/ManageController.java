package com.tnt.project.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tnt.project.dto.SessionLogDTO;
import com.tnt.project.services.ManageService;
import com.tnt.project.utils.JwtUtil;

@RestController
@RequestMapping("/manage")
public class ManageController {

	
	@Autowired
	private JwtUtil jwtUtil;
	
	private final ObjectMapper objectMapper;
	private final ManageService manageService;

	public ManageController(ObjectMapper objectMapper,
			ManageService manageService) {
		this.objectMapper = objectMapper;
		this.manageService = manageService;
	}

	@PostMapping(value = "/session-log", consumes = "application/json")
	public ResponseEntity<?>  saveSessionLog(@RequestBody String body,
			Authentication authentication) throws Exception {
		
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build(); // ✅ 비회원 차단
        }

        JsonNode node = objectMapper.readTree(body);

        String startTime = node.get("startTime").asText();
        String endTime   = node.get("endTime").asText();

        String loginId = authentication.getName();

        //manageService.save(loginId, startTime, endTime);

        return ResponseEntity.ok().build();
    }
	
	 @PostMapping("/logout/beacon")
	    public ResponseEntity<String> logoutByBeacon(@RequestBody String body) throws Exception {
	     System.out.println("테스트제발!!! : " + LocalDateTime.now());   
		 JsonNode node = objectMapper.readTree(body);
	        String token = node.has("token") ? node.get("token").asText() : "NO_TOKEN";

	        // 토큰 없으면 UNKNOWN 처리
	        String userId = jwtUtil.validateToken(token) ? jwtUtil.getIdFromToken(token) : "UNKNOWN";

	        SessionLogDTO log = new SessionLogDTO();
	        log.setId(userId);
	        log.setLogout_type("BEACON");

	        manageService.logout(log);

	        System.out.println("Logout beacon received for: " + userId);

	        return ResponseEntity.ok("logout recorded");
	    }
}
