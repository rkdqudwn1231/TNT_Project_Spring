package com.tnt.project.dao;

import java.util.HashMap;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BodyTypeDAO {

    @Autowired
    private SqlSession mybatis;

    public Map<String, Object> findBodyTypeInfo(Map<String, String> param) {
        return mybatis.selectOne("BodyType.findBodyTypeInfo", param);
    }

    public Map<String, Object> findBodyResult(String body_type) {
        Map<String, Object> param = new HashMap<>();
        param.put("body_type", body_type);
        return mybatis.selectOne("BodyType.findBodyResult", param);
    }

}