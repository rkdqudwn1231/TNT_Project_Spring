package com.tnt.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnt.project.dao.HistoryDAO;
import com.tnt.project.dto.HistoryDTO;

@Service
public class HistoryService {

	@Autowired
	private HistoryDAO historydao;
	
	public List<HistoryDTO> getHistoryList() {
		
		return historydao.getHistoryList();
	}

}
