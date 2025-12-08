package com.tnt.project.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tnt.project.dto.SessionLogDTO;

@Repository
public class ManageDAO {
	
	@Autowired
    private SqlSession mybatis;

    // 로그인 시간 추가
    public void login(String id) {
        mybatis.insert("manage.insert", id);
    }
    
 // 로그인 시간 추가
    public void logout(SessionLogDTO sessionLogDTO) {
    	
    	System.out.println("dao까지 온다");
        mybatis.update("manage.update", sessionLogDTO);
    }
}
