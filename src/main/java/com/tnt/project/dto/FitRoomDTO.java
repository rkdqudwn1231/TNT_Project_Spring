package com.tnt.project.dto;

import java.util.Date;


public class FitRoomDTO {
    private int seq;
    private String memberId;
    private String modelImageUrl;
    private String modelName;
    private String clothType;
    private String upperImageUrl;
    private String upperName;
    private String lowerImageUrl;
    private String lowerName;
    private String category;
    private String resultUrl;
    private String taskId;
  
    
    public FitRoomDTO() {}
    
    public FitRoomDTO(int seq, String memberId, String modelImageUrl, String modelName, String clothType,
			String upperImageUrl, String upperName, String lowerImageUrl, String lowerName, String category,
			String resultUrl, Date startDate) {
		super();
		this.seq = seq;	
		this.memberId = memberId;
		this.modelImageUrl = modelImageUrl;
		this.modelName = modelName;
		this.clothType = clothType;
		this.upperImageUrl = upperImageUrl;
		this.upperName = upperName;
		this.lowerImageUrl = lowerImageUrl;
		this.lowerName = lowerName;
		this.category = category;
		this.resultUrl = resultUrl;
		this.startDate = startDate;
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
	public String getModelImageUrl() {
		return modelImageUrl;
	}
	public void setModelImageUrl(String modelImageUrl) {
		this.modelImageUrl = modelImageUrl;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
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
	public String getResultUrl() {
		return resultUrl;
	}
	public void setResultUrl(String resultUrl) {
		this.resultUrl = resultUrl;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	private Date startDate;


//
	public void setTaskId(String taskId) {
	    this.taskId = taskId;
	}

	public String getTaskId() {
	    return taskId;
	}
}

