package com.tnt.project.dto;

import java.sql.Timestamp;

public class BodySurveyDTO {

	private int seq;
	private String member_id;
	private String gender;
	private String answer_q1;
	private String answer_q2;
	private String answer_q3;
	private String answer_q4;
	private String answer_q5;
	private String answer_q6;
	private String answer_q7;
	private String body_type;
	private Timestamp created_at;
	
	public BodySurveyDTO() {}

	public BodySurveyDTO(int seq, String member_id, String gender, String answer_q1, String answer_q2, String answer_q3,
			String answer_q4, String answer_q5, String answer_q6, String answer_q7, String body_type,
			Timestamp created_at) {
		super();
		this.seq = seq;
		this.member_id = member_id;
		this.gender = gender;
		this.answer_q1 = answer_q1;
		this.answer_q2 = answer_q2;
		this.answer_q3 = answer_q3;
		this.answer_q4 = answer_q4;
		this.answer_q5 = answer_q5;
		this.answer_q6 = answer_q6;
		this.answer_q7 = answer_q7;
		this.body_type = body_type;
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

	public String getAnswer_q1() {
		return answer_q1;
	}

	public void setAnswer_q1(String answer_q1) {
		this.answer_q1 = answer_q1;
	}

	public String getAnswer_q2() {
		return answer_q2;
	}

	public void setAnswer_q2(String answer_q2) {
		this.answer_q2 = answer_q2;
	}

	public String getAnswer_q3() {
		return answer_q3;
	}

	public void setAnswer_q3(String answer_q3) {
		this.answer_q3 = answer_q3;
	}

	public String getAnswer_q4() {
		return answer_q4;
	}

	public void setAnswer_q4(String answer_q4) {
		this.answer_q4 = answer_q4;
	}

	public String getAnswer_q5() {
		return answer_q5;
	}

	public void setAnswer_q5(String answer_q5) {
		this.answer_q5 = answer_q5;
	}

	public String getAnswer_q6() {
		return answer_q6;
	}

	public void setAnswer_q6(String answer_q6) {
		this.answer_q6 = answer_q6;
	}

	public String getAnswer_q7() {
		return answer_q7;
	}

	public void setAnswer_q7(String answer_q7) {
		this.answer_q7 = answer_q7;
	}

	public String getBody_type() {
		return body_type;
	}

	public void setBody_type(String body_type) {
		this.body_type = body_type;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}

}
