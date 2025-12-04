package com.tnt.project.dto;

public class PersonalcolorDTO {
	private int seq;
	private String season;
	private String tone_type;
	private String best_color;
	private String worst_color;
	private String member_id;



	public PersonalcolorDTO() {

	}

	public int getSeq() {
		return seq;
	}
	

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}


	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getSeason() {
		return season;
	}


	public void setSeason(String season) {
		this.season = season;
	}

	public String getTone_type() {
		return tone_type;
	}
	public void setTone_type(String tone_type) {
		this.tone_type = tone_type;
	}
	public String getBest_color() {
		return best_color;
	}
	public void setBest_color(String best_color) {
		this.best_color = best_color;
	}
	public String getWorst_color() {
		return worst_color;
	}
	public void setWorst_color(String worst_color) {
		this.worst_color = worst_color;
	}

	public PersonalcolorDTO(int seq, String season, String tone_type, String best_color, String worst_color,String member_id) {
		super();
		this.seq = seq;
		this.season = season;
		this.tone_type = tone_type;
		this.best_color = best_color;
		this.worst_color = worst_color;
		this.member_id=member_id;
	}



}
