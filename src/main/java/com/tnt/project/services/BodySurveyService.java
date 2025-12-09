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
    private BodySurveyDAO bodySurveyDAO;
       
    @Autowired
    private BodySurveryAlgorithm algorithm;

    public Map<String, Object> analyzeSurvey(BodySurveyDTO bodySurveyDTO ) {

        // 설문 기반 체형 계산
        String body_type = algorithm.evaluate(
        		bodySurveyDTO.getAnswer_q1(),
        		bodySurveyDTO.getAnswer_q2(),
        		bodySurveyDTO.getAnswer_q3(),
        		bodySurveyDTO.getAnswer_q4(),
        		bodySurveyDTO.getAnswer_q5(),
        		bodySurveyDTO.getAnswer_q6(),
        		bodySurveyDTO.getAnswer_q7()
        );

        bodySurveyDTO.setBody_type(body_type);

        // 로그인 여부 체쿠 후 DB 저장 ( 회원만 진단 결과 DB 저장 )
        if (bodySurveyDTO.getMember_id() != null) {
        
        	bodySurveyDAO.insertSurvey(bodySurveyDTO);
        }
        
        // 체형 유형 상세 조회
        Map<String, String> param = new HashMap<>();
        param.put("body_type", body_type);
        param.put("gender", bodySurveyDTO.getGender());

        Map<String, Object> res = bodySurveyDAO.findBodyTypeInfo(param);

        if (res == null) {
            res = new HashMap<>();
        }
        res.put("body_type", body_type);

        return res;
    }
}
