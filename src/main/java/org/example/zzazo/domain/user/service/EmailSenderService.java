package org.example.zzazo.domain.user.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


// 이메일 발송 담당 서비스
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.email-verification.subject}")
    private String verificationEmailSubject;

    @Value("${app.email-verification.from}")
    private String serviceEmail;

    // SMTP 발송은 시간이 걸리므로 별도 스레드에서 비동기로 처리한다.
    @Async("mailTaskExecutor")
    public void sendVerificationEmail(String email, String verificationCode) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
            helper.setFrom(serviceEmail);
            helper.setTo(email);
            helper.setSubject(verificationEmailSubject);

            String html = createVerificationHtml(verificationCode);

            helper.setText(html,true);


            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("[EmailSenderService] 인증 이메일 발송 실패 - email: {}", email, e);
        }
    }

    private String createVerificationHtml(String code) {

        Context context = new Context();
        context.setVariable("code", code);

        return templateEngine.process(
                "email/verification",
                context
        );
    }
}
