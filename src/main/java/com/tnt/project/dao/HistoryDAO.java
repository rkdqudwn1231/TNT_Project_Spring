package com.tnt.project.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tnt.project.dto.HistoryDTO;

@Repository
public class HistoryDAO {

	  @Autowired
	    private SqlSessionTemplate mybatis;

	    public int insertHistory(HistoryDTO historyDTO) {
	    	return mybatis.insert("History.insertHistory", historyDTO);
	    }

		public List<HistoryDTO> getHistoryList() {
			
			return mybatis.selectList("History.getHistoryList");
		}
	
}
