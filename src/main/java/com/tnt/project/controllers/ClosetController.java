package com.tnt.project.controllers;

import java.lang.reflect.Parameter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tnt.project.dto.ClosetDTO;
import com.tnt.project.services.ClosetService;



@RestController
@RequestMapping("/closet")
public class ClosetController {
	
	@Autowired
	private ClosetService closetService;
	
	@GetMapping("/list")
	public List<ClosetDTO> getClosetList() {
		
		ClosetDTO dto = new ClosetDTO();
				
		List<ClosetDTO> list = closetService.getClosetList();
		
		return list;
				
	}

	@DeleteMapping("/delete")
	public int deleteCloth(@RequestParam int seq) {
		
		int del = closetService.deleteCloth(seq);
		
		return del;
	}
	
}
