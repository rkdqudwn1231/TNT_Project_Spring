package com.tnt.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnt.project.dao.PersonalcolorDAO;
import com.tnt.project.dto.PersonalcolorDTO;

@Service
public class PersonalcolorService {

	
	@Autowired
	private PersonalcolorDAO dao;
	
	public int insert(PersonalcolorDTO dto) {
		System.out.println("service 왔어여");
		return dao.insert(dto);
	}
}
