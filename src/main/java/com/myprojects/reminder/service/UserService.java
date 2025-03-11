package com.myprojects.reminder.service;

import com.myprojects.reminder.constants.Roles;
import com.myprojects.reminder.dtorequest.UserRequest;
import com.myprojects.reminder.exception.UserAlreadyExistException;
import com.myprojects.reminder.exception.UserNotFoundException;
import com.myprojects.reminder.model.UserEntity;
import com.myprojects.reminder.repository.UserRepository;
import com.myprojects.reminder.security.EmailAuthToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserService (UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void createUser(String email,String password){
        var userEntity = userRepository.findByEmail(email);
        if(userEntity.isPresent()){
            throw new UserAlreadyExistException("User already exists");
        }
        var user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(List.of(Roles.USER));
        userRepository.save(user);
    }

    public String verify(UserRequest userRequest) {
        EmailAuthToken authToken = (EmailAuthToken) authenticationManager.authenticate(EmailAuthToken.unauthenticated(userRequest.getEmail(),userRequest.getPassword()));
        UserEntity user = userRepository.findByEmail(userRequest.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found"));

        if(authToken.isAuthenticated()){
            return jwtService.generateToken(user);
        }else {
            //Handle it later
            throw new BadCredentialsException("Invalid email or password");
        }
    }
}
