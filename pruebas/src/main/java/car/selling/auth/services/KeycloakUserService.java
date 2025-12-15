package car.selling.auth.services;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;


@Service
public class KeycloakUserService{
    @Value("${keycloak.server-url}")
    private String serverUrl;
    @Value("${keycloak.realm}")
    private String adminRealm;
    @Value("${keycloak.admin-client-id}")
    private String adminClientId;
    @Value("${keycloak.admin-client-secret}")
    private String adminClientSecret;
    @Value("${keycloak.admin-username}")
    private String adminUser;
    @Value("${keycloak.admin-password}")
    private String adminPass;
    @Value("${keycloak.target-realm}")
    private String targetRealm;

    private Keycloak getKeycloakClient() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(adminRealm)                
                .username(adminUser)
                .password(adminPass)
                .clientId(adminClientId)   
                .clientSecret(adminClientSecret)      
                .build();
    }

    public String createUser(String username, String email, String password,
        String firstname, String lastname,int idEntityCarSelling
     ) {
        Keycloak kc = getKeycloakClient();

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setAttributes(
            Collections.singletonMap("idEntityCarSelling", 
                Collections.singletonList(String.valueOf(idEntityCarSelling)))
        );

        // añadir credencial (password)
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(password);
        user.setCredentials(Collections.singletonList(cred));

        var response = kc.realm(targetRealm).users().create(user);
        if (response.getStatus() == 201) {
            // Location header contiene el id del usuario -> extraer id
            String location = response.getHeaderString("Location"); // .../users/{id}
            String id = location.substring(location.lastIndexOf('/') + 1);
            return id;
        } else {
            throw new RuntimeException("Keycloak create user failed: " + response.getStatusInfo());
        }
    }

    public void assignRealmRoleToUser(String userId, String roleName) {
        Keycloak kc = getKeycloakClient();
        var realmResource = kc.realm(targetRealm);
        var role = realmResource.roles().get(roleName).toRepresentation();
        realmResource.users().get(userId).roles().realmLevel().add(Collections.singletonList(role));
    }


}