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
@RequestMapping("/bodySurvey")
public class BodySurveyController {

    @Autowired
    private BodySurveyService bodySurveyService;

    @PostMapping("/insert")
    public Map<String, Object> analyzeSurvey(@RequestBody BodySurveyDTO bodySurveyDTO , 
    													Authentication authentication) {
       
    	// 로그인 사용자 아이디
    	if(authentication != null) {
    		
    		String loginId = authentication.getName();    		
    		bodySurveyDTO.setMember_id(loginId);
    		
    	}
    	
    	return bodySurveyService.analyzeSurvey(bodySurveyDTO);
    }

}