package com.tnt.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnt.project.dao.BoardTagDAO;
import com.tnt.project.dto.BoardTagDTO;

@Service
public class BoardTagService {

    @Autowired
    private BoardTagDAO boardTagDAO;


    public void insertTag(int boardId, String tag) {
        BoardTagDTO dto = new BoardTagDTO();
        dto.setBoard_id(boardId);
        dto.setTag(tag);

        boardTagDAO.insertTag(dto);
    }

    public List<BoardTagDTO> findByBoardId(int boardId) {
        return boardTagDAO.findByBoardId(boardId);
    }

    public void deleteByBoardId(int boardId) {
        boardTagDAO.deleteByBoardId(boardId);
    }
}
