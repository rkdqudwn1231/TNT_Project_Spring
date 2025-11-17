package com.tnt.project.dto;

import java.util.Date;

public class HistoryDTO {
    private int seq;
    private String memberId;
    private String resultUrl;
    private String name;
    private String upperImageUrl;
    private String upperName;
    private String lowerImageUrl;
    private String lowerName;
    private Date saveDate;
    private String taskId;
    
    public HistoryDTO() {}
    
    
	public HistoryDTO(int seq, String memberId, String resultUrl, String name, String upperImageUrl, String upperName,
			String lowerImageUrl, String lowerName, Date saveDate) {
		super();
		this.seq = seq;
		this.memberId = memberId;
		this.resultUrl = resultUrl;
		this.name = name;
		this.upperImageUrl = upperImageUrl;
		this.upperName = upperName;
		this.lowerImageUrl = lowerImageUrl;
		this.lowerName = lowerName;
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
	public String getResultUrl() {
		return resultUrl;
	}
	public void setResultUrl(String resultUrl) {
		this.resultUrl = resultUrl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Date getSaveDate() {
		return saveDate;
	}
	public void setSaveDate(Date saveDate) {
		this.saveDate = saveDate;
	}
	
//

	public void setTaskId(String taskId) {
	    this.taskId = taskId;
	}

	public String getTaskId() {
	    return taskId;
	}
}

