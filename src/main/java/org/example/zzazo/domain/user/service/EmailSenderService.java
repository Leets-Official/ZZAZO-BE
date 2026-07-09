package org.example.zzazo.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

// 이메일 발송 담당 서비스
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender javaMailSender;

    @Value("${app.email-verification.subject}")
    private String verificationEmailSubject;

    // SMTP 발송은 시간이 걸리므로 별도 스레드에서 비동기로 처리한다.
    @Async("mailTaskExecutor")
    public void sendVerificationEmail(String email, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(verificationEmailSubject);
        message.setText("요청하신 인증번호는 [" + verificationCode + "] 입니다. 인증번호는 발급 시점으로부터 일정 시간 동안만 유효합니다.");

        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("[EmailSenderService] 인증 이메일 발송 실패 - email: {}", email, e);
        }
    }
}
