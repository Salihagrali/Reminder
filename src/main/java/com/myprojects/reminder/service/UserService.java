package com.myprojects.reminder.service;

import com.myprojects.reminder.constants.Roles;
import com.myprojects.reminder.model.UserEntity;
import com.myprojects.reminder.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService (UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(String email,String password){
        var user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(List.of(Roles.USER));
        userRepository.save(user);
    }

}
