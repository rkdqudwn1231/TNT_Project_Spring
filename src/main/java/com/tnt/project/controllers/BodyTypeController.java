package com.tnt.project.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tnt.project.services.BodyTypeService;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/body")
public class BodyTypeController {

    @Autowired
    private BodyTypeService bodyTypeService;

    @GetMapping("/result")
    public ResponseEntity<?> getBodyResult(@RequestParam("type") String body_type) {
        Map<String, Object> result = bodyTypeService.findBodyResult(body_type);
        if(result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO_DATA");
        }
        return ResponseEntity.ok(result);
    }
}
