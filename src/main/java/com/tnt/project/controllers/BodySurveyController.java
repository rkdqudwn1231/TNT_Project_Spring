package com.tnt.project.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnt.project.dto.BodySurveyDTO;
import com.tnt.project.services.BodySurveyService;

@RestController
@RequestMapping("/api/body")
public class BodySurveyController {

    @Autowired
    private BodySurveyService service;

    @PostMapping("/survey")
    public Map<String, Object> analyzeSurvey(@RequestBody BodySurveyDTO dto , Authentication authentication) {
       
    	// 로그인 사용자 아이디(JWT에서 복원된 username)
    	if(authentication != null) {
    		
    		String loginId = authentication.getName();    		
    		dto.setMember_id(loginId);
    		
    	}
    	
    	return service.analyzeSurvey(dto);
    }

}