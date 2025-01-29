package com.myprojects.reminder.security;

import com.myprojects.reminder.model.UserEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class EmailAuthProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    //Account lockout mechanism
    private final ConcurrentHashMap<String,Integer> loginAttempts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> lockTimestamps = new ConcurrentHashMap<>();
    private final int MAX_ATTEMPTS = 5;
    private final long LOCK_TIME_DURATION = TimeUnit.MINUTES.toMillis(15);

    public EmailAuthProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(authentication.getPrincipal().toString());
        UserEntity user = userDetails.getUser();

        if (isAccountLocked(user.getEmail())) {
            throw new LockedException("Account is locked due to multiple failed attempts. Please try again after 15 minutes");
        }

        if(user == null || !passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())){
            recordFailedAttempt(user.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }

        resetFailedAttempts(user.getEmail());
        return EmailAuthToken.authenticated(user, user.getAuthorities());
    }

    private boolean isAccountLocked(String email){
        if(loginAttempts.containsKey(email) && loginAttempts.get(email) >= MAX_ATTEMPTS){
            long lockTime = lockTimestamps.getOrDefault(email, 0L);
            if(System.currentTimeMillis() - lockTime < LOCK_TIME_DURATION){
                return true;
            }else {
                resetFailedAttempts(email);
                return false;
            }
        }
        return false;
    }

    private void recordFailedAttempt(String email){
        loginAttempts.put(email, loginAttempts.getOrDefault(email, 0) + 1);
        if(loginAttempts.get(email) >= MAX_ATTEMPTS){
            lockTimestamps.put(email, System.currentTimeMillis());
        }
    }

    private void resetFailedAttempts(String email){
        loginAttempts.remove(email);
        lockTimestamps.remove(email);
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return EmailAuthToken.class.isAssignableFrom(authentication);
    }
}




















