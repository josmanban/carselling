package car.selling.pruebas.services;

import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class DistanceAPIInfoService {

    @Value("${distance.api.url}")
    private String distanceApiUrl;

    @Value("${distance.api.service}")
    private String distanceApiService;

    /**
     * Obtiene información de la API de distancia.
     * 
     * Realiza una petición HTTP GET al servicio de API de distancia configurado,
     * procesa la respuesta JSON y extrae el estado de la respuesta.
     * 
     * @return JsonNode que contiene la estructura JSON completa de la respuesta de la API
     * @throws IllegalStateException si ocurre un error al parsear la respuesta JSON
     * 
     * @implNote Este método utiliza WebClient para realizar la petición HTTP de forma
     *           bloqueante. La respuesta se parsea usando ObjectMapper de Jackson.
     *           Se extrae y imprime el campo "status" si existe en la respuesta.
     */
    public JsonNode getInfo(){
        WebClient webClient = WebClient.create(distanceApiUrl);
        ObjectMapper objectMapper = new ObjectMapper();        
        String response = webClient.get()
            .uri(distanceApiService)
            .retrieve()
            .bodyToMono(String.class)
            .block();
        
        try{
            JsonNode jsonNode = objectMapper.readTree(response);        
            String status = jsonNode.has("status") ? jsonNode.get("status").asText() : "unknown";
            System.out.println("Status from JSON: " + status);
            return jsonNode;
        }catch(JsonProcessingException e){
            throw new IllegalStateException("Error parsing JSON response: " + e.getMessage());
        }
    }
}