package com.tnt.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.username}")
    private String fromEmail;  // 네이버 메일 주소

    public void sendMail(String to, String subject, String htmlBody) {

        try {
            // 반드시 MimeMessage 사용
            MimeMessage message = mailSender.createMimeMessage();

            // true = multipart 여부, 여기서는 단순 본문만 쓰니까 false 로 둬도 되고
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            // 보내는 사람 이름 바꾸기: "TNT FitRoom"
            helper.setFrom(new InternetAddress(fromEmail, "TNT 커뮤니티 팀"));

            helper.setTo(to);
            helper.setSubject(subject);

            // ★ 두 번째 인자를 true 로 해야 HTML 모드
            helper.setText(htmlBody, true);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("메일 발송 실패", e);
        }
    }
}
