package com.example.demo.user.service;

import com.example.demo.post.service.port.MailSender;
import com.example.demo.user.controller.port.CertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificationServiceImpl implements CertificationService {

    private final MailSender mailSender;

    @Override
    public void send(String email, long userId, String certificationCode) {
        String title = "Please certify your email address";
        String certificationUrl = generateCertificationUrl(userId, certificationCode);
        String content = "Please click the following link to certify your email address: " + certificationUrl;

        mailSender.send(email, title, content);
    }

    private String generateCertificationUrl(long userId, String certificationCode) {
        return "http://localhost:8080/api/users/" + userId + "/verify?certificationCode=" + certificationCode;
    }
}
