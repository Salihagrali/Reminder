package com.myprojects.reminder.controller;

import com.myprojects.reminder.domain.Response;
import com.myprojects.reminder.dtorequest.EmailRequest;
import com.myprojects.reminder.dtorequest.UserRequest;
import com.myprojects.reminder.dtos.NoticeDto;
import com.myprojects.reminder.service.EmailService;
import com.myprojects.reminder.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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

    public EmailController(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    @PostMapping("/sendEmail")
    public ResponseEntity<Response> sendEmail(@RequestBody @Valid EmailRequest emailRequest, HttpServletRequest request) {
        emailService.handleRequest(emailRequest);
        return ResponseEntity.ok(new Response(now().toString(),OK.value(), request.getRequestURI(),OK,"Email sent check it please"));
    }

    @PostMapping("/messages")
    public List<NoticeDto> messages(@RequestParam("email") String email, @RequestParam("password") String password) {
        log.info(email,password);
        return emailService.getAllMessages();
    }

}
