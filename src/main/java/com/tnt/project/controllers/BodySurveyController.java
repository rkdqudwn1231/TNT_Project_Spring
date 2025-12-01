package com.tnt.project.controllers;

import com.tnt.project.dto.BodySurveyDTO;
import com.tnt.project.services.BodySurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/body")
public class BodySurveyController {

    @Autowired
    private BodySurveyService service;

    @PostMapping("/survey")
    public Map<String, Object> analyzeSurvey(@RequestBody BodySurveyDTO dto) {
        return service.analyzeSurvey(dto);
    }
}
