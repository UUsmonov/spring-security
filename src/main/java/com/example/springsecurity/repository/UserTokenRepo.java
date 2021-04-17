package com.example.springsecurity.repository;

import com.example.springsecurity.model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTokenRepo extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByToken(String token);
}
