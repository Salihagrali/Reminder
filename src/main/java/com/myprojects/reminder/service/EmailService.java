package com.myprojects.reminder.service;

import com.myprojects.reminder.dtorequest.EmailRequest;
import com.myprojects.reminder.repository.NoticeRepository;
import com.myprojects.reminder.repository.SenderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.host}")
    private String host;

    private final NoticeRepository noticeRepository;
    private final SenderRepository senderRepository;
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender, NoticeRepository noticeRepository, SenderRepository senderRepository) {
        this.mailSender = mailSender;
        this.noticeRepository = noticeRepository;
        this.senderRepository = senderRepository;
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(host);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void handleRequest(EmailRequest emailRequest) {

        //First I need to do persist data into database
        sendEmail(emailRequest.getEmail(), emailRequest.getTitle(), emailRequest.getContent());
    }


}
