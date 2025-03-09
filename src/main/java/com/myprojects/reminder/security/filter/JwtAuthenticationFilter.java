package com.myprojects.reminder.security.filter;

import com.myprojects.reminder.security.EmailAuthToken;
import com.myprojects.reminder.security.userDetails.CustomUserDetails;
import com.myprojects.reminder.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(final JwtService jwtService, final UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try{
            final String token = authHeader.substring(7);
            final String userEmail = jwtService.extractSubject(token);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if(userEmail != null && auth == null) {
                var user = (CustomUserDetails) userDetailsService.loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(token, user)) {
                    EmailAuthToken authToken = EmailAuthToken.authenticated(user.getUser(), user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            //throw new RuntimeException(e);
        }
    }
}
