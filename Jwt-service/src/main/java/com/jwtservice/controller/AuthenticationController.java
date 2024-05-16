package com.jwtservice.controller;

import com.jwtservice.dto.AuthenticationRequest;
import com.jwtservice.dto.AuthenticationResponse;
import com.jwtservice.dto.LogoutResponse;
import com.jwtservice.entity.User;
import com.jwtservice.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @PreAuthorize("permitAll()")
    @GetMapping("validate-token")
    public ResponseEntity<Boolean> validate(@RequestParam String token) {
        boolean isTokenValid = authService.validate(token);
        return ResponseEntity.ok(isTokenValid);
    }

    @CrossOrigin
    @PreAuthorize("permitAll()")
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest authenticationRequest){

            AuthenticationResponse response = authService.login(authenticationRequest);
            return ResponseEntity.ok(response);
    }
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(HttpServletRequest request){
        authService.logout(request);
        return ResponseEntity.ok(new LogoutResponse("logout successful"));
    }

    @PreAuthorize("hasAuthority('READ_MY_PROFILE')")
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(){
        User user= authService.findLoggedInUser();
        return  ResponseEntity.ok(user);
    }
}
