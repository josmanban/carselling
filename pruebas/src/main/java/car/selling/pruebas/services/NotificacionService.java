package car.selling.pruebas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import car.selling.pruebas.services.ClientCredentialsService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;

@Service
public class NotificacionService {

    private final ClientCredentialsService clientCredentialsService;
    
    @Value("${notifications.api.url}")
    private String notificationsApiUrl;
    
    @Value("${notifications.api.service}")
    private String notificationsApiService;

    @Autowired
    public NotificacionService(ClientCredentialsService clientCredentialsService) {
        this.clientCredentialsService = clientCredentialsService;
    }

    /**
     * Envía una notificación a través del microservicio de notificaciones.
     * 
     * Este método se encarga de:
     * 1. Obtener un token de acceso utilizando el servicio de ClientCredentials
     * 2. Construir el cuerpo de la solicitud con el mensaje y el número de teléfono
     * 3. Realizar una petición POST al microservicio de notificaciones
     * 4. Autenticar la petición utilizando el token obtenido
     * 
     * @param mensaje el contenido del mensaje a enviar en la notificación
     * @param telefonoContacto el número de teléfono del contacto destino
     * @return una cadena de texto que contiene:
     *         - El mensaje enviado
     *         - El número de teléfono destino
     *         - La respuesta del servicio de notificaciones
     * 
     * @throws WebClientException si hay un error en la comunicación con el 
     *         microservicio de notificaciones
     * @throws IllegalStateException si no se puede obtener el token de acceso
     */
    public String enviarNotificacion(String mensaje, String telefonoContacto) {
        // Obtener el token de acceso usando ClientCredentialsService
        // Este token se usará para autenticar la petición al microservicio de notificaciones

        String token = clientCredentialsService.getAccessToken();        

        String body = "{\"mensaje\":\""+mensaje+"\",\"telefonos\":[\""+telefonoContacto+"\"]}";


        String response = WebClient.create()
            .post()
            .uri(notificationsApiUrl + notificationsApiService)
            .header("Content-Type","application/json")
            .header("Authorization", "Bearer "+ token)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(String.class)
            .block();
        
        return "Notificación enviada: " + mensaje + " al número: " + telefonoContacto + ". Respuesta del servicio: " + response;

    }
    
}
