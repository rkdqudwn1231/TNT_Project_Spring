package com.tnt.project.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tnt.project.dto.ModelDTO;

@Repository
public class ModelDAO {

	   @Autowired
	    private SqlSessionTemplate mybatis;

	    public int insertModel(ModelDTO modelDTO) {
	    	return mybatis.insert("Model.insertModel", modelDTO);
	    }
	
	

}
