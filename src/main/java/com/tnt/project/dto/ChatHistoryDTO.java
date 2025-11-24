package com.tnt.project.dto;

import java.sql.Timestamp;

public class ChatHistoryDTO {

	private int seq;
	private String user_id; // 사용자 ID
    private String role;   // "user" 또는 "model"
    private String message; // 메시지 내용
    private Timestamp created_at; // 메시지 기록 시간
    
	public ChatHistoryDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ChatHistoryDTO(int seq, String user_id, String role, String message, Timestamp created_at) {
		super();
		this.seq = seq;
		this.user_id = user_id;
		this.role = role;
		this.message = message;
		this.created_at = created_at;
	}
	
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Timestamp getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}
	
    
		
}
