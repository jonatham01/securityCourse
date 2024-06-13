package com.jwtservice.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.net.http.HttpRequest;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("60")
    private long expiration;

    private final String secret = "clavedadasnfdasidnaindaidnasnmasnmoamsoamsoamsoasomafgfgsapspaspas";
    
    @Value("${security.jwt.secret}")
    private String SECRET_KEY;
    
    private SecretKey generateKey(){
            byte[] key = Decoders.BASE64.decode(SECRET_KEY);
            return Keys.hmacShaKeyFor(key);
    }
    
     public String generateToken(UserDetails user, Map<String, Object> extraClaims) {
     
             Date issuedAt = new Date(System.currentTimeMillis());
             Date expirationDate = new Date( (expiration * 60 * 1000) + issuedAt.getTime() ) ;
     
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
    public String extractJwtRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null && !bearerToken.startsWith("Bearer ")) {
            return  null;
        }
        return bearerToken.split("Bearer ", 2)[1];
    }

}

