package com.jwtservice.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LogoutResponse implements Serializable {

    private String message;

    public LogoutResponse(String message){
        this.message=message;
    }
}
