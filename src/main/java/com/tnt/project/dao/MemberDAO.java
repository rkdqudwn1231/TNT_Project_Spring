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
    
    // 아이디 중복 여부 확인 
    public int checkId(String id) {
        return mybatis.selectOne("Member.checkId", id);
    }

    // 닉네임 중복 여부 확인 
    public int checkNickname(String nickname) {
        return mybatis.selectOne("Member.checkNickname", nickname);
    }

	public String findIdByEmail(String email) {
		return mybatis.selectOne("Member.findIdByEmail", email);
	}

	public int updatePassword(String email, String encryptedPw) {
	    Map<String, Object> params = new HashMap<>();
	    params.put("email", email);
	    params.put("password", encryptedPw);

	    return mybatis.update("Member.updatePassword", params);
	}
	

    
}
