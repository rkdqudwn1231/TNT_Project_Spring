package com.tnt.project.dao;

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
}
