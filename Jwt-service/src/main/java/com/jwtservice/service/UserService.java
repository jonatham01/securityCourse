package com.jwtservice.service;

import com.jwtservice.dto.SaveUser;
import com.jwtservice.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    public User registerOneCustomer(SaveUser newUser);

    public Optional<User> findOneByUserName(String userName);
}
