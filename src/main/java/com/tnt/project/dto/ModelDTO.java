package com.tnt.project.dto;

import java.util.Date;

public class ModelDTO {
    private int seq;
    private String memberId;
    private String modelUrl;
    private String modelName;
    private String taskId;
    
    public ModelDTO() {}
    
    
    public ModelDTO(int seq, String memberId, String modelUrl, String modelName, Date createdAt) {
		super();
		this.seq = seq;
		this.memberId = memberId;
		this.modelUrl = modelUrl;
		this.modelName = modelName;
		this.createdAt = createdAt;
	}
	private Date createdAt;
    
    
    
    
    
    
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
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
