package com.tnt.project.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tnt.project.dto.FitRoomDTO;

@Repository
public class FitRoomDAO {

	@Autowired
	private SqlSessionTemplate mybatis;
	
	   public int insertFitRoom(FitRoomDTO fitRoomDTO) {
		   return mybatis.insert("FitRoom.insertFitRoom", fitRoomDTO);
	    }
	
}
