package com.jwtservice.service;

import com.jwtservice.dto.SaveUser;
import com.jwtservice.entity.User;
import com.jwtservice.exception.InvalidPasswordException;
import com.jwtservice.repository.UserRepository;
import com.jwtservice.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerOneCustomer(SaveUser newUser) {

        validatePassword(newUser);
        User user = new User();
        user.setUsername(newUser.getUsername());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setPassword(newUser.getPassword());
        user.setRole(Role.ROLE_ADMINISTRATOR);
        return userRepository.save(user);
    }

    private void validatePassword(SaveUser newUser) {

        if(!StringUtils.hasText(newUser.getPassword()) ||  !StringUtils.hasText(newUser.getRepeatedPassword()) ){
            throw new InvalidPasswordException("Password don't match");
        }
        if(!newUser.getRepeatedPassword().equals(newUser.getPassword())){
            throw new InvalidPasswordException("Password don't match");
        }
    }

}
