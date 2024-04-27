package com.jwtservice.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
public class AuthenticationResponse implements Serializable {
    private String token;

}
