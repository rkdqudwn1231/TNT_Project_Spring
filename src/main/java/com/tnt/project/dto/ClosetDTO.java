package com.tnt.project.dto;

import java.util.Date;

public class ClosetDTO {

	private int seq;
	private String memberId;
	private String clothType;
	private String upperImageUrl;
	private String upperName;
	private Integer upperColorR;
	private Integer upperColorG;
	private Integer upperColorB;
	private String lowerImageUrl;
	private String lowerName;
    private Integer lowerColorR;
    private Integer lowerColorG;
    private Integer lowerColorB;
    
    
	public ClosetDTO() {}
    
	public ClosetDTO(int seq, String memberId, String clothType, String upperImageUrl, String upperName,
			Integer upperColorR, Integer upperColorG, Integer upperColorB, String lowerImageUrl, String lowerName,
			Integer lowerColorR, Integer lowerColorG, Integer lowerColorB, String category, String lowerCategory,
			Date saveDate) {
		super();
		this.seq = seq;
		this.memberId = memberId;
		this.clothType = clothType;
		this.upperImageUrl = upperImageUrl;
		this.upperName = upperName;
		this.upperColorR = upperColorR;
		this.upperColorG = upperColorG;
		this.upperColorB = upperColorB;
		this.lowerImageUrl = lowerImageUrl;
		this.lowerName = lowerName;
		this.lowerColorR = lowerColorR;
		this.lowerColorG = lowerColorG;
		this.lowerColorB = lowerColorB;
		this.category = category;
		this.lowerCategory = lowerCategory;
		this.saveDate = saveDate;
	}
	private String category;
	private String lowerCategory;
	private Date saveDate;
	
	
	
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
	public Integer getUpperColorR() {
		return upperColorR;
	}
	public void setUpperColorR(Integer upperColorR) {
		this.upperColorR = upperColorR;
	}
	public Integer getUpperColorG() {
		return upperColorG;
	}
	public void setUpperColorG(Integer upperColorG) {
		this.upperColorG = upperColorG;
	}
	public Integer getUpperColorB() {
		return upperColorB;
	}
	public void setUpperColorB(Integer upperColorB) {
		this.upperColorB = upperColorB;
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
	public Integer getLowerColorR() {
		return lowerColorR;
	}
	public void setLowerColorR(Integer lowerColorR) {
		this.lowerColorR = lowerColorR;
	}
	public Integer getLowerColorG() {
		return lowerColorG;
	}
	public void setLowerColorG(Integer lowerColorG) {
		this.lowerColorG = lowerColorG;
	}
	public Integer getLowerColorB() {
		return lowerColorB;
	}
	public void setLowerColorB(Integer lowerColorB) {
		this.lowerColorB = lowerColorB;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getLowerCategory() {
		return lowerCategory;
	}
	public void setLowerCategory(String lowerCategory) {
		this.lowerCategory = lowerCategory;
	}
	public Date getSaveDate() {
		return saveDate;
	}
	public void setSaveDate(Date saveDate) {
		this.saveDate = saveDate;
	}
	
	
	
	
	
	
	
	
	



}
