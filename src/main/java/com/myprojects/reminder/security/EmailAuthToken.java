package com.myprojects.reminder.security;


import com.myprojects.reminder.model.UserEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

public class EmailAuthToken extends AbstractAuthenticationToken {
    private static final String PROTECTED = "PROTECTED";
    private String email;
    private String password;
    private UserEntity user;
    private boolean authenticated;

    private EmailAuthToken(String email, String password) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.email = email;
        this.password = password;
        this.authenticated = false;
    }

    private EmailAuthToken(UserEntity user, Collection<? extends GrantedAuthority> authorities){
        super(authorities);
        this.user = user;
        this.email = PROTECTED;
        this.password = PROTECTED;
        this.authenticated = true;
    }

    public static EmailAuthToken unauthenticated(String email, String password) {
        return new EmailAuthToken(email, password);
    }

    public static EmailAuthToken authenticated(UserEntity user, Collection<? extends GrantedAuthority> authorities){
        return new EmailAuthToken(user, authorities);
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException("You can not do this!!");
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }

}
