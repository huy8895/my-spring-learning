package com.example.keycloak.dao;

import com.example.keycloak.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileJpaRepository extends JpaRepository<UserProfile, String> {
    Optional<UserProfile> findByUsername(String username);
}
