package com.tnt.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnt.project.dto.ModelDTO;
import com.tnt.project.services.ModelService;

@RestController
@RequestMapping("/model")
public class ModelController {
	
	@Autowired
	private ModelService modelService;
	
	@GetMapping("/list")
	public List<ModelDTO> getModelList() {
		
		ModelDTO dto = new ModelDTO();
				
		List<ModelDTO> list = modelService.getModelList();
		
		return list;
				
	}
}
