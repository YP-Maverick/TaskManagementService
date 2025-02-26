package com.example.taskmanagementservice.user.repository;

import com.example.taskmanagementservice.user.model.Role;
import com.example.taskmanagementservice.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Page<User> findByRole(Role role, Pageable pageable);

    void deleteById(Integer id);
}
