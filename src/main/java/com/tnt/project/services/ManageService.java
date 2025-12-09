package com.tnt.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnt.project.dao.ManageDAO;
import com.tnt.project.dto.SessionLogDTO;

@Service
public class ManageService {

	@Autowired
	private ManageDAO manageDAO;

	public void login(String id) {
		manageDAO.login(id);	
	}	


	// 로그인 시간 추가
	public void logout(SessionLogDTO sessionLogDTO) {
		manageDAO.logout(sessionLogDTO);
	}
}
