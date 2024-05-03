package com.jwtservice.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwtservice.dto.ApiError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        ApiError error = new ApiError();
        error.setBackendMessage(authException.getLocalizedMessage());
        error.setMessage("Credentials has not been provided. Please provide a valid credentials.");
        error.setPath(request.getRequestURI());
        error.setMethod(request.getMethod());
        error.setTimestamp(LocalDateTime.now());
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
        String jsonError= new ObjectMapper().writeValueAsString(error);
        response.getWriter().print(jsonError);
    }
}
