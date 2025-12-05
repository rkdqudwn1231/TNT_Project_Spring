package com.tnt.project.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tnt.project.dao.ModelDAO;
import com.tnt.project.dto.ModelDTO;

@Service
public class ModelService {

	@Autowired
	private ModelDAO modeldao;

	@Autowired
	private FileService Fserv;

	//추가
	public Object modelinsert(String memberId, String sex, MultipartFile modeImage) {

		ModelDTO dto = new ModelDTO();

		dto.setMemberId(memberId);
		dto.setSex(sex);


		try {
			// 이미지 업로드
			if (modeImage != null) {
				String modelUrlGCP = Fserv.upload(
						modeImage.getBytes(),
						"models/" + System.currentTimeMillis() + "_" + modeImage.getOriginalFilename(),
						modeImage.getContentType()
						);
				dto.setModelUrl(modelUrlGCP);
				dto.setModelName(modeImage.getOriginalFilename());
			}


		}catch(Exception e) {
			e.printStackTrace();
		}

		return modeldao.modelinsert(dto);
	}

	// 리스트
	public List<ModelDTO> getModelList(String memberId) {

		return modeldao.getModelList(memberId);
	}

	public List<ModelDTO> getModelPublicList() {

		return modeldao.getModelPublicList();
	}

	// 삭제
	public int deleteModel(int seq) {
		// 기본 가상모델(1,2) 삭제 금지
		if (seq == 1 || seq == 2) {
			return -1;   // 구분하기 위해 -1 반환
		}
		return modeldao.deleteModel(seq);
	}

	// 수정
	public int editModel(int seq, String name, String sex) {

		
	    // 기본 모델(1,2) 수정 금지
	    if (seq == 1 || seq == 2) {
	        return -1;
	    }
		
		Map<String, Object> params = new HashMap<>();
		params.put("seq", seq);
		params.put("name", name);
		params.put("sex", sex);

		return modeldao.editModel(params);
	}






}
