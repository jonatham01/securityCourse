package com.jwtservice.controller;

import com.jwtservice.dto.AuthenticationRequest;
import com.jwtservice.dto.AuthenticationResponse;
import com.jwtservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @GetMapping("validate-token")
    public ResponseEntity<Boolean> validate(@RequestParam String token) {
        boolean isTokenValid = authService.validate(token);
        return ResponseEntity.ok(isTokenValid);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest authenticationRequest){

            AuthenticationResponse response = authService.login(authenticationRequest);
            return ResponseEntity.ok(response);
    }
}
