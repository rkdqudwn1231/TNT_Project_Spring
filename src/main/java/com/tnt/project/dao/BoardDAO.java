package com.tnt.project.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tnt.project.dto.BoardDTO;

@Repository
public class BoardDAO {

    @Autowired
    private SqlSession mybatis;

    public int insert(BoardDTO dto) {
    	 mybatis.insert("Board.insert", dto);
    	    return dto.getSeq(); 
    }

    // 게시글 목록 조회 추가
    public List<BoardDTO> findAll() {
        return mybatis.selectList("Board.findAll");
    }
	
    
}
