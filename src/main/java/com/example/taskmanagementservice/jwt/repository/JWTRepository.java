package com.example.taskmanagementservice.jwt.repository;

import com.example.taskmanagementservice.jwt.model.Token;
import com.example.taskmanagementservice.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JWTRepository extends JpaRepository<Token, Integer> {

    Optional<Token> findByToken(String token);

    List<Token> findAllByUser(User user);

    long countByUserAndRevokedFalseAndExpiredFalse(User user);
}
