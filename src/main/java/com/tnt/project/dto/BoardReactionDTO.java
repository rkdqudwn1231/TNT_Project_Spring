package com.tnt.project.dto;

public class BoardReactionDTO {

    private int seq;              // PK
    private int board_seq;        // board.seq (FK)
    private String member_id;     // member.id (FK)
    private String reaction;      // 'LIKE' 또는 'DISLIKE'

    public BoardReactionDTO() {
    }

    public BoardReactionDTO(int seq, int board_seq, String member_id, String reaction) {
        this.seq = seq;
        this.board_seq = board_seq;
        this.member_id = member_id;
        this.reaction = reaction;
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

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }
}
