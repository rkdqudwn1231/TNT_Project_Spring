package com.tnt.project.dto;

import java.util.Date;

public class BoardCommentDTO {

    private int seq;                    // PK
    private int board_seq;              // 게시글 번호 (board.seq)
    private String member_nickname;     // 작성자 닉네임 (member.nickname)
    private String content;             // 댓글 내용

    private Integer parent_seq;         // 부모 댓글 번호 (대댓글용, 없으면 null)
    private Integer depth;              // 댓글 깊이 (0: 일반, 1: 대댓글)

    private Date created_at;            // 작성일
    private Date updated_at;            // 수정일
    private String is_deleted;          // 'Y' / 'N'
    private String member_id;
    public BoardCommentDTO() {
    }

    public BoardCommentDTO(
            int seq,
            int board_seq,
            String member_nickname,
            String content,
            Integer parent_seq,
            Integer depth,
            Date created_at,
            Date updated_at,
            String is_deleted
    ) {
        this.seq = seq;
        this.board_seq = board_seq;
        this.member_nickname = member_nickname;
        this.content = content;
        this.parent_seq = parent_seq;
        this.depth = depth;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.is_deleted = is_deleted;
    }

    
    
    	
    public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public int getSeq() {
        return seq;
    }
    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getBoard_seq() {
        return board_seq;
    }
    public void setBoard_seq(int board_seq) {
        this.board_seq = board_seq;
    }

    public String getMember_nickname() {
        return member_nickname;
    }
    public void setMember_nickname(String member_nickname) {
        this.member_nickname = member_nickname;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public Integer getParent_seq() {
        return parent_seq;
    }
    public void setParent_seq(Integer parent_seq) {
        this.parent_seq = parent_seq;
    }

    public Integer getDepth() {
        return depth;
    }
    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Date getCreated_at() {
        return created_at;
    }
    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }
    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getIs_deleted() {
        return is_deleted;
    }
    public void setIs_deleted(String is_deleted) {
        this.is_deleted = is_deleted;
    }
}
