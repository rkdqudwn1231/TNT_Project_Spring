package com.tnt.project.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // 게시글 1건 상세 조회
    public BoardDTO findBySeq(int seq) {
        return mybatis.selectOne("Board.findBySeq", seq);
    }

    // 조회수 증가
    public int increaseReadCount(int seq) {
        return mybatis.update("Board.increaseReadCount", seq);
    }

    // 게시글 삭제
    public int delete(int seq) {
        return mybatis.delete("Board.delete", seq);
    }

    // 좋아요 +1
    public int increaseLike(int seq) {
        return mybatis.update("Board.increaseLike", seq);
    }

    // 좋아요 -1
    public int decreaseLike(int seq) {
        return mybatis.update("Board.decreaseLike", seq);
    }

    // 싫어요 +1
    public int increaseDislike(int seq) {
        return mybatis.update("Board.increaseDislike", seq);
    }

    // 싫어요 -1
    public int decreaseDislike(int seq) {
        return mybatis.update("Board.decreaseDislike", seq);
    }

    // 좋아요 순 Top N
    public List<BoardDTO> findTopByLikeCount(int limit) {
        Map<String, Object> param = new HashMap<>();
        param.put("limit", limit);
        return mybatis.selectList("Board.findTopByLikeCount", param);
    }

    
}
