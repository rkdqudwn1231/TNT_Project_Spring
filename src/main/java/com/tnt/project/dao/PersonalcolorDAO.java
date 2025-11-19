package com.tnt.project.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tnt.project.dto.PersonalcolorDTO;

@Repository
public class PersonalcolorDAO {

	
	@Autowired
	private SqlSession mybatis;
	
	
	public int insert(PersonalcolorDTO dto) {
		System.out.println("왔어용");
		return mybatis.insert("color.insert",dto);
	}
}
