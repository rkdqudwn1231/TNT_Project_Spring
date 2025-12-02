package com.tnt.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnt.project.dao.BoardDAO;
import com.tnt.project.dto.BoardDTO;

@Service
public class BoardService {

    @Autowired
    private BoardDAO boardDAO;

    // 게시글 저장
    public int insert(BoardDTO dto) {
    	return  boardDAO.insert(dto);
    }
    // 게시글 조회
    public List<BoardDTO> findAll() {
        return boardDAO.findAll();
    }

    
}
