package com.tnt.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tnt.project.dto.ClosetDTO;
import com.tnt.project.dto.ModelDTO;
import com.tnt.project.services.ModelService;

@RestController
@RequestMapping("/model")
public class ModelController {
	
	@Autowired
	private ModelService modelService;
	
	
	@PostMapping("/insert")
	public void modelinsert(@RequestParam("memberId") String memberId, @RequestParam("sex") String sex,
							@RequestParam(value = "modelUrl", required = false) MultipartFile modeImage) {
		
		
		modelService.modelinsert(memberId, sex , modeImage);
	}
	
	
	@GetMapping("/list")
	public List<ModelDTO> getModelList(@RequestParam String memberId) {
		
		ModelDTO dto = new ModelDTO();
				
		List<ModelDTO> list = modelService.getModelList(memberId);
		
		return list;
				
	}
	
	@GetMapping("/publicList")
	public List<ModelDTO> getModelPublicList() {
		
		ModelDTO dto = new ModelDTO();
				
		List<ModelDTO> list = modelService.getModelPublicList();
		
		return list;
				
	}
	
	
	@DeleteMapping("/delete")
	public int deleteModel(@RequestParam int seq) {
		
		
		
		int del = modelService.deleteModel(seq);
		
		
		return del;
				
	}
	
	
	@PutMapping("/edit")
	public int editModel(@RequestParam int seq , @RequestParam String name , @RequestParam String sex) {
		
		
		int edit = modelService.editModel(seq, name , sex);
		
		return edit;
		
	}
	
	
}
