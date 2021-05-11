package com.example.keycloak.service.impl;

import com.example.keycloak.dao.UserProfileJpaRepository;
import com.example.keycloak.entity.UserProfile;
import com.example.keycloak.helper.KeycloakHelper;
import com.example.keycloak.model.UserProfileDto;
import com.example.keycloak.service.UserProfileService;
import com.example.keycloak.util.KeyCloakUtil;
import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileServiceIml implements UserProfileService {
    private final UserProfileJpaRepository upRepo;
    private final KeycloakHelper keycloakHelper;
    private final KeycloakSpringBootProperties kclProperties;

    @Override
    public UserProfile save(UserProfileDto dto) {
        Keycloak keycloak = keycloakHelper.getKeycloak();
        UsersResource usersResource = keycloak.realm(kclProperties.getRealm()).users();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(dto.getUsername());
        userRepresentation.setEnabled(true);
        Response response = usersResource.create(userRepresentation);
        String keycloakUserId = CreatedResponseUtil.getCreatedId(response);

        final int status = response.getStatus();
        if (status != HttpStatus.CREATED.value()) {
            return null;
        }
        final String createdId = KeyCloakUtil.getCreatedId(response);
        // Reset password
        CredentialRepresentation newCredential = new CredentialRepresentation();
        UserResource userResource = keycloakHelper.getInstance()
                                                  .realm(kclProperties.getRealm())
                                                  .users()
                                                  .get(createdId);
        newCredential.setType(CredentialRepresentation.PASSWORD);
        newCredential.setValue(dto.getPassword());
        newCredential.setTemporary(false);
        userResource.resetPassword(newCredential);
        UserProfile userProfile = UserProfile.builder()
                                             .username(dto.getUsername())
                                             .keycloakId(createdId)
                                             .build();
        return upRepo.save(userProfile);
    }
}
