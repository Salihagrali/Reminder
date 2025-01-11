package com.myprojects.reminder.service;

import com.myprojects.reminder.dtorequest.EmailRequest;
import com.myprojects.reminder.dtos.NoticeDto;
import com.myprojects.reminder.dtos.SenderDto;
import com.myprojects.reminder.model.Notice;
import com.myprojects.reminder.model.Sender;
import com.myprojects.reminder.repository.NoticeRepository;
import com.myprojects.reminder.repository.SenderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.myprojects.reminder.mapper.NoticeMapper.noticeDtoToNotice;
import static com.myprojects.reminder.mapper.SenderMapper.senderDtotoSender;

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
        SenderDto senderDto = new SenderDto(emailRequest.getEmail(), emailRequest.getPassword());
        Sender sender = senderDtotoSender(senderDto);

        NoticeDto noticeDto = new NoticeDto(emailRequest.getTitle(), emailRequest.getContent());
        Notice notice = noticeDtoToNotice(noticeDto);
        notice.setAuthor(sender);

        noticeRepository.save(notice);
        senderRepository.save(sender);

        sendEmail(sender.getEmail(), notice.getTitle(), notice.getContent());
    }


}
