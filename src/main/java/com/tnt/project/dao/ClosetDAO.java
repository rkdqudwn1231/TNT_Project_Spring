package com.tnt.project.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tnt.project.dto.ClosetDTO;

@Repository
public class ClosetDAO {

    @Autowired
    private SqlSessionTemplate mybatis;

    public int insertCloset(ClosetDTO closetDTO) {
    	return mybatis.insert("Closet.insertCloset", closetDTO);
    }

	public List<ClosetDTO> getClosetList() {
	
		return mybatis.selectList("Closet.getClosetList");
	}

	public int deleteCloth(Object seq) {
		
		return mybatis.delete("Closet.deleteCloth" , seq);
	}

	public int editCloth(Map<String, Object> param) {
	
		return mybatis.update("Closet.editCloth" , param);
	}
}
