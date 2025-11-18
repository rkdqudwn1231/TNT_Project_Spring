package com.tnt.project.dto;

public class PersonalcolorDTO {
	private int seq;
	private String season;

	private String tone_type;
	private String best_color;
	private String worst_color;


	public PersonalcolorDTO() {

	}

	public int getSeq() {
		return seq;
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

	public PersonalcolorDTO(int seq, String season, String tone_type, String best_color, String worst_color) {
		super();
		this.seq = seq;
		this.season = season;
		this.tone_type = tone_type;
		this.best_color = best_color;
		this.worst_color = worst_color;
	}



}
