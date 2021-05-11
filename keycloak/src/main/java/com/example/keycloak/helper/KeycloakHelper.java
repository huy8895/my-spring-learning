package com.example.keycloak.helper;

import com.example.keycloak.constant.Constants;
import com.example.keycloak.dao.UserProfileJpaRepository;
import com.example.keycloak.entity.UserProfile;
import com.example.keycloak.util.KeyCloakUtil;
import com.example.keycloak.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.UserSessionRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeycloakHelper {

    @Value("${keycloak.auth-server-url}")
    private String SERVER_URL;

    @Value("${keycloak.realm}")
    private String REALM;

    @Value("${custom.properties.keycloak-admin-username}")
    private String USERNAME;

    @Value("${custom.properties.keycloak-admin-password}")
    private String PASSWORD;

    @Value("${keycloak.resource}")
    private String CLIENT_ID;

    @Value("${keycloak.credentials.secret}")
    private String CLIENT_SECRET;

    private final KeycloakSpringBootProperties kclProperties;
    private final UserProfileJpaRepository upRepo;


    public Keycloak getInstance() {
        return KeycloakBuilder
                .builder()
                .serverUrl(SERVER_URL)
                .realm(REALM)
                .username(USERNAME)
                .password(PASSWORD)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .grantType(OAuth2Constants.PASSWORD) //
                .build();
    }

    /**
     * By default KeyCloak REST API doesn't allow to create account with credential type is PASSWORD,
     * it means after created account, need an extra step to make it works, it's RESET PASSWORD
     * @param username
     * @param password
     * @return
     */
    public int createAccount(final String username, final String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setFirstName("First Name");
        user.setLastName("Last Name");
        user.singleAttribute("customAttribute", "customAttribute");
        user.setCredentials(Arrays.asList(credential));
        Response response = getInstance().realm(REALM).users().create(user);
        final int status = response.getStatus();
        if (status != HttpStatus.CREATED.   value()) {
            return status;
        }
        final String createdId = KeyCloakUtil.getCreatedId(response);
        // Reset password
        CredentialRepresentation newCredential = new CredentialRepresentation();
        UserResource userResource = getInstance().realm(REALM).users().get(createdId);
        newCredential.setType(CredentialRepresentation.PASSWORD);
        newCredential.setValue(password);
        newCredential.setTemporary(false);
        userResource.resetPassword(newCredential);
        return HttpStatus.CREATED.value();
    }

    public CredentialRepresentation credentialRepresentation(String password) {
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        if (StringUtils.isNullOrEmpty(password)) {
            passwordCred.setValue(Constants.KC_PASS_DEFAULT);
        } else {
            passwordCred.setValue(password);
        }
        return passwordCred;
    }

    public void setPassword(UserProfile user, String password) {
        // login with admin user
        Keycloak instance = getKeycloak();
        UsersResource usersResource = instance.realm(kclProperties.getRealm()).users();
        UserResource userResource = usersResource.get(user.getKeycloakId());
        userResource.resetPassword(credentialRepresentation(password));
    }

    public void logout(String username) {

        if (StringUtils.isNullOrEmpty(username))
            return;

        Optional<UserProfile> user = upRepo.findByUsername(username);

        /* không tồn tại thì return luôn không cần phải làm gì tiếp theo */
        if (!user.isPresent())
            return;

        // login with admin user
        Keycloak instance = getKeycloak();
        UsersResource usersResource = instance.realm(kclProperties.getRealm()).users();
        UserResource userResource = usersResource.get(user.get().getKeycloakId());
        List<UserSessionRepresentation> userSessions = userResource.getUserSessions();

        if (!userSessions.isEmpty())
            userResource.logout();
    }

    public Keycloak getKeycloak() {
        return Keycloak.getInstance(kclProperties.getAuthServerUrl(),
                kclProperties.getRealm(),
                this.USERNAME,
                this.PASSWORD,
                kclProperties.getResource(),
                kclProperties.getCredentials()
                             .get("secret").toString());
    }

    public RealmResource getRealResource(){
        Keycloak keycloak = KeycloakBuilder.builder()
                                           .serverUrl(SERVER_URL)
                                           .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                                           .realm(REALM)
                                           .clientId(CLIENT_ID)
                                           .clientSecret(CLIENT_SECRET)
                                           .resteasyClient(
                                                   new ResteasyClientBuilder()
                                                           .connectionPoolSize(10).build()
                                           ).build();

//        keycloak.tokenManager().getAccessToken();
        RealmResource realmResource = keycloak.realm(REALM);
        return realmResource;
    }

}
