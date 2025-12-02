package com.tnt.project.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MemberDTO {

    private String id;                      // 로그인 아이디
    private String password;                    // 비밀번호 해시
    private String nickname;                    // 닉네임
    private String name;                        // 실명

    private String gender;                      // male / female
    private LocalDate birth;                    // 생년월일
    private String phone;                       // 전화번호
    private String email;                       // 이메일

    private String profileImageUuid;            // GCP UUID 이름
    private String profileImageOriginal;        // 원본 파일명
    private String profileImageUrl;             // 공개 URL

    private String personalColor;               // spring_warm, summer_cool, autumn_warm, winter_cool
    private String bodyShape;                   // hourglass, triangle, inverted_triangle, rectangle, apple

    private LocalDateTime createdAt;            // 생성일
    private LocalDateTime updatedAt;            // 수정일

    // 기본 생성자
    public MemberDTO() {}

    // Getter / Setter
    public String getId() {
        return id;
    }
    public void setUserId(String id) {
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

    public String getProfileImageUuid() {
        return profileImageUuid;
    }
    public void setProfileImageUuid(String profileImageUuid) {
        this.profileImageUuid = profileImageUuid;
    }

    public String getProfileImageOriginal() {
        return profileImageOriginal;
    }
    public void setProfileImageOriginal(String profileImageOriginal) {
        this.profileImageOriginal = profileImageOriginal;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getPersonalColor() {
        return personalColor;
    }
    public void setPersonalColor(String personalColor) {
        this.personalColor = personalColor;
    }

    public String getBodyShape() {
        return bodyShape;
    }
    public void setBodyShape(String bodyShape) {
        this.bodyShape = bodyShape;
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
