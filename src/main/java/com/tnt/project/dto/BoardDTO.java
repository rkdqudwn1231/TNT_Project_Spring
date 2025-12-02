package com.tnt.project.dto;

import java.util.Date;

public class BoardDTO {

	
    private int seq;                    // PK
    private String id;                  // 작성자 ID

    private String title;               // 제목
    private String text;                // 내용
    private String color;               // 퍼스널 컬러
    private String body_shape;          // 체형  ← DB 동일
    private String tag;
    private String image_uuid;          // UUID
    private String image_original;      // 원본 파일명
    private String image_url;           // 이미지 URL

    private Date created_at;            // 작성시간

    
    public BoardDTO() {}
    

	public BoardDTO(int seq, String id, String title, String text, String color, String body_shape, String tag,
			String image_uuid, String image_original, String image_url, Date created_at) {
		super();
		this.seq = seq;
		this.id = id;
		this.title = title;
		this.text = text;
		this.color = color;
		this.body_shape = body_shape;
		this.tag = tag;
		this.image_uuid = image_uuid;
		this.image_original = image_original;
		this.image_url = image_url;
		this.created_at = created_at;
	}





	public int getSeq() {
		return seq;
	}


	public void setSeq(int seq) {
		this.seq = seq;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public String getColor() {
		return color;
	}


	public void setColor(String color) {
		this.color = color;
	}


	public String getBody_shape() {
		return body_shape;
	}


	public void setBody_shape(String body_shape) {
		this.body_shape = body_shape;
	}


	public String getTag() {
		return tag;
	}


	public void setTag(String tag) {
		this.tag = tag;
	}


	public String getImage_uuid() {
		return image_uuid;
	}


	public void setImage_uuid(String image_uuid) {
		this.image_uuid = image_uuid;
	}


	public String getImage_original() {
		return image_original;
	}


	public void setImage_original(String image_original) {
		this.image_original = image_original;
	}


	public String getImage_url() {
		return image_url;
	}


	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}


	public Date getCreated_at() {
		return created_at;
	}


	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
    
	
    
    
}
