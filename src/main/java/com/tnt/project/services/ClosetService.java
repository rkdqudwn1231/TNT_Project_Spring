package com.tnt.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnt.project.dao.ClosetDAO;
import com.tnt.project.dto.ClosetDTO;
import com.tnt.project.dto.HistoryDTO;
@Service
public class ClosetService {

		@Autowired
		private ClosetDAO closetdao;
		
		public List<ClosetDTO> getClosetList() {
			
			return closetdao.getClosetList();
		

	}
}
