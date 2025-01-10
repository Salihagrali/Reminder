package com.myprojects.reminder.controller;

import com.myprojects.reminder.dtorequest.EmailRequest;
import com.myprojects.reminder.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/sendEmail")
    public ResponseEntity<Response> sendEmail(@RequestBody @Valid EmailRequest emailRequest) {
        emailService.handleRequest(emailRequest);
        // not completed
        return ResponseEntity.ok(new Response("Email sent"));
    }
}
