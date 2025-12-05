package com.tnt.project.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tnt.project.dto.ClosetDTO;
import com.tnt.project.dto.ModelDTO;

@Repository
public class ModelDAO {

	@Autowired
	private SqlSessionTemplate mybatis;

	public Object modelinsert(ModelDTO dto) {

		return mybatis.insert("Model.insertModel",dto);
	}

	public int insertModel(ModelDTO modelDTO) {
		return mybatis.insert("Model.insertModel", modelDTO);
	}

	public List<ModelDTO> getModelList(String memberId) {

		return mybatis.selectList("Model.getModelList",memberId);
	}
	
	public List<ModelDTO> getModelPublicList() {
	
		return mybatis.selectList("Model.getModelPublicList");
	}

	public int deleteModel(int seq) {

		return mybatis.delete("Model.deleteModel",seq);
	}

	public int editModel(Map<String, Object> params) {
	
		return mybatis.update("Model.editModel",params);
	}







}
