package com.jwtservice.service;

import com.jwtservice.dto.AuthenticationRequest;
import com.jwtservice.dto.AuthenticationResponse;
import com.jwtservice.dto.RegisteredUser;
import com.jwtservice.dto.SaveUser;
import com.jwtservice.entity.User;
import com.jwtservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    public RegisteredUser registerOneCustomer(SaveUser newUser){
        User user = userService.registerOneCustomer(newUser);

        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setId(user.getId());
        registeredUser.setUsername(user.getUsername());
        registeredUser.setName(user.getName());
        registeredUser.setRole(user.getRole().name());

        String jwt= jwtService.generateToken(user,generateClaims(user));
        registeredUser.setJwt(jwt);
        return registeredUser;
    }

    public Map<String,Object> generateClaims(User user){
        Map<String,Object> claims = new HashMap<>();
        claims.put("username",user.getUsername());
        claims.put("name",user.getName());
        claims.put("role",user.getRole().name());
        claims.put("authorities",user.getAuthorities());
        return claims;
    }

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword());

        authenticationManager.authenticate(authentication);
        //SecurityContextHolder.getContext().getAuthentication().setAuthenticated(true);

        User user= userService.findOneByUserName(authenticationRequest.getUsername()).get();
        String jwt =jwtService.generateToken(user, generateClaims(user));
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwt);
        return authenticationResponse;
    }


    public boolean validate(String token) {
        try {
            jwtService.extractUserName(token);
            return true;
        }catch (Exception e){
            return false;
        }

    }
}
