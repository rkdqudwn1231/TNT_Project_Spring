package com.tnt.project.dto;

import java.sql.Timestamp;

public class SessionLogDTO {
	private int seq;
	private String id;
	private Timestamp start_time;
	private Timestamp end_time;
	private String logout_type;
	private Timestamp created_at;
	public SessionLogDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SessionLogDTO(int seq, String id, Timestamp start_time, Timestamp end_time, String logout_type,
			Timestamp created_at) {
		super();
		this.seq = seq;
		this.id = id;
		this.start_time = start_time;
		this.end_time = end_time;
		this.logout_type = logout_type;
		this.created_at = created_at;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Timestamp getStart_time() {
		return start_time;
	}
	public void setStart_time(Timestamp start_time) {
		this.start_time = start_time;
	}
	public Timestamp getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Timestamp end_time) {
		this.end_time = end_time;
	}
	public String getLogout_type() {
		return logout_type;
	}
	public void setLogout_type(String logout_type) {
		this.logout_type = logout_type;
	}
	public Timestamp getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}
	
	
}
