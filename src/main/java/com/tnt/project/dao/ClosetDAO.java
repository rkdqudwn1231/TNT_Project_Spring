package com.tnt.project.dao;

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
}
