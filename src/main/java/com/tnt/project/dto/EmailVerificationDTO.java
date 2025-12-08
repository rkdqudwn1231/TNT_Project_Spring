package com.tnt.project.dto;

import java.util.Date;

public class EmailVerificationDTO {

    private String email;
    private String token;
    private Date expires_at;
    private String verified; // 'Y' or 'N'

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpires_at() {
        return expires_at;
    }
    public void setExpires_at(Date expires_at) {
        this.expires_at = expires_at;
    }

    public String getVerified() {
        return verified;
    }
    public void setVerified(String verified) {
        this.verified = verified;
    }
}
