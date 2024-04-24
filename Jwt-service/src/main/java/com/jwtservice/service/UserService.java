package com.jwtservice.service;

import com.jwtservice.dto.SaveUser;
import com.jwtservice.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public User registerOneCustomer(SaveUser newUser);
}
