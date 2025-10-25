package com.pramod.splitwise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pramod.splitwise.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findByOauthId(String oauthId);
}

