package com.tnt.project.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tnt.project.dto.BoardReactionDTO;

@Repository
public class BoardReactionDAO {

    @Autowired
    private SqlSession mybatis;

    // 특정 게시글 + 특정 회원의 반응 한 건 조회
    public BoardReactionDTO findByBoardAndMember(int board_seq, String member_id) {
        Map<String, Object> param = new HashMap<>();
        param.put("board_seq", board_seq);
        param.put("member_id", member_id);
        return mybatis.selectOne("BoardReaction.findByBoardAndMember", param);
    }

    // 새로운 반응 INSERT (LIKE 또는 DISLIKE)
    public int insert(BoardReactionDTO dto) {
        return mybatis.insert("BoardReaction.insert", dto);
    }

    // 반응 타입 변경 (LIKE ↔ DISLIKE)
    public int updateReaction(int board_seq, String member_id, String reaction) {
        Map<String, Object> param = new HashMap<>();
        param.put("board_seq", board_seq);
        param.put("member_id", member_id);
        param.put("reaction", reaction);
        return mybatis.update("BoardReaction.updateReaction", param);
    }

    // 반응 삭제 (좋아요/싫어요 취소)
    public int delete(int board_seq, String member_id) {
        Map<String, Object> param = new HashMap<>();
        param.put("board_seq", board_seq);
        param.put("member_id", member_id);
        return mybatis.delete("BoardReaction.delete", param);
    }
}
