package com.tnt.project.dto;

import java.util.Date;

public class ClosetDTO {

	private int seq;
	private String memberId;
	private String clothType;
	private String upperImageUrl;
	private String upperName;
	private String lowerImageUrl;
	private String lowerName;
	private String category;
	private Date saveDate;

	
	
	public ClosetDTO() {}
	
	
	public ClosetDTO(int seq, String memberId, String clothType, String upperImageUrl, String upperName,
			String lowerImageUrl, String lowerName, String category, Date saveDate) {
		super();
		this.seq = seq;
		this.memberId = memberId;
		this.clothType = clothType;
		this.upperImageUrl = upperImageUrl;
		this.upperName = upperName;
		this.lowerImageUrl = lowerImageUrl;
		this.lowerName = lowerName;
		this.category = category;
		this.saveDate = saveDate;
	}
	
	

	
	
	public Date getSaveDate() {
		return saveDate;
	}


	public void setSaveDate(Date saveDate) {
		this.saveDate = saveDate;
	}


	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getClothType() {
		return clothType;
	}
	public void setClothType(String clothType) {
		this.clothType = clothType;
	}
	public String getUpperImageUrl() {
		return upperImageUrl;
	}
	public void setUpperImageUrl(String upperImageUrl) {
		this.upperImageUrl = upperImageUrl;
	}
	public String getUpperName() {
		return upperName;
	}
	public void setUpperName(String upperName) {
		this.upperName = upperName;
	}
	public String getLowerImageUrl() {
		return lowerImageUrl;
	}
	public void setLowerImageUrl(String lowerImageUrl) {
		this.lowerImageUrl = lowerImageUrl;
	}
	public String getLowerName() {
		return lowerName;
	}
	public void setLowerName(String lowerName) {
		this.lowerName = lowerName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}






}
