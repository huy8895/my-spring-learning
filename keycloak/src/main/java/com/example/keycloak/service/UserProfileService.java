package com.example.keycloak.service;

import com.example.keycloak.entity.UserProfile;
import com.example.keycloak.model.UserProfileDto;

public interface UserProfileService {
    UserProfile save(UserProfileDto dto);
}
