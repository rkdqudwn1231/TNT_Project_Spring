package com.tnt.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnt.project.dao.NotificationDAO;
import com.tnt.project.dao.BoardDAO;
import com.tnt.project.dao.BoardCommentDAO;
import com.tnt.project.dto.NotificationDTO;
import com.tnt.project.dto.BoardDTO;
import com.tnt.project.dto.BoardCommentDTO;

@Service
public class NotificationService {

    @Autowired
    private NotificationDAO notificationDAO;

    @Autowired
    private BoardDAO boardDAO;

    @Autowired
    private BoardCommentDAO boardCommentDAO;

    /**
     * 내가 받은 알림 목록
     */
    public List<NotificationDTO> findByMemberId(String memberId) {
        return notificationDAO.findByMemberId(memberId);
    }

    /**
     * 알림 1개 읽음 처리
     */
    public void markAsRead(long seq) {
        notificationDAO.markAsRead(seq);
    }

    /**
     * 특정 유저 알림 전체 읽음 처리
     */
    public void markAllAsRead(String memberId) {
        notificationDAO.markAllAsRead(memberId);
    }

    /**
     * 공통 알림 생성
     * (필요하면 다른 서비스에서 직접 호출해도 됨)
     */
    public void createNotification(NotificationDTO dto) {
        notificationDAO.insert(dto);
    }

    // =============================
    //  아래부터는 “이벤트별” 편의 메서드
    // =============================

    /**
     * 게시글 좋아요 알림
     * - board_seq: 좋아요가 눌린 게시글 번호
     * - fromMemberId: 좋아요 누른 사람 id
     * - fromNickname: 좋아요 누른 사람 닉네임 (메시지용)
     */
    public void notifyLikeOnBoard(long boardSeq, String fromMemberId, String fromNickname) {

        BoardDTO board = boardDAO.findBySeq((int) boardSeq);
        if (board == null) {
            return;
        }

        String ownerId = board.getId(); // 게시글 작성자 ID

        // 본인이 자기 글에 좋아요 누른 경우 알림 안보냄
        if (ownerId != null && ownerId.equals(fromMemberId)) {
            return;
        }

        NotificationDTO noti = new NotificationDTO();
        noti.setMember_id(ownerId);        // 알림 받을 사람
        noti.setType("LIKE");              // 알림 타입
        noti.setBoard_seq(boardSeq);       // 관련 게시글
        noti.setComment_seq(null);         // 댓글 없음
        noti.setMessage(fromNickname + "님이 회원님의 게시글에 좋아요를 눌렀습니다.");

        notificationDAO.insert(noti);
    }

    /**
     * 게시글에 “새 댓글” 알림
     * - boardSeq: 댓글이 달린 게시글 번호
     * - fromMemberId: 댓글 단 사람 id
     * - fromNickname: 댓글 단 사람 닉네임
     */
    public void notifyCommentOnBoard(long boardSeq, String fromMemberId, String fromNickname) {

        BoardDTO board = boardDAO.findBySeq((int) boardSeq);
        if (board == null) {
            return;
        }

        String ownerId = board.getId(); // 게시글 작성자 ID

        // 내 게시글에 내가 댓글 달면 알림 안보냄
        if (ownerId != null && ownerId.equals(fromMemberId)) {
            return;
        }

        NotificationDTO noti = new NotificationDTO();
        noti.setMember_id(ownerId);
        noti.setType("COMMENT");
        noti.setBoard_seq(boardSeq);
        noti.setComment_seq(null); // 필요하면 실제 comment seq 넣어도 됨
        noti.setMessage(fromNickname + "님이 회원님의 게시글에 댓글을 남겼습니다.");

        notificationDAO.insert(noti);
    }

    /**
     * 내가 쓴 댓글에 “대댓글”이 달렸을 때 알림
     * - replyCommentSeq: 새로 달린 대댓글 seq
     * - parentCommentSeq: 내가 쓴 부모 댓글 seq
     * - fromMemberId: 대댓글 단 사람 id
     * - fromNickname: 대댓글 단 사람 닉네임
     */
    public void notifyReplyOnComment(long boardSeq,
                                     long parentCommentSeq,
                                     long replyCommentSeq,
                                     String fromMemberId,
                                     String fromNickname) {

        // 부모 댓글 정보 조회
        BoardCommentDTO parent = boardCommentDAO.findBySeq((int) parentCommentSeq);
        if (parent == null) {
            return;
        }

        String parentOwnerId = parent.getMember_id(); // 부모 댓글 작성자 id

        // 내가 쓴 댓글에 내가 또 대댓글 단 경우는 알림 안보냄
        if (parentOwnerId != null && parentOwnerId.equals(fromMemberId)) {
            return;
        }

        NotificationDTO noti = new NotificationDTO();
        noti.setMember_id(parentOwnerId);  // 부모 댓글 작성자에게 알림
        noti.setType("REPLY");
        noti.setBoard_seq(boardSeq);
        noti.setComment_seq(replyCommentSeq); // 새로 달린 대댓글 번호
        noti.setMessage(fromNickname + "님이 회원님의 댓글에 답글을 남겼습니다.");

        notificationDAO.insert(noti);
    }
}
