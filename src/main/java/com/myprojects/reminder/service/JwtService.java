package com.myprojects.reminder.service;

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

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(),userDetails);
    }

    //extraClaims means something like "role", userDetails.getUser().getRoles(). We can add extra information to the token.
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims,userDetails,expiration);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return builder.get()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .expiration(Date.from(Instant.now().plusSeconds(expiration)))
                .compact();
    }
}
