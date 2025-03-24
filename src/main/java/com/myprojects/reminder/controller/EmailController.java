package com.myprojects.reminder.controller;

import com.myprojects.reminder.domain.Response;
import com.myprojects.reminder.dtorequest.EmailRequest;
import com.myprojects.reminder.dtos.NoticeDto;
import com.myprojects.reminder.service.EmailService;
import com.myprojects.reminder.service.JwtService;
import com.myprojects.reminder.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.time.LocalTime.now;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("/v1")
public class EmailController {

    private final EmailService emailService;
    private final UserService userService;
    private final JwtService jwtService;

    public EmailController(EmailService emailService, UserService userService, JwtService jwtService) {
        this.emailService = emailService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/scheduleEmail")
    public ResponseEntity<Response> scheduleEmail(@RequestBody @Valid EmailRequest emailRequest, HttpServletRequest request) throws SchedulerException {
        String userEmail = jwtService.extractSubject(request.getHeader("Authorization").substring(7));
        emailService.handleRequest(emailRequest,userEmail);
        return ResponseEntity.ok(new Response(now().toString(),OK.value(), request.getRequestURI(),OK,"Email will be sent.",null));
    }
    //Make it accessible only for admin
    @GetMapping("/messages")
    public List<NoticeDto> messages() {
        return emailService.getAllMessages();
    }
}
