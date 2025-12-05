package com.tnt.project.dto;

import java.sql.Timestamp;

public class BodySizeDTO {

	private int seq;
	private String member_id;
	private String gender;
	private int shoulder;
	private int bust;
	private int waist;
	private int hip;
	private int height;
	private String body_type_result;
	private Timestamp created_at;
	
	public BodySizeDTO() {
	}

	public BodySizeDTO(int seq, String member_id, String gender, int shoulder, int bust, int waist, int hip, int height,
			String body_type_result, Timestamp created_at) {
		super();
		this.seq = seq;
		this.member_id = member_id;
		this.gender = gender;
		this.shoulder = shoulder;
		this.bust = bust;
		this.waist = waist;
		this.hip = hip;
		this.height = height;
		this.body_type_result = body_type_result;
		this.created_at = created_at;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getShoulder() {
		return shoulder;
	}

	public void setShoulder(int shoulder) {
		this.shoulder = shoulder;
	}

	public int getBust() {
		return bust;
	}

	public void setBust(int bust) {
		this.bust = bust;
	}

	public int getWaist() {
		return waist;
	}

	public void setWaist(int waist) {
		this.waist = waist;
	}

	public int getHip() {
		return hip;
	}

	public void setHip(int hip) {
		this.hip = hip;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getBody_type_result() {
		return body_type_result;
	}

	public void setBody_type_result(String body_type_result) {
		this.body_type_result = body_type_result;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}
	
}
