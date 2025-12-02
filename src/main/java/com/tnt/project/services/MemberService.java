package com.tnt.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnt.project.dao.MemberDAO;
import com.tnt.project.dto.MemberDTO;

@Service
public class MemberService {

	@Autowired
	private MemberDAO dao;
	
	public void signup(MemberDTO member) {
		dao.insertMember(member);
	}
	
}
