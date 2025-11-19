package com.tnt.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tnt.project.dto.HistoryDTO;
import com.tnt.project.services.HistoryService;



@RestController
@RequestMapping("/history")
public class HistoryController {
	
	@Autowired
	private HistoryService historyService;
	
	@GetMapping("/list")
	public List<HistoryDTO> getHistoryList() {
		
		HistoryDTO dto = new HistoryDTO();
				
		List<HistoryDTO> list = historyService.getHistoryList();
		
		return list;
				
	}
	
	@DeleteMapping("/delete")
	public int deleteHistory(@RequestParam int seq) {
		
		int del = historyService.deleteHistory(seq);
		
		return del;
	}
	
	
}
