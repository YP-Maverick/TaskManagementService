package com.example.taskmanagementservice.jwt.repository;

import java.util.List;
import java.util.Optional;

import com.example.taskmanagementservice.jwt.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = """
            select t from _token t
            inner join t.user u
            where u.id = :id and (t.expired = false and t.revoked = false)
            """)
    List<Token> findAllValidTokenByUser(@Param("id") Long id);

    Optional<Token> findByToken(String token);
}
