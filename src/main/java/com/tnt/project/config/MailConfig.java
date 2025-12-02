package com.tnt.project.config;

import java.util.Properties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties; // ★ 추가!
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@EnableConfigurationProperties(MailConfig.class)   // ★ 추가!
@ConfigurationProperties(prefix = "mail")
public class MailConfig {

    private String username;
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Bean
    public JavaMailSender javaMailService() {

        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost("smtp.naver.com");
        sender.setPort(465);
        sender.setUsername(username);   // ← application.properties 값 적용됨!
        sender.setPassword(password);

        sender.setJavaMailProperties(getMailProperties());
        return sender;
    }

    private Properties getMailProperties() {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.debug", "true");
        props.setProperty("mail.smtp.ssl.trust", "smtp.naver.com");
        props.setProperty("mail.smtp.ssl.enable", "true");
        return props;
    }
}
