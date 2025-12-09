package com.tnt.project.dto;

import java.time.LocalDateTime;

public class NotificationDTO {

    private Long seq;             // PK

    private String member_id;     // 알림 받는 회원 ID (member.id)
    private String type;          // "LIKE", "COMMENT", "REPLY" 등

    private Long board_seq;       // 관련 게시글 seq (board.seq) 
    private Long comment_seq;     // 관련 댓글 seq (board_comment.seq, 없을 수도 있음)

    private String message;       // 사람이 읽을 알림 메시지

    private String is_read;       // 'Y' / 'N'

    private LocalDateTime created_at; // 생성 시각

    public NotificationDTO() {}

    public NotificationDTO(Long seq,
                           String member_id,
                           String type,
                           Long board_seq,
                           Long comment_seq,
                           String message,
                           String is_read,
                           LocalDateTime created_at) {
        this.seq = seq;
        this.member_id = member_id;
        this.type = type;
        this.board_seq = board_seq;
        this.comment_seq = comment_seq;
        this.message = message;
        this.is_read = is_read;
        this.created_at = created_at;
    }

    public Long getSeq() {
        return seq;
    }
    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public String getMember_id() {
        return member_id;
    }
    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public Long getBoard_seq() {
        return board_seq;
    }
    public void setBoard_seq(Long board_seq) {
        this.board_seq = board_seq;
    }

    public Long getComment_seq() {
        return comment_seq;
    }
    public void setComment_seq(Long comment_seq) {
        this.comment_seq = comment_seq;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getIs_read() {
        return is_read;
    }
    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }
    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
