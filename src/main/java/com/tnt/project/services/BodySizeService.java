package com.tnt.project.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnt.project.dao.BodySizeDAO;
import com.tnt.project.dto.BodySizeDTO;

@Service
public class BodySizeService {

    @Autowired
    private BodySizeDAO sizeDAO;

    @Autowired
    private BodySizeAlgorithm sizeAlgorithm;

    public Map<String, Object> sizeInsert(BodySizeDTO dto, String loginId) {

        // 1. 체형 분석
        String resultType = sizeAlgorithm.evaluate(
                dto.getShoulder(),
                dto.getBust(),
                dto.getWaist(),
                dto.getHip()
        );

        dto.setBody_type_result(resultType);

        // 2. 로그인 유저만 저장
        if (loginId != null) {
            dto.setMember_id(loginId);
            sizeDAO.insertBodySize(dto);
        }

        // 3. 프론트에는 "체형 한 글자만" 전달
        Map<String, Object> res = new HashMap<>();
        res.put("body_type", resultType);
        res.put("gender", dto.getGender());
        res.put("saved", loginId != null); // 선택: 저장 성공 여부

        return res;
    }
}

