package com.tnt.project.dto;

public class BodyTypeDTO {

	private String body_type;
	private String gender;
	private String summary;
	private String top_tips;
	private String bottom_tips;
	private String pattern_tips;
	private String image_url;
	
	
	public BodyTypeDTO() {}

	public BodyTypeDTO(String body_type, String gender, String summary, String top_tips, String bottom_tips,
			String pattern_tips, String image_url) {
		super();
		this.body_type = body_type;
		this.gender = gender;
		this.summary = summary;
		this.top_tips = top_tips;
		this.bottom_tips = bottom_tips;
		this.pattern_tips = pattern_tips;
		this.image_url = image_url;
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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTop_tips() {
		return top_tips;
	}

	public void setTop_tips(String top_tips) {
		this.top_tips = top_tips;
	}

	public String getBottom_tips() {
		return bottom_tips;
	}

	public void setBottom_tips(String bottom_tips) {
		this.bottom_tips = bottom_tips;
	}

	public String getPattern_tips() {
		return pattern_tips;
	}

	public void setPattern_tips(String pattern_tips) {
		this.pattern_tips = pattern_tips;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	
}
