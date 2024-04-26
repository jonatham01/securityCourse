package com.jwtservice.service;

import com.jwtservice.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${security.jwt.expiration-minutes}")
    private Long EXPIRATION_IN_MINUTES;

    @Value("${security.jwt.secret}")
    private String SECRET_KEY;

    private SecretKey generateKey(){
        byte[] key = Decoders.BASE64.decode("miclaveesseguraadadadadadadadadaddadadafdffadadadae");
        return Keys.hmacShaKeyFor(key);
    }

    public String generateToken(UserDetails user, Map<String, Object> extraClaims) {

        Date issuedAt = new Date(System.currentTimeMillis());
        Date expirationDate = new Date( (EXPIRATION_IN_MINUTES * 60 * 1000) + issuedAt.getTime() ) ;

        return Jwts.builder()
                .header()
                    .type("JWT")
                    .and()
                .subject(user.getUsername())
                .issuedAt(issuedAt)
                .expiration(expirationDate)
                .claims(extraClaims)
                .signWith(generateKey(),Jwts.SIG.HS256)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUserName(String token) {
        return extractAllClaims(token).getSubject();
    }

}
