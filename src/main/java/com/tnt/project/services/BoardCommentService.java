package com.tnt.project.services;

import com.tnt.project.dao.BoardCommentDAO;
import com.tnt.project.dto.BoardCommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardCommentService {

    @Autowired
    private BoardCommentDAO boardCommentDAO;
    
    @Autowired
    private NotificationService notificationService;

    /**
     * 게시글별 댓글 목록 조회
     */
    public List<BoardCommentDTO> findByBoardSeq(int boardSeq) {
        return boardCommentDAO.findByBoardSeq(boardSeq);
    }

    /**
     * 댓글 등록
     */
    @Transactional
    public void insert(BoardCommentDTO dto) {
        // dto 안에 board_seq, member_id, member_nickname, content 세팅돼서 옴
        boardCommentDAO.insert(dto);

        // ★ 게시글 작성자에게 "새 댓글" 알림
        notificationService.notifyCommentOnBoard(
                dto.getBoard_seq(),        // 댓글이 달린 게시글 번호
                dto.getMember_id(),        // 댓글 단 사람 id
                dto.getMember_nickname()   // 댓글 단 사람 닉네임
        );
    }

    /**
     * 댓글 내용 수정
     * - seq + member_id 로 작성자 검증 (닉네임 변경돼도 상관없게)
     *
     * @return 수정된 행이 1개 이상이면 true, 아니면 false
     */
    @Transactional
    public boolean updateContent(int seq, String memberId, String content) {

        BoardCommentDTO dto = new BoardCommentDTO();
        dto.setSeq(seq);
        dto.setMember_id(memberId);
        dto.setContent(content);

        int affected = boardCommentDAO.updateContent(dto);
        return affected > 0;
    }

    /**
     * 댓글 삭제 (soft delete: is_deleted = 'Y')
     * - seq + member_id 로 작성자 확인
     *
     * @return 삭제된 행이 1개 이상이면 true, 아니면 false
     */
    @Transactional
    public boolean softDelete(int seq, String memberId) {

        BoardCommentDTO dto = new BoardCommentDTO();
        dto.setSeq(seq);
        dto.setMember_id(memberId);

        int affected = boardCommentDAO.softDelete(dto);
        return affected > 0;
    }

    /**
     * 대댓글 등록
     * 컨트롤러에서 dto 만들어서 넘겨줌
     *  - board_seq
     *  - parent_seq
     *  - member_id
     *  - member_nickname
     *  - content
     */
    @Transactional
    public void insertReply(int boardSeq, int parentSeq, String memberId, String nickname, String content) {

        BoardCommentDTO dto = new BoardCommentDTO();
        dto.setBoard_seq(boardSeq);
        dto.setParent_seq(parentSeq);
        dto.setMember_id(memberId);
        dto.setMember_nickname(nickname);
        dto.setContent(content);

        // DB에 대댓글 저장 (트리거/시퀀스로 seq 생성, MyBatis selectKey로 dto.seq 채워진다고 가정)
        boardCommentDAO.insertReply(dto);

        // ★ 부모 댓글 작성자에게 "대댓글" 알림
        //  - boardSeq       : 어느 게시글인지
        //  - parentSeq      : 부모 댓글 seq
        //  - dto.getSeq()   : 방금 달린 대댓글 seq
        //  - memberId       : 대댓글 단 사람 id
        //  - nickname       : 대댓글 단 사람 닉네임
        notificationService.notifyReplyOnComment(
                boardSeq,
                parentSeq,
                dto.getSeq(),
                memberId,
                nickname
        );
    }

}
