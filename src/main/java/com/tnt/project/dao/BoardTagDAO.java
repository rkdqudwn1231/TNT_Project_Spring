package com.tnt.project.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tnt.project.dto.BoardTagDTO;

@Repository
public class BoardTagDAO {

    @Autowired
    private SqlSession mybatis;

    public void insertTag(BoardTagDTO dto) {
        mybatis.insert("BoardTag.insertTag", dto);
    }

    public List<BoardTagDTO> findByBoardId(int boardId) {
        return mybatis.selectList("BoardTag.findByBoardId", boardId);
    }

    public void deleteByBoardId(int boardId) {
        mybatis.delete("BoardTag.deleteByBoardId", boardId);
    }
}
