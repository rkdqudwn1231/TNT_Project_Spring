package com.tnt.project.dto;

import java.util.Date;

public class ModelDTO {
    private int seq;
    private String memberId;
    private String modelUrl;
    private String modelName;
    private String taskId;
    private Date saveDate;
    
    public ModelDTO() {}
    
    
    public ModelDTO(int seq, String memberId, String modelUrl, String modelName, Date saveDate) {
		super();
		this.seq = seq;
		this.memberId = memberId;
		this.modelUrl = modelUrl;
		this.modelName = modelName;
		this.saveDate = saveDate;
	}
	
    
    
    
    
    
    
	public Date getSaveDate() {
		return saveDate;
	}


	public void setSaveDate(Date saveDate) {
		this.saveDate = saveDate;
	}


	public String getTaskId() {
		return taskId;
	}


	public void setTaskId(String taskId) {
		this.taskId = taskId;
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
	public String getModelUrl() {
		return modelUrl;
	}
	public void setModelUrl(String modelUrl) {
		this.modelUrl = modelUrl;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
}
