package car.selling.pruebas.services;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;


@Service
public class ClientCredentialsService {

    @Value("${microservices.server-url}")
    private String serverUrl;

    @Value("${microservices.realm}")
    private String realm;

    @Value("${microservices.admin-client-id}")
    private String clientId;

    @Value("${microservices.admin-client-secret}")
    private String clientSecret;    


    /**
     * Obtiene un token de acceso utilizando el flujo Client Credentials de OAuth2.
     * 
     * Este método se comunica con el servidor de autorización (Keycloak) para obtener
     * un token de acceso que se utiliza para autenticar las solicitudes a otros microservicios.
     * 
     * El proceso incluye:
     * <ul>
     *   <li>Construir una solicitud con el tipo de concesión "client_credentials"</li>
     *   <li>Incluir las credenciales del cliente (client_id y client_secret)</li>
     *   <li>Enviar una solicitud POST al endpoint de token del servidor de autorización</li>
     *   <li>Extraer y validar el token de acceso de la respuesta</li>
     * </ul>
     * 
     * @return {@code String} El token de acceso obtenido del servidor de autorización,
     *         necesario para autenticar solicitudes a microservicios.
     * 
     * @throws IllegalStateException si la respuesta del servidor no contiene un token
     *         de acceso válido o si la respuesta es nula.
     * 
     * @see org.springframework.web.reactive.function.client.WebClient
     * @see com.fasterxml.jackson.databind.JsonNode
     */
    public String getAccessToken(){
        // Obtener un token de acceso usando el flujo Client Credentials de OAuth2
        // Este token se utilizará para autenticar las solicitudes a otros microservicios
        
        // Construir el cuerpo de la solicitud con los parámetros requeridos
        MultiValueMap<String,String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);

        // URL del servidor de autorización (Keycloak)
        String tokenUrl = "/realms/" + realm + "/protocol/openid-connect/token";

        // Crear el cliente HTTP WebClient con la URL base del servidor
        WebClient webClient = WebClient.builder().baseUrl(serverUrl).build();

        // Enviar la solicitud POST al servidor de autorización y obtener la respuesta
        JsonNode tokenResponse = webClient.post()
            .uri(tokenUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(form))
            .retrieve()
            .bodyToMono(JsonNode.class)
            .block();

        // Validar que la respuesta contiene el token de acceso
        if (tokenResponse == null || tokenResponse.get("access_token") == null) {
            throw new IllegalStateException("No access_token in token response");
        }

        // Extraer y retornar el token de acceso
        String accessToken = tokenResponse.get("access_token").asText();
        return accessToken;

    }

    
    
}
