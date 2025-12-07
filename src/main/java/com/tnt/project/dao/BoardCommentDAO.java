package com.tnt.project.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tnt.project.dto.BoardCommentDTO;

@Repository
public class BoardCommentDAO {

    @Autowired
    private SqlSession mybatis;

    // 게시글별 댓글 목록 조회 
    public List<BoardCommentDTO> findByBoardSeq(int boardSeq) {
        return mybatis.selectList("BoardComment.findByBoardSeq", boardSeq);
    }

    //댓글 등록 
    public int insert(BoardCommentDTO dto) {
        return mybatis.insert("BoardComment.insert", dto);
    }
    
    // 댓글 1건 조회 (부모 댓글 찾을 때 사용)
    public BoardCommentDTO findBySeq(int seq) {
        return mybatis.selectOne("BoardComment.findBySeq", seq);
    }
    
    // 댓글 수정 (작성자 닉네임 기준) 
    public int updateContent(BoardCommentDTO dto) {
        return mybatis.update("BoardComment.updateContent", dto);
    }

    // 댓글 삭제 (soft delete, is_deleted = 'Y') 
    public int softDelete(BoardCommentDTO dto) {
        return mybatis.update("BoardComment.softDelete", dto);
    }
    // 대댓글 insert
    public int insertReply(BoardCommentDTO dto) {
        return mybatis.insert("BoardComment.insertReply", dto);
    }

    
}
