package com.tnt.project.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnt.project.dao.BodySizeDAO;
import com.tnt.project.dao.BodySurveyDAO;
import com.tnt.project.dao.BodyTypeDAO;
import com.tnt.project.dto.BodySizeDTO;
import com.tnt.project.dto.BodySurveyDTO;

@Service
public class BodySizeService {

	  @Autowired
	    private BodySizeDAO bodySizeDAO;   // 치수 기록 저장 전용

	    @Autowired
	    private BodyTypeDAO bodyTypeDAO;   // 체형 상세 조회 전용

	    @Autowired
	    private BodySizeAlgorithm sizeAlgorithm;

	    public Map<String, Object> analyze(BodySizeDTO bodySizeDTO, String loginId) {

	        // 치수 기반 체형 계산
	        String body_type = sizeAlgorithm.evaluate(
	        		
	        		bodySizeDTO.getShoulder(),
	        		bodySizeDTO.getBust(),
	        		bodySizeDTO.getWaist(),
	        		bodySizeDTO.getHip()
	        );

	        bodySizeDTO.setBody_type_result(body_type);

	        // ② 로그인 사용자만 DB 저장
	        if (loginId != null) {
	        	bodySizeDTO.setMember_id(loginId);
	            bodySizeDAO.insertBodySize(bodySizeDTO);
	        }

	        // 체형 상세 정보 조회 (BodyTypeDAO)
	        Map<String, String> param = new HashMap<>();
	        param.put("body_type", body_type);
	        param.put("gender", bodySizeDTO.getGender());

	        Map<String, Object> res = bodyTypeDAO.findBodyTypeInfo(param);

	        if (res == null) res = new HashMap<>();
	        res.put("body_type", body_type);

	        return res;
	    }
	}