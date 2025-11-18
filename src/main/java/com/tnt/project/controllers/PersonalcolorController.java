package com.tnt.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnt.project.dto.PersonalcolorDTO;
import com.tnt.project.services.PersonalcolorService;

@RestController
@RequestMapping("Personalcolor")
public class PersonalcolorController {
	
	@Autowired
	private PersonalcolorService personalcolorservice;
	
	
	
	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody PersonalcolorDTO dto){
		System.out.println("controller 왔어용");
		int result=personalcolorservice.insert(dto);
		return ResponseEntity.ok().build();
	}
}
