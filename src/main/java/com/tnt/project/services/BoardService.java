package com.tnt.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tnt.project.dao.BoardDAO;
import com.tnt.project.dao.BoardReactionDAO;
import com.tnt.project.dto.BoardDTO;
import com.tnt.project.dto.BoardReactionDTO;

@Service
public class BoardService {

    @Autowired
    private BoardDAO boardDAO;

    @Autowired
    private BoardReactionDAO boardReactionDAO;

    
    // 게시글 저장
    public int insert(BoardDTO dto) {
    	return  boardDAO.insert(dto);
    }
    // 게시글 조회
    public List<BoardDTO> findAll() {
        return boardDAO.findAll();
    }

 // 게시글 상세 조회 + 조회수 증가
    // 컨트롤러: GET /board/detail/{seq} 에서 사용
    @Transactional
    public BoardDTO getDetail(int seq) {
    	System.out.println("여기까지옴");
        boardDAO.increaseReadCount(seq);
        return boardDAO.findBySeq(seq);
    }

    // 게시글 삭제
    // 컨트롤러: DELETE /board/delete/{seq}
    public int delete(int seq) {
        return boardDAO.delete(seq);
    }

    // 좋아요 상위 N개
    // 컨트롤러: GET /board/top10
    public List<BoardDTO> findTopByLikeCount(int limit) {
        return boardDAO.findTopByLikeCount(limit);
    }

    // 좋아요 토글 처리
    // 아무 반응 없음    → LIKE 추가, like_count +1
    // 이미 LIKE 상태    → 반응 삭제, like_count -1
    // DISLIKE 상태였다 → LIKE 로 변경, dislike_count -1, like_count +1
    @Transactional
    public void reactLike(int boardSeq, String memberId) {
        BoardReactionDTO existing = boardReactionDAO.findByBoardAndMember(boardSeq, memberId);

        if (existing == null) {
            // 첫 반응 → 좋아요 추가
            BoardReactionDTO dto = new BoardReactionDTO();
            dto.setBoard_seq(boardSeq);
            dto.setMember_id(memberId);
            dto.setReaction("LIKE");
            boardReactionDAO.insert(dto);

            boardDAO.increaseLike(boardSeq);

        } else if ("LIKE".equals(existing.getReaction())) {
            // 이미 좋아요 상태 → 좋아요 취소
            boardReactionDAO.delete(boardSeq, memberId);
            boardDAO.decreaseLike(boardSeq);

        } else if ("DISLIKE".equals(existing.getReaction())) {
            // 싫어요 상태 → 좋아요로 변경
            boardReactionDAO.updateReaction(boardSeq, memberId, "LIKE");
            boardDAO.decreaseDislike(boardSeq);
            boardDAO.increaseLike(boardSeq);
        }
    }

    // 싫어요 토글 처리
    // 아무 반응 없음    → DISLIKE 추가, dislike_count +1
    // 이미 DISLIKE 상태 → 반응 삭제, dislike_count -1
    // LIKE 상태였다    → DISLIKE 로 변경, like_count -1, dislike_count +1
    @Transactional
    public void reactDislike(int boardSeq, String memberId) {
        BoardReactionDTO existing = boardReactionDAO.findByBoardAndMember(boardSeq, memberId);

        if (existing == null) {
            // 첫 반응 → 싫어요 추가
            BoardReactionDTO dto = new BoardReactionDTO();
            dto.setBoard_seq(boardSeq);
            dto.setMember_id(memberId);
            dto.setReaction("DISLIKE");
            boardReactionDAO.insert(dto);

            boardDAO.increaseDislike(boardSeq);

        } else if ("DISLIKE".equals(existing.getReaction())) {
            // 이미 싫어요 상태 → 취소
            boardReactionDAO.delete(boardSeq, memberId);
            boardDAO.decreaseDislike(boardSeq);

        } else if ("LIKE".equals(existing.getReaction())) {
            // 좋아요 상태 → 싫어요로 변경
            boardReactionDAO.updateReaction(boardSeq, memberId, "DISLIKE");
            boardDAO.decreaseLike(boardSeq);
            boardDAO.increaseDislike(boardSeq);
        }
    }
    
    
}
