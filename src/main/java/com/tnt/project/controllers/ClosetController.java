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
import com.tnt.project.services.ClosetService;



@RestController
@RequestMapping("/closet")
public class ClosetController {

	@Autowired
	private ClosetService closetService;


	@PostMapping("/insert")
	public void insertCloset(@RequestParam("memberId") String memberId, 
			@RequestParam("category") String category,
			@RequestParam("clothType") String clothType,
			@RequestParam(value = "cloth_image", required = false) MultipartFile cloth_image,
			@RequestParam(value = "lower_cloth_image", required = false) MultipartFile lower_cloth_image,
			 @RequestParam(value="upperClothColorR", required=false) Integer upperClothColorR,
		        @RequestParam(value="upperClothColorG", required=false) Integer upperClothColorG,
		        @RequestParam(value="upperClothColorB", required=false) Integer upperClothColorB,
		        @RequestParam(value="lowerClothColorR", required=false) Integer lowerClothColorR,
		        @RequestParam(value="lowerClothColorG", required=false) Integer lowerClothColorG,
		        @RequestParam(value="lowerClothColorB", required=false) Integer lowerClothColorB) {

		closetService.insertCloset(memberId,category,clothType,cloth_image,lower_cloth_image
				,upperClothColorR,upperClothColorG,upperClothColorB
	    		,lowerClothColorR,lowerClothColorG,lowerClothColorB);
	
	}

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
	
	@PutMapping("/edit")
	public int editCloth(@RequestParam Object seq , @RequestParam String name , @RequestParam String type,
						 @RequestParam String category  , @RequestParam String url) {
		
		
		int edit = closetService.editCloth(seq, name , type ,category, url);
		
		return edit;
	}

	
	
}
