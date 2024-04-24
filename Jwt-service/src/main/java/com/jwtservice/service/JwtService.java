package com.jwtservice.service;

import com.jwtservice.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    public String generateToken(UserDetails user) {
        return null;
    }
}
