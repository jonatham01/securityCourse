package com.jwtservice.controller;

import com.jwtservice.dto.RegisteredUser;
import com.jwtservice.dto.SaveUser;
import com.jwtservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private AuthService authService;

    @PreAuthorize("permitAll()")
    @PostMapping
    public ResponseEntity<RegisteredUser> registerOne(@RequestBody @Validated SaveUser newUser){

        RegisteredUser registeredUser = authService.registerOneCustomer(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }
}
