package com.myprojects.reminder.service;

import com.myprojects.reminder.constants.Roles;
import com.myprojects.reminder.model.UserEntity;
import com.myprojects.reminder.repository.UserRepository;
import com.myprojects.reminder.security.EmailAuthToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserService (UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public void createUser(String email,String password){
        var userEntity = userRepository.findByEmail(email);
        if(userEntity.isPresent()){
            //TO-DO ! Create exceptions.
            throw new DisabledException("User already exists");
        }
        var user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(List.of(Roles.USER));
        userRepository.save(user);
    }

    public String verify(String email, String password) {
        EmailAuthToken authToken = (EmailAuthToken) authenticationManager.authenticate(EmailAuthToken.unauthenticated(email,password));
        //return token from here
        if(authToken.isAuthenticated()){
            return "YES";
        }else return "NOOOOOOOOOOOOOOOOOOOOOOO";
    }
}
