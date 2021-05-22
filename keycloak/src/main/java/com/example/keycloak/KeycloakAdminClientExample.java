package com.example.keycloak;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;

@Component
public class KeycloakAdminClientExample {

    public void create() {

        String serverUrl = "http://localhost:8180/auth";
        String realm = "SpringBootKeycloak";
        String clientId = "login-app";
        String clientSecret = "a3066b73-eff3-4229-8038-a19e8f4f2351";

//		// Client "idm-client" needs service-account with at least "manage-users, view-clients, view-realm, view-users" roles for "realm-management"
//		Keycloak keycloak = KeycloakBuilder.builder() //
//				.serverUrl(serverUrl) //
//				.realm(realm) //
//				.grantType(OAuth2Constants.CLIENT_CREDENTIALS) //
//				.clientId(clientId) //
//				.clientSecret(clientSecret).build();

        // User "javaland" needs at least "manage-users, view-clients, view-realm, view-users" roles for "realm-management"
        Keycloak keycloak = KeycloakBuilder.builder() //
                                           .serverUrl(serverUrl) //
                                           .realm(realm) //
                                           .grantType(OAuth2Constants.PASSWORD) //
                                           .clientId(clientId) //
                                           .clientSecret(clientSecret) //
                                           .username("huy8895") //
                                           .password("12121212!q") //
                                           .build();

        // Define user
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername("tester1");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setEmail("tom+tester1@tdlabs.local");
//        user.setAttributes(Collections.singletonMap("origin", Arrays.asList("demo")));

        // Get realm
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource userRessource = realmResource.users();

        // Create user (requires manage-users role)
        Response response = userRessource.create(user);
        System.out.println("Repsonse: " + response.getStatusInfo());
        System.out.println(response.getLocation());
        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        System.out.printf("User created with userId: %s%n", userId);

        // Get realm role "tester" (requires view-realm role)
        RoleRepresentation testerRealmRole = realmResource.roles()//
                                                          .get("user").toRepresentation();

        // Assign realm role tester to user
        userRessource.get(userId).roles().realmLevel() //
                     .add(Arrays.asList(testerRealmRole));

        // Get client
        ClientRepresentation app1Client = realmResource.clients() //
                                                       .findByClientId("app-javaee-petclinic").get(0);

        // Get client level role (requires view-clients role)
        RoleRepresentation userClientRole = realmResource.clients().get(app1Client.getId()) //
                                                         .roles().get("user").toRepresentation();

        // Assign client level role to user
        userRessource.get(userId).roles() //
                     .clientLevel(app1Client.getId()).add(Arrays.asList(userClientRole));

        // Define password credential
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue("test");

        // Set password credential
        userRessource.get(userId).resetPassword(passwordCred);

    }
}
