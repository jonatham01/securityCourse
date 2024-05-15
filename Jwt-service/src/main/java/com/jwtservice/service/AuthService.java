package com.jwtservice.service;

import com.jwtservice.dto.AuthenticationRequest;
import com.jwtservice.dto.AuthenticationResponse;
import com.jwtservice.dto.RegisteredUser;
import com.jwtservice.dto.SaveUser;
import com.jwtservice.entity.JwtToken;
import com.jwtservice.entity.User;
import com.jwtservice.repository.JwtRepository;
import com.jwtservice.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Autowired
    private JwtRepository jwtTokenRepository;

    public RegisteredUser registerOneCustomer(SaveUser newUser){
        User user = userService.registerOneCustomer(newUser);
        String jwt= jwtService.generateToken(user,generateClaims(user));
        saveUserToken(user,jwt);

        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setId(user.getId());
        registeredUser.setUsername(user.getUsername());
        registeredUser.setName(user.getName());
        registeredUser.setRole(user.getRole().name());


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

        saveUserToken(user,jwt);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwt);
        return authenticationResponse;
    }

    private void saveUserToken(User user, String jwt) {

        JwtToken token = new JwtToken();
        token.setToken(jwt);
        token.setUser(user);
        token.setExpiration(jwtService.extractExpiration(jwt));
        token.setValid(true);
        jwtTokenRepository.save(token);

    }

    public void logout(HttpServletRequest request){
        String jwt = jwtService.extractJwtRequest(request);
        if(  !StringUtils.hasText(jwt) || jwt ==null) return;


        Optional<JwtToken> token = jwtTokenRepository.findByToken(jwt);

        if(token.isPresent() && token.get().isValid()){
            token.get().setValid(false);
            jwtTokenRepository.save(token.get());
        }
    }


    public boolean validate(String token) {
        try {
            jwtService.extractUserName(token);
            return true;
        }catch (JwtException e){
            return false;
        }

    }

    public User findLoggedInUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = (String) authentication.getPrincipal();
        return userService.findOneByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
