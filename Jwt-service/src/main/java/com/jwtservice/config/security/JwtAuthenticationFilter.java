package com.jwtservice.config.security;

import com.jwtservice.entity.User;
import com.jwtservice.service.JwtService;
import com.jwtservice.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtService jwtService, @Qualifier("userService") UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (!StringUtils.hasText(authHeader) && !authHeader.startsWith("Bearer ")) {
           filterChain.doFilter(request, response);
           return;
        }

        String token = authHeader.split("Bearer ")[1];
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String username= jwtService.extractUserName(token);

        User userDetails = userService.findOneByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}


