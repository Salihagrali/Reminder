package com.myprojects.reminder.controller;

import com.myprojects.reminder.domain.Response;
import com.myprojects.reminder.dtorequest.UserRequest;
import com.myprojects.reminder.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity<Response> register(@RequestBody @Valid UserRequest userRequest, HttpServletRequest request) {
        userService.createUser(userRequest.getEmail(), userRequest.getPassword());
        return ResponseEntity.ok(new Response(now().toString(),OK.value(), request.getRequestURI(),OK,"Account created."));
    }

    @GetMapping("/login")
    public ResponseEntity<Response> login(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletRequest request) {
        String result = userService.verify(email,password);
        return ResponseEntity.ok(new Response(now().toString(),OK.value(), request.getRequestURI(),OK,result));
    }
}
