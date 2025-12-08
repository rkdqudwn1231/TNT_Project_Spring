package com.tnt.project.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MemberDTO {

    private String id;                      // 로그인 아이디
    private String password;                // 비밀번호 해시
    private String nickname;                // 닉네임
    private String name;                    // 실명

    private String gender;                  // male / female
    private LocalDate birth;                // 생년월일
    private String phone;                   // 전화번호
    private String email;                   // 이메일

    // ===== 프로필 이미지 =====
    private String image_uuid;              // UUID
    private String image_original;          // 원본 파일명
    private String image_url;               // 이미지 URL

    // ===== 퍼스널 컬러 / 체형 =====
    private String personal_color;          // spring_warm, summer_cool, autumn_warm, winter_cool ...
    private String body_shape;             // hourglass, triangle, inverted_triangle, rectangle, apple

    private LocalDateTime createdAt;        // 생성일
    private LocalDateTime updatedAt;        // 수정일

    // 기본 생성자
    public MemberDTO() {}

	public MemberDTO(String id, String password, String nickname, String name, String gender, LocalDate birth,
			String phone, String email, String image_uuid, String image_original, String image_url,
			String personal_color, String body_shape, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.password = password;
		this.nickname = nickname;
		this.name = name;
		this.gender = gender;
		this.birth = birth;
		this.phone = phone;
		this.email = email;
		this.image_uuid = image_uuid;
		this.image_original = image_original;
		this.image_url = image_url;
		this.personal_color = personal_color;
		this.body_shape = body_shape;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public LocalDate getBirth() {
		return birth;
	}

	public void setBirth(LocalDate birth) {
		this.birth = birth;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getPersonal_color() {
		return personal_color;
	}

	public void setPersonal_color(String personal_color) {
		this.personal_color = personal_color;
	}

	public String getBody_shape() {
		return body_shape;
	}

	public void setBody_shape(String body_shape) {
		this.body_shape = body_shape;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

    
    
}