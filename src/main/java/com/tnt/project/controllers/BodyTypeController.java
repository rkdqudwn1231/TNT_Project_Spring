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
@RequestMapping("/bodyType")
public class BodyTypeController {

    @Autowired
    private BodyTypeService bodyTypeService;

    @GetMapping("/result")
    public ResponseEntity<?> getBodyResult(@RequestParam String body_type ,
    									   @RequestParam String gender) {
        Map<String, Object> result = bodyTypeService.findBodyResult(body_type,gender);
        if(result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO_DATA");
        }
        System.out.println("테스트12:"+body_type);
        return ResponseEntity.ok(result);
    }
}
