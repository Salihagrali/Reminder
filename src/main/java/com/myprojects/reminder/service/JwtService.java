package com.myprojects.reminder.service;

import com.myprojects.reminder.model.UserEntity;
import com.myprojects.reminder.security.userDetails.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    private final Supplier<SecretKey> secretKeySupplier = () -> Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

    private final Function<String,Claims> extractAllClaims = token ->
            Jwts.parser()
                    .verifyWith(secretKeySupplier.get())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

    private final Supplier<JwtBuilder> builder = () ->
            Jwts.builder()
                    .issuedAt(Date.from(Instant.now()))
                    .signWith(secretKeySupplier.get());

    public <T> T extractClaim(String token, Function<Claims, T> claim) {
//        Claims claims = extractAllClaims.apply(token);
//        return claim.apply(claims);
        return extractAllClaims.andThen(claim).apply(token);
    }

    public String extractSubject(String token) {
        //return extractClaim(token, (claim) -> claim.getSubject());
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(UserEntity userEntity) {
        return generateToken(new HashMap<>(),userEntity);
    }

    //extraClaims means something like "role", userDetails.getUser().getRoles(). We can add extra information to the token.
    public String generateToken(Map<String, Object> extraClaims, UserEntity userEntity) {
        return buildToken(extraClaims,userEntity,expiration);
    }

    private String buildToken(Map<String, Object> extraClaims, UserEntity userEntity, long expiration) {
        return builder.get()
                .claims(extraClaims)
                .subject(userEntity.getEmail())
                .expiration(Date.from(Instant.now().plusSeconds(expiration)))
                .compact();
    }

    public boolean isTokenValid(String token, CustomUserDetails userDetails) {
        final String userEmail = extractSubject(token);
        return (userEmail.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
