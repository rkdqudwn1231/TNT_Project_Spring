package com.tnt.project.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tnt.project.dto.StyleRecommendDTO;

@Repository
public class StyleRecommendDAO {

    @Autowired
    private SqlSessionTemplate mybatis;

    public List<StyleRecommendDTO> getRecommendList(String body_type, String gender, String cloth_type) {
        Map<String, Object> param = Map.of(
            "body_type", body_type,
            "gender", gender,
            "cloth_type", cloth_type
        );
        return mybatis.selectList("StyleRecommend.getRecommendList", param);
    }
}
