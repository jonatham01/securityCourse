package com.jwtservice.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveUser {

    @Size(min = 4, max = 20)
    private String username;
    @Size(min = 4, max = 20)
    private String name;
    @Size(min = 8, max = 20)
    private String password;
    @Size(min = 4, max = 20)
    private String repeatedPassword;
}
