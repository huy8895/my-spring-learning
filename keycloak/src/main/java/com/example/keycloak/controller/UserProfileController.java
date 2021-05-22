package com.example.keycloak.controller;

import com.example.keycloak.KeycloakAdminClientExample;
import com.example.keycloak.entity.UserProfile;
import com.example.keycloak.model.UserProfileDto;
import com.example.keycloak.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.Response;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-profiles")
public class UserProfileController {
    private final UserProfileService userProfileService;
    private final KeycloakAdminClientExample example;
    private String authServerUrl = "http://localhost:8180/auth";

    @Value("${keycloak.auth-server-url}")
    private String SERVER_URL;

    @Value("${keycloak.realm}")
    private String REALM;

    @Value("${keycloak.resource}")
    private String CLIENT_ID;

    @Value("${keycloak.credentials.secret}")
    private String CLIENT_SECRET;

    @Value("${custom.properties.keycloak-admin-username}")
    private String USERNAME;

    @Value("${custom.properties.keycloak-admin-password}")
    private String PASSWORD;

    @PostMapping
    public UserProfile create(@RequestBody UserProfileDto dto){
        return userProfileService.save(dto);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<?> createUser(@RequestBody UserProfileDto userDTO) {

        // co the dung tai khoan root : admin / admin de get keycloak
        Keycloak keycloak_main = KeycloakBuilder.builder().serverUrl(authServerUrl)
                                                .grantType(OAuth2Constants.PASSWORD)
                                                .realm("master")
                                                .clientId("admin-cli")
                                                .username("admin")
                                                .password("admin")
                                                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10)
                                                                                           .build())
                                                .build();
        // hoac dung tai khoan khac nhung co full quyen.
        Keycloak keycloak = KeycloakBuilder.builder()
                                           .serverUrl(authServerUrl)
                                           .grantType(OAuth2Constants.PASSWORD)
                                           .realm(REALM)
                                           .clientId(CLIENT_ID)
                                           .clientSecret(CLIENT_SECRET)
                                           .username(USERNAME)
                                           .password(PASSWORD)
                                           .resteasyClient(
                                                   new ResteasyClientBuilder()
                                                           .connectionPoolSize(10)
                                                           .build())
                                           .build();

        keycloak.tokenManager().getAccessToken();


        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userDTO.getUsername());

        // Get realm
        RealmResource realmResource = keycloak.realm(REALM);
        UsersResource usersResource = realmResource.users();

        Response response = usersResource.create(user);


        if (response.getStatus() == 201) {

            String userId = CreatedResponseUtil.getCreatedId(response);



            // create password credential
            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(false);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(userDTO.getPassword());

            UserResource userResource = usersResource.get(userId);

            // Set password credential
            userResource.resetPassword(passwordCred);

            // Get realm role student
//            RoleRepresentation realmRoleUser = realmResource.roles().get(role).toRepresentation();

            // Assign realm role student to user
//            userResource.roles().realmLevel().add(Arrays.asList(realmRoleUser));
        }
        return ResponseEntity.ok(userDTO);
    }
}
