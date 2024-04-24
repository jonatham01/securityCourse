package com.jwtservice.service;

import com.jwtservice.dto.RegisteredUser;
import com.jwtservice.dto.SaveUser;
import com.jwtservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    public RegisteredUser registerOneCustomer(SaveUser newUser){
        User user = userService.registerOneCustomer(newUser);

        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setId(user.getId());
        registeredUser.setUsername(user.getUsername());
        registeredUser.setName(user.getName());
        registeredUser.setRole(user.getRole().name());

        String jwt= jwtService.generateToken(user);
        registeredUser.setJwt(jwt);
        return registeredUser;
    }
}
