package com.tnt.project.dto;

import java.sql.Timestamp;

public class StyleRecommendDTO {

	private int seq;
	private String body_type;
	private String gender;
	private String cloth_type;
	private String category;
	private String name;
	private String image_url;
	private Timestamp created_at;
	
	
	public StyleRecommendDTO() {}

	public StyleRecommendDTO(int seq, String body_type, String gender, String cloth_type, String category, String name,
			String image_url, Timestamp created_at) {
		super();
		this.seq = seq;
		this.body_type = body_type;
		this.gender = gender;
		this.cloth_type = cloth_type;
		this.category = category;
		this.name = name;
		this.image_url = image_url;
		this.created_at = created_at;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getBody_type() {
		return body_type;
	}

	public void setBody_type(String body_type) {
		this.body_type = body_type;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCloth_type() {
		return cloth_type;
	}

	public void setCloth_type(String cloth_type) {
		this.cloth_type = cloth_type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}
		
}
