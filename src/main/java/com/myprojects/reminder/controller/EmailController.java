package com.myprojects.reminder.controller;

import com.myprojects.reminder.domain.Response;
import com.myprojects.reminder.dtorequest.EmailRequest;
import com.myprojects.reminder.dtos.NoticeDto;
import com.myprojects.reminder.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.time.LocalTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/v1")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/sendEmail")
    public ResponseEntity<Response> sendEmail(@RequestBody @Valid EmailRequest emailRequest, HttpServletRequest request) {
        emailService.handleRequest(emailRequest);
        return ResponseEntity.ok(new Response(now().toString(),OK.value(), request.getRequestURI(),OK,"Email sent check it please"));
    }

    @GetMapping("/messages")
    public List<NoticeDto> messages() {
        return emailService.getAllMessages();
    }


}
