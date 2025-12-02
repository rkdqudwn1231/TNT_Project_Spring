package com.tnt.project.dao;

import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.tnt.project.dto.BodySurveyDTO;

@Repository
public class BodySurveyDAO {

    @Autowired
    private SqlSession mybatis;

    public void insertSurvey(BodySurveyDTO dto) {
        mybatis.insert("BodySurvey.insertSurvey", dto);
    }

    public Map<String, Object> findBodyTypeInfo(Map<String, String> map) {
        return mybatis.selectOne("BodySurvey.findBodyTypeInfo", map);
    }
}
