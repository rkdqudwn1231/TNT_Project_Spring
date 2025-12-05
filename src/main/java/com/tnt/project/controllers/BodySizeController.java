package com.tnt.project.controllers;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.tnt.project.dto.BodySizeDTO;
import com.tnt.project.services.BodySizeService;

@RestController
@RequestMapping("/body")
public class BodySizeController {

    @Autowired
    private BodySizeService service;

    @PostMapping("/size")
    public Map<String, Object> analyzeBySize(@RequestBody BodySizeDTO dto, Authentication authentication) {
        
        String loginId = (authentication != null ? authentication.getName() : null);

        return service.sizeInsert(dto, loginId);
    }
}
