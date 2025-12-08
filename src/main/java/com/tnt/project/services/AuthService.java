package com.tnt.project.services;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnt.project.dao.AuthDAO;
import com.tnt.project.dto.EmailVerificationDTO;
import com.tnt.project.dto.MemberDTO;
import com.tnt.project.utils.Encrypt;

@Service
public class AuthService {

    @Autowired
    private AuthDAO authDAO;

    @Autowired
    private MailService mailService;

    /** 로그인 */
    public MemberDTO login(String id, String rawPw) {

        MemberDTO member = authDAO.findByUserId(id);

        if (member == null) {
            throw new RuntimeException("아이디가 존재하지 않습니다.");
        }

        String encPw = Encrypt.encrypt(rawPw);
        if (!encPw.equals(member.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return member;
    }

    /** 이메일 인증 링크 발송 */
    public void sendVerifyLink(String email) {

        // 토큰 생성
        String token = UUID.randomUUID().toString();

        // 만료시간: 지금 + 5분
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 5);
        Date expiresAt = cal.getTime();

        // DB 저장 (email_verification 테이블)
        authDAO.saveEmailVerification(email, token, expiresAt);

        // 링크 생성
        String verifyLink = "http://10.10.55.97/auth/verify?token=" + token;

        String subject = "[TNT FitRoom] 이메일 인증 안내";
        String text =
                "아래 링크를 클릭하여 이메일 인증을 완료해주세요.\n\n" +
                verifyLink + "\n\n" +
                "본 링크는 5분 후 만료됩니다.\n" +
                "감사합니다.";

        mailService.sendMail(email, subject, text);
    }

    /** 인증 링크 클릭 시 */
    public boolean verifyEmail(String token) {

        EmailVerificationDTO ev = authDAO.findVerificationByToken(token);

        if (ev == null) {
            return false; // 잘못된 토큰
        }

        // 만료 체크
        Date now = new Date();
        if (ev.getExpires_at().before(now)) {
            return false; // 만료된 토큰
        }

        // 이미 'Y'면 그냥 true
        if ("Y".equals(ev.getVerified())) {
            return true;
        }

        authDAO.markVerified(token);
        return true;
    }

    /** 회원가입 시 이메일 인증 여부 확인 */
    public boolean isEmailVerified(String email) {
        return authDAO.isEmailVerified(email);
    }
}
