package com.jwtservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RegisteredUser implements Serializable {

    private Long id;
    private String username;
    private String name;
    private String role;
    private String jwt;
}
