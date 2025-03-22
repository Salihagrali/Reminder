package com.myprojects.reminder.service;

import com.myprojects.reminder.dtorequest.EmailRequest;
import com.myprojects.reminder.dtos.NoticeDto;
import com.myprojects.reminder.dtos.SenderDto;
import com.myprojects.reminder.exception.EmailNotFoundException;
import com.myprojects.reminder.model.Notice;
import com.myprojects.reminder.model.Sender;
import com.myprojects.reminder.repository.NoticeRepository;
import com.myprojects.reminder.repository.SenderRepository;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.myprojects.reminder.mapper.NoticeMapper.noticeDtoToNotice;
import static com.myprojects.reminder.mapper.NoticeMapper.noticeToNoticeDto;
import static com.myprojects.reminder.mapper.SenderMapper.senderDtotoSender;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String host;

    private final NoticeRepository noticeRepository;
    private final SenderRepository senderRepository;
    private final JavaMailSender mailSender;
    private final SchedulerService schedulerService;
    private final NoticeService noticeService;
    private final SenderService senderService;

    public EmailService(JavaMailSender mailSender, NoticeRepository noticeRepository,
                        SenderRepository senderRepository, SchedulerService schedulerService,
                        NoticeService noticeService, SenderService senderService) {
        this.mailSender = mailSender;
        this.noticeRepository = noticeRepository;
        this.senderRepository = senderRepository;
        this.schedulerService = schedulerService;
        this.noticeService = noticeService;
        this.senderService = senderService;
    }

    public void sendEmail(String to, String subject, String body) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(host);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void handleRequest(EmailRequest emailRequest) throws SchedulerException {
        Sender sender = senderRepository.findByEmail(emailRequest.getEmail()).orElseThrow(() -> new EmailNotFoundException("Sender not found"));

        if(sender == null) {
            sender = senderService.createSender(emailRequest.getEmail(), emailRequest.getPassword());
        }

        Notice notice = noticeService.createNotice(emailRequest.getTitle(),emailRequest.getContent(),sender);

        schedulerService.scheduleEmail(sender.getEmail(),notice.getTitle(),notice.getContent(), emailRequest.getDelay());
//        sendEmail(sender.getEmail(), notice.getTitle(), notice.getContent());
    }

    public List<NoticeDto> getAllMessages() {
        List<Notice> notices= noticeRepository.findAll();
        List<NoticeDto> noticeDtos = new ArrayList<>();
        for(Notice notice : notices){
            noticeDtos.add(noticeToNoticeDto(notice));
        }
        return noticeDtos;
    }
}
