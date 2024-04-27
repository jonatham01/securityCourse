package com.jwtservice.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
public class AuthenticationRequest implements Serializable {
    private String username;
    private String password;
}
