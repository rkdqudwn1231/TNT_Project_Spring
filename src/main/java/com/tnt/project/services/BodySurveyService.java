package com.tnt.project.services;

import com.tnt.project.dao.AuthDAO;
import com.tnt.project.dao.BodySurveyDAO;
import com.tnt.project.dto.BodySurveyDTO;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class BodySurveyService {

    @Autowired
    private BodySurveyDAO surveryDao;
       
    @Autowired
    private BodySurveryAlgorithm algorithm;

    public Map<String, Object> analyzeSurvey(BodySurveyDTO dto ) {

        // 설문 기반 체형 계산 ㅎ
        String body_type = algorithm.evaluate(
                dto.getAnswer_q1(),
                dto.getAnswer_q2(),
                dto.getAnswer_q3(),
                dto.getAnswer_q4(),
                dto.getAnswer_q5(),
                dto.getAnswer_q6(),
                dto.getAnswer_q7()
        );

        dto.setBody_type(body_type);

        // 로그인 여부 체쿠 후 DB 저장 ( 회원만 진단 결과 DB 저장 )
        if (dto.getMember_id() != null) {
        
        	surveryDao.insertSurvey(dto);
        }
        
        // 체형 유형 상세 조회
        Map<String, String> param = new HashMap<>();
        param.put("body_type", body_type);
        param.put("gender", dto.getGender());

        Map<String, Object> res = surveryDao.findBodyTypeInfo(param);

        if (res == null) {
            res = new HashMap<>();
        }
        res.put("body_type", body_type);

        return res;
    }
}
