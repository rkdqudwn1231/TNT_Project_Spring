package com.tnt.project.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tnt.project.dto.HistoryDTO;

@Repository
public class HistoryDAO {

	  @Autowired
	    private SqlSessionTemplate mybatis;

	    public void insertHistory(HistoryDTO historyDTO) {
	        mybatis.insert("History.insertHistory", historyDTO);
	    }
	
}
