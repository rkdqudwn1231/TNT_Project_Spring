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
        return boardDAO.insert(dto);
    }

    // 게시글 목록 조회
    public List<BoardDTO> findAll() {
        return boardDAO.findAll();
    }

    // 게시글 상세 조회 + 조회수 증가
    @Transactional
    public BoardDTO getDetail(int seq) {
        boardDAO.increaseReadCount(seq);
        return boardDAO.findBySeq(seq);
    }

    
 // 게시글 수정
    public int update(BoardDTO dto) {
        return boardDAO.update(dto);
    }

    
    // 게시글 삭제
    public int delete(int seq) {
        return boardDAO.delete(seq);
    }

    // 좋아요 상위 N개
    public List<BoardDTO> findTopByLikeCount(int limit) {
        return boardDAO.findTopByLikeCount(limit);
    }

    // ==========================
    //   좋아요 / 싫어요 토글
    // ==========================

    /**
     * 좋아요 토글
     *
     * 상태별 동작
     * 1) 기존 반응 없음(null)          → LIKE 추가, like_count +1
     * 2) 기존 반응 LIKE               → 반응 삭제(취소), like_count -1
     * 3) 기존 반응 DISLIKE            → LIKE 로 변경,
     *                                   dislike_count -1, like_count +1
     */
    @Transactional
    public void reactLike(int boardSeq, String memberId) {

        BoardReactionDTO existing =
                boardReactionDAO.findByBoardAndMember(boardSeq, memberId);

     

        if (existing == null) {
            // 1) 아무 반응 없음 → LIKE 추가
            BoardReactionDTO dto = new BoardReactionDTO();
            dto.setBoard_seq(boardSeq);
            dto.setMember_id(memberId);
            dto.setReaction("LIKE");
            boardReactionDAO.insert(dto);

            boardDAO.increaseLike(boardSeq);
      

        } else if ("LIKE".equals(existing.getReaction())) {
            // 2) 이미 LIKE → 취소
            boardReactionDAO.delete(boardSeq, memberId);
            boardDAO.decreaseLike(boardSeq);
           

        } else if ("DISLIKE".equals(existing.getReaction())) {
            // 3) DISLIKE → LIKE 로 변경
            boardReactionDAO.updateReaction(boardSeq, memberId, "LIKE");
            boardDAO.decreaseDislike(boardSeq);
            boardDAO.increaseLike(boardSeq);
          
        } else {
            // 혹시 다른 값이 들어있을 경우 방어 코드
            boardReactionDAO.updateReaction(boardSeq, memberId, "LIKE");
         
        }
    }

    /**
     * 싫어요 토글
     *
     * 상태별 동작
     * 1) 기존 반응 없음(null)          → DISLIKE 추가, dislike_count +1
     * 2) 기존 반응 DISLIKE            → 반응 삭제(취소), dislike_count -1
     * 3) 기존 반응 LIKE               → DISLIKE 로 변경,
     *                                   like_count -1, dislike_count +1
     */
    @Transactional
    public void reactDislike(int boardSeq, String memberId) {

        BoardReactionDTO existing =
                boardReactionDAO.findByBoardAndMember(boardSeq, memberId);

    

        if (existing == null) {
            // 1) 아무 반응 없음 → DISLIKE 추가
            BoardReactionDTO dto = new BoardReactionDTO();
            dto.setBoard_seq(boardSeq);
            dto.setMember_id(memberId);
            dto.setReaction("DISLIKE");
            boardReactionDAO.insert(dto);

            boardDAO.increaseDislike(boardSeq);
       

        } else if ("DISLIKE".equals(existing.getReaction())) {
            // 2) 이미 DISLIKE → 취소
            boardReactionDAO.delete(boardSeq, memberId);
            boardDAO.decreaseDislike(boardSeq);
           

        } else if ("LIKE".equals(existing.getReaction())) {
            // 3) LIKE → DISLIKE 로 변경
            boardReactionDAO.updateReaction(boardSeq, memberId, "DISLIKE");
            boardDAO.decreaseLike(boardSeq);
            boardDAO.increaseDislike(boardSeq);
            
        } else {
            // 방어 코드
            boardReactionDAO.updateReaction(boardSeq, memberId, "DISLIKE");
            
        }
    }
    
 // BoardService.java

    @Transactional(readOnly = true)
    public String getMyReaction(int boardSeq, String memberId) {
        BoardReactionDTO existing =
                boardReactionDAO.findByBoardAndMember(boardSeq, memberId);

        return (existing != null) ? existing.getReaction() : null; // "LIKE" / "DISLIKE" / null
    }

    
}
