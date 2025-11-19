package com.tnt.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnt.project.dao.ModelDAO;
import com.tnt.project.dto.ModelDTO;

@Service
public class ModelService {

	@Autowired
	private ModelDAO modeldao;
	
	public List<ModelDTO> getModelList() {
		
		return modeldao.getModelList();
	}

	
	// 삭제
	public int deleteModel(int seq) {
		
		return modeldao.deleteModel(seq);
	}

}
