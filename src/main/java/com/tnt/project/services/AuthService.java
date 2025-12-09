package com.tnt.project.services;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    // application.properties 에서 주입
    // tnt.app.base-url=http://10.10.55.97 이런 식으로 넣어둔 값
    @Value("${tnt.app.base-url}")
    private String baseUrl;

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

    /** 공통 토큰 생성 + DB 저장 후 토큰 반환 */
    private String createEmailVerificationToken(String email) {
        String token = UUID.randomUUID().toString();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 5);
        Date expiresAt = cal.getTime();

        authDAO.saveEmailVerification(email, token, expiresAt);
        return token;
    }

    /** 회원가입용 이메일 인증 링크 발송 (기존 메서드) */
    public void sendVerifyLink(String email) {
        String token = createEmailVerificationToken(email);

        // application.properties 에서 가져온 baseUrl 사용
        String verifyLink = baseUrl + "/auth/verify?token=" + token + "&type=signup";

        String subject = "[TNT] 이메일 인증 안내";

        String html = """
            <!DOCTYPE html>
            <html lang="ko">
            <head>
                <meta charset="UTF-8" />
                <title>TNT 이메일 인증</title>
            </head>
            <body style="margin:0;padding:0;background:#f9fafb;
                         font-family:-apple-system,BlinkMacSystemFont,
                         'Noto Sans KR',system-ui,sans-serif;">
                <table align="center" width="100%%" cellpadding="0" cellspacing="0"
                       style="max-width:600px;margin:0 auto;padding:24px;">
                    <tr>
                        <td style="padding:16px 0;text-align:center;">
                            <span style="display:inline-block;font-size:24px;
                                         font-weight:800;color:#ff6fa5;">
                                TNT
                            </span>
                        </td>
                    </tr>

                    <tr>
                        <td style="background:#ffffff;border-radius:16px;
                                   padding:32px 28px;
                                   box-shadow:0 8px 30px rgba(148,163,184,0.25);">

                            <p style="font-size:13px;font-weight:600;color:#fb7185;
                                      margin:0 0 8px 0;">
                                “ 한 장의 사진으로 나만의 컬러를 찾는 곳, TNT ”
                            </p>

                            <h1 style="font-size:20px;font-weight:700;color:#111827;
                                       margin:0 0 16px 0;">
                                회원가입 이메일 인증을 완료해주세요
                            </h1>

                            <p style="font-size:14px;line-height:1.7;color:#4b5563;
                                      margin:0 0 18px 0;">
                                안녕하세요.<br/>
                                TNT 회원가입을 진행해주셔서 감사합니다.<br/>
                                아래 버튼을 눌러 이메일을 인증해 주세요.<br/>
                                본 링크는 5분 후 만료됩니다.
                            </p>

                            <div style="text-align:center;margin:24px 0;">
                                <a href="%s"
                                   style="display:inline-block;padding:12px 28px;
                                          border-radius:999px;background:#ff6fa5;
                                          color:#ffffff;text-decoration:none;
                                          font-size:14px;font-weight:600;">
                                    이메일 인증하기
                                </a>
                            </div>

                            <hr style="border:none;border-top:1px solid #f3f4f6;
                                       margin:24px 0;"/>

                            <p style="font-size:11px;color:#9ca3af;margin:0;">
                                이 메일은 발신 전용입니다. 잘못 수신하셨다면 삭제해 주세요.
                            </p>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(verifyLink);

        mailService.sendMail(email, subject, html);
    }

    /** 아이디 찾기용 이메일 인증 링크 발송 */
    public void sendFindIdVerifyLink(String email) {
        String token = createEmailVerificationToken(email);
        String verifyLink = baseUrl + "/auth/verify?token=" + token + "&type=findId";

        String subject = "[TNT] 아이디 찾기 이메일 인증 안내";

        String html = """
            <!DOCTYPE html>
            <html lang="ko">
            <head>
                <meta charset="UTF-8" />
                <title>TNT 아이디 찾기 이메일 인증</title>
            </head>
            <body style="margin:0;padding:0;background:#f9fafb;
                         font-family:-apple-system,BlinkMacSystemFont,
                         'Noto Sans KR',system-ui,sans-serif;">
                <table align="center" width="100%%" cellpadding="0" cellspacing="0"
                       style="max-width:600px;margin:0 auto;padding:24px;">
                    <tr>
                        <td style="padding:16px 0;text-align:center;">
                            <span style="display:inline-block;font-size:24px;
                                         font-weight:800;color:#ff6fa5;">
                                TNT
                            </span>
                        </td>
                    </tr>

                    <tr>
                        <td style="background:#ffffff;border-radius:16px;
                                   padding:32px 28px;
                                   box-shadow:0 8px 30px rgba(148,163,184,0.25);">

                            <p style="font-size:13px;font-weight:600;color:#fb7185;
                                      margin:0 0 8px 0;">
                                “ TNT 아이디 찾기 인증 안내 ”
                            </p>

                            <h1 style="font-size:20px;font-weight:700;color:#111827;
                                       margin:0 0 16px 0;">
                                아이디 찾기를 위한 이메일 인증을 완료해주세요
                            </h1>

                            <p style="font-size:14px;line-height:1.7;color:#4b5563;
                                      margin:0 0 18px 0;">
                                아래 버튼을 눌러 이메일을 인증하신 뒤,<br/>
                                아이디 찾기 화면으로 돌아가 인증 완료 버튼을 눌러주세요.<br/>
                                본 링크는 5분 후 만료됩니다.
                            </p>

                            <div style="text-align:center;margin:24px 0;">
                                <a href="%s"
                                   style="display:inline-block;padding:12px 28px;
                                          border-radius:999px;background:#ff6fa5;
                                          color:#ffffff;text-decoration:none;
                                          font-size:14px;font-weight:600;">
                                    이메일 인증하기
                                </a>
                            </div>

                            <hr style="border:none;border-top:1px solid #f3f4f6;
                                       margin:24px 0;"/>

                            <p style="font-size:11px;color:#9ca3af;margin:0;">
                                이 메일은 발신 전용입니다. 잘못 수신하셨다면 삭제해 주세요.
                            </p>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(verifyLink);

        mailService.sendMail(email, subject, html);
    }

    /** 비밀번호 찾기용 이메일 인증 링크 발송 */
    public void sendResetPwVerifyLink(String email) {
        String token = createEmailVerificationToken(email);
        String verifyLink = baseUrl + "/auth/verify?token=" + token + "&type=resetPw";

        String subject = "[TNT] 비밀번호 재설정을 위한 이메일 인증 안내";

        String html = """
            <!DOCTYPE html>
            <html lang="ko">
            <head>
                <meta charset="UTF-8" />
                <title>TNT 비밀번호 재설정 이메일 인증</title>
            </head>
            <body style="margin:0;padding:0;background:#f9fafb;
                         font-family:-apple-system,BlinkMacSystemFont,
                         'Noto Sans KR',system-ui,sans-serif;">
                <table align="center" width="100%%" cellpadding="0" cellspacing="0"
                       style="max-width:600px;margin:0 auto;padding:24px;">
                    <tr>
                        <td style="padding:16px 0;text-align:center;">
                            <span style="display:inline-block;font-size:24px;
                                         font-weight:800;color:#ff6fa5;">
                                TNT
                            </span>
                        </td>
                    </tr>

                    <tr>
                        <td style="background:#ffffff;border-radius:16px;
                                   padding:32px 28px;
                                   box-shadow:0 8px 30px rgba(148,163,184,0.25);">

                            <p style="font-size:13px;font-weight:600;color:#fb7185;
                                      margin:0 0 8px 0;">
                                “ TNT 비밀번호 재설정 인증 안내 ”
                            </p>

                            <h1 style="font-size:20px;font-weight:700;color:#111827;
                                       margin:0 0 16px 0;">
                                비밀번호 재설정을 위한 이메일 인증을 완료해주세요
                            </h1>

                            <p style="font-size:14px;line-height:1.7;color:#4b5563;
                                      margin:0 0 18px 0;">
                                아래 버튼을 눌러 이메일을 인증하신 뒤,<br/>
                                비밀번호 재설정 화면으로 돌아가 새 비밀번호를 입력해 주세요.<br/>
                                본 링크는 5분 후 만료됩니다.
                            </p>

                            <div style="text-align:center;margin:24px 0;">
                                <a href="%s"
                                   style="display:inline-block;padding:12px 28px;
                                          border-radius:999px;background:#ff6fa5;
                                          color:#ffffff;text-decoration:none;
                                          font-size:14px;font-weight:600;">
                                    이메일 인증하기
                                </a>
                            </div>

                            <hr style="border:none;border-top:1px solid #f3f4f6;
                                       margin:24px 0;"/>

                            <p style="font-size:11px;color:#9ca3af;margin:0;">
                                이 메일은 발신 전용입니다. 잘못 수신하셨다면 삭제해 주세요.
                            </p>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(verifyLink);

        mailService.sendMail(email, subject, html);
    }

    /** 인증 링크 클릭 시 */
    public boolean verifyEmail(String token) {
        EmailVerificationDTO ev = authDAO.findVerificationByToken(token);

        if (ev == null) {
            return false;
        }

        Date now = new Date();
        if (ev.getExpires_at().before(now)) {
            return false;
        }

        if ("Y".equals(ev.getVerified())) {
            return true;
        }

        authDAO.markVerified(token);
        return true;
    }

    /** 이메일이 인증되었는지 확인 */
    public boolean isEmailVerified(String email) {
        return authDAO.isEmailVerified(email);
    }
}
