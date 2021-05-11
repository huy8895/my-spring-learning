package com.example.keycloak.controller;

import com.example.keycloak.KeycloakAdminClientExample;
import com.example.keycloak.entity.UserProfile;
import com.example.keycloak.model.UserProfileDto;
import com.example.keycloak.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-profiles")
public class UserProfileController {
    private final UserProfileService userProfileService;
    private final KeycloakAdminClientExample example;

    @PostMapping
    public UserProfile create(@RequestBody UserProfileDto dto){
        return userProfileService.save(dto);
    }
}
