package com.tnt.project.services;

import com.tnt.project.dao.BodySurveyDAO;
import com.tnt.project.dto.BodySurveyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class BodySurveyService {

    @Autowired
    private BodySurveyDAO dao;

    @Autowired
    private BodySurveryAlgorithm algorithm;

    public Map<String, Object> analyzeSurvey(BodySurveyDTO dto) {

        // 설문 기반 체형 계산
        String body_type = algorithm.evaluate(
                dto.getAnswer_q1(),
                dto.getAnswer_q2(),
                dto.getAnswer_q3(),
                dto.getAnswer_q4(),
                dto.getAnswer_q5()
        );

        dto.setBody_type(body_type);

        // DB 저장
        dao.insertSurvey(dto);

        // 체형 유형 상세 조회
        Map<String, String> param = new HashMap<>();
        param.put("body_type", body_type);
        param.put("gender", dto.getGender());

        Map<String, Object> res = dao.findBodyTypeInfo(param);

        if (res == null) {
            res = new HashMap<>();
        }
        res.put("body_type", body_type);

        return res;
    }
}
