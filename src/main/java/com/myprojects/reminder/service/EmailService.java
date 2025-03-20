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

    public EmailService(JavaMailSender mailSender, NoticeRepository noticeRepository, SenderRepository senderRepository, SchedulerService schedulerService) {
        this.mailSender = mailSender;
        this.noticeRepository = noticeRepository;
        this.senderRepository = senderRepository;
        this.schedulerService = schedulerService;
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

        //DO it in SenderService
        if(sender == null) {
            SenderDto senderDto = new SenderDto(emailRequest.getEmail(), emailRequest.getPassword());
            sender = senderDtotoSender(senderDto);
            senderRepository.save(sender);
        }
        //Do it in NoticeService
        NoticeDto noticeDto = new NoticeDto(emailRequest.getTitle(), emailRequest.getContent());
        Notice notice = noticeDtoToNotice(noticeDto);
        notice.setAuthor(sender);

        noticeRepository.save(notice);
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
