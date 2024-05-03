package com.jwtservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiError {
    private String backendMessage;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    private String method;
}
