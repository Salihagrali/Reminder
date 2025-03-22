package com.myprojects.reminder.service;

import com.myprojects.reminder.dtorequest.EmailRequest;
import com.myprojects.reminder.dtos.NoticeDto;
import com.myprojects.reminder.exception.UserNotFoundException;
import com.myprojects.reminder.model.Notice;
import com.myprojects.reminder.model.UserEntity;
import com.myprojects.reminder.repository.NoticeRepository;
import com.myprojects.reminder.repository.UserRepository;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.myprojects.reminder.mapper.NoticeMapper.noticeToNoticeDto;

@Service
public class EmailService {

    private final UserRepository userRepository;
    private final UserService userService;
    @Value("${spring.mail.username}")
    private String host;

    private final NoticeRepository noticeRepository;
    private final JavaMailSender mailSender;
    private final SchedulerService schedulerService;
    private final NoticeService noticeService;

    public EmailService(JavaMailSender mailSender, NoticeRepository noticeRepository,
                        SchedulerService schedulerService, NoticeService noticeService,
                        UserRepository userRepository, UserService userService) {
        this.mailSender = mailSender;
        this.noticeRepository = noticeRepository;
        this.schedulerService = schedulerService;
        this.noticeService = noticeService;
        this.userRepository = userRepository;
        this.userService = userService;
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
        UserEntity user = userRepository.findByEmail(emailRequest.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found"));
        //Sender sender = senderRepository.findByEmail(emailRequest.getEmail()).orElseThrow();

        if(user == null) {
            user = userService.createUser(emailRequest.getEmail(), emailRequest.getPassword());
          //  sender = senderService.createSender(emailRequest.getEmail(), emailRequest.getPassword());
        }

        Notice notice = noticeService.createNotice(emailRequest.getTitle(),emailRequest.getContent(),user);

        schedulerService.scheduleEmail(user.getEmail(),notice.getTitle(),notice.getContent(), emailRequest.getDelay());
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
