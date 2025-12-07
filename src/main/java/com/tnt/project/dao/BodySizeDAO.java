package com.tnt.project.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tnt.project.dto.BodySizeDTO;

@Repository
public class BodySizeDAO {

    @Autowired
    private SqlSession mybatis;

    // 치수 진단 결과 저장
    public int insertBodySize(BodySizeDTO dto) {
        return mybatis.insert("BodySize.insertBodySize", dto);
    }
}
