package com.jwtservice.repository;

import com.jwtservice.entity.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtRepository extends JpaRepository<JwtToken,Long> {
    Optional<JwtToken> findByToken(String token);
}
