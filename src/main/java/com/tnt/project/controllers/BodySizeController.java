package com.tnt.project.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnt.project.dto.BodySizeDTO;
import com.tnt.project.services.BodySizeService;

@RestController
@RequestMapping("/bodySize")
public class BodySizeController {

    @Autowired
    private BodySizeService bodySizeService;

    @PostMapping("/insert")
    public Map<String, Object> analyzeBySize( @RequestBody BodySizeDTO bodySizeDTO,   Authentication authentication ) {
    	
        String loginId = (authentication != null ? authentication.getName() : null);
        return bodySizeService.analyze(bodySizeDTO, loginId);
        
    }
}
