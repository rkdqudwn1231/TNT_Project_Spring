package com.tnt.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tnt.project.dao.HistoryDAO;
import com.tnt.project.dto.HistoryDTO;

@Service
public class HistoryService {

	@Autowired
	private HistoryDAO historydao;
	
    private final RestTemplate restTemplate = new RestTemplate(); // 추가
	
	public List<HistoryDTO> getHistoryList() {
		
		return historydao.getHistoryList();
	}
	

	public int deleteHistory(int seq) {
		
		return historydao.deleteHistory(seq);
	}

	public HistoryDTO getHistoryBySeq(int seq) {
		
		return historydao.getHistoryBySeq(seq);
	}

}
