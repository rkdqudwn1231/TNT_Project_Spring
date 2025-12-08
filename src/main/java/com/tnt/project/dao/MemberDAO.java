package com.tnt.project.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tnt.project.dto.MemberDTO;

@Repository
public class MemberDAO {

    @Autowired
    private SqlSession mybatis;

    public MemberDTO findByUserId(String userId) {
        return mybatis.selectOne("Member.findByUserId", userId);
    }
    
    // 회원 추가 
    public void insertMember(MemberDTO member) {
        mybatis.insert("Member.insertMember", member);
    }
    
    public int updateMyPage(MemberDTO dto) {
        return mybatis.update("Member.updateMyPage", dto);
    }
    
    // 체형 진단 결과 후 결과 저장하기 버튼 눌렀을 때 member에 body_shape에 업데이트
    public int updateBodyShape(String id, String body_shape) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("body_shape", body_shape);
        return mybatis.update("Member.updateBodyShape", param);
    }
}
