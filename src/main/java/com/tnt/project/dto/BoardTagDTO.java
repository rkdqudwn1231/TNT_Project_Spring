package com.tnt.project.dto;

public class BoardTagDTO {

    private int seq;          // PK
    private int board_id;     // board.seq (FK)
    private String tag;       // 태그 문자열

    
    
    public BoardTagDTO() {}
    
    public BoardTagDTO(int seq, int board_id, String tag) {
		super();
		this.seq = seq;
		this.board_id = board_id;
		this.tag = tag;
	}
	public int getSeq() {
        return seq;
    }
    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getBoard_id() {
        return board_id;
    }
    public void setBoard_id(int board_id) {
        this.board_id = board_id;
    }

    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
}
