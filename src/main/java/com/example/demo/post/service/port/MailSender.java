package com.example.demo.post.service.port;

public interface MailSender {

    void send(String email, String title, String content);
}
