package com.tnt.project.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tnt.project.dto.EmailVerificationDTO;
import com.tnt.project.dto.MemberDTO;

@Repository
public class AuthDAO {

    @Autowired
    private SqlSession mybatis;

    /** 로그인용 - member 테이블 */
    public MemberDTO findByUserId(String id) {
        return mybatis.selectOne("Auth.findByUserId", id);
    }

    /** 이메일 인증용 - email_verification upsert */
    public void saveEmailVerification(String email, String token, Date expiresAt) {
        Map<String, Object> param = new HashMap<>();
        param.put("email", email);
        param.put("token", token);
        param.put("expires_at", expiresAt);

        mybatis.insert("Auth.upsertEmailVerification", param);
    }

    /** 토큰으로 email_verification 조회 */
    public EmailVerificationDTO findVerificationByToken(String token) {
        return mybatis.selectOne("Auth.findVerificationByToken", token);
    }

    /** 토큰 기준으로 verified = 'Y' */
    public int markVerified(String token) {
        return mybatis.update("Auth.markVerified", token);
    }

    /** 이메일이 인증 완료 상태인지 */
    public boolean isEmailVerified(String email) {
        Integer result = mybatis.selectOne("Auth.isEmailVerified", email);
        return result != null && result == 1;
    }
}
