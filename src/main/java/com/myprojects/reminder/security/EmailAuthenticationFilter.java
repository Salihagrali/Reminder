package com.myprojects.reminder.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Slf4j
public class EmailAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    protected EmailAuthenticationFilter() {
        //Look at here one more time. Try to understand it !!!!
        super(new AntPathRequestMatcher("/v1/login","POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("Attempting to authenticate user");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null) {
            throw new AuthenticationException("Email or Password not provided") {};
        }

        // Validate input
        if (!isValidEmail(email) || !isValidPassword(password)) {
            throw new AuthenticationException("Invalid email or password format") {};
        }

        EmailAuthToken authRequest = EmailAuthToken.unauthenticated(email, password);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private boolean isValidEmail(String email) {
        // Add email validation logic (e.g., regex)
        return email.contains("@");
    }

    private boolean isValidPassword(String password) {
        // Add password validation logic (e.g., length, complexity)
        return password.length() >= 3;
    }
}
