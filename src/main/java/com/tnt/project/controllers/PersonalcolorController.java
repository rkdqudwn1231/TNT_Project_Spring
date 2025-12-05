package com.tnt.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnt.project.dto.PersonalcolorDTO;
import com.tnt.project.services.PersonalcolorService;

@RestController
@RequestMapping("color")
public class PersonalcolorController {
	
	@Autowired
	private PersonalcolorService personalcolorservice;
	
	
	
	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody PersonalcolorDTO dto){
		System.out.println("controller 왔어용");
		int result=personalcolorservice.insert(dto);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/update")
	public ResponseEntity<Void> update(@RequestBody PersonalcolorDTO dto) {
	    System.out.println("controller 업데이트 요청 들어옴");
	    int result = personalcolorservice.update(dto);

	    if (result > 0) {
	        return ResponseEntity.ok().build();  // 성공
	    } else {
	        return ResponseEntity.badRequest().build(); // 실패 처리
	    }
	}
}
	
	
	
	
	

