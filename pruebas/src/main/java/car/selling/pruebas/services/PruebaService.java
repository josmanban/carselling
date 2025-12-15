package car.selling.pruebas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import car.selling.pruebas.base.HaversineCalculator;
import car.selling.pruebas.base.InsideAreaCalculator;
import car.selling.pruebas.dataTransferObjects.PosicionDTO;
import car.selling.pruebas.models.Empleado;
import car.selling.pruebas.models.Incidencia;
import car.selling.pruebas.models.Interesado;
import car.selling.pruebas.models.Prueba;
import car.selling.pruebas.models.Posicion;
import car.selling.pruebas.repositories.IncidenciaRepository;
import car.selling.pruebas.repositories.InteresadoRepository;
import car.selling.pruebas.repositories.PruebaRepository;
import car.selling.pruebas.services.NotificacionService;

import com.fasterxml.jackson.databind.JsonNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
@Service
public class PruebaService {

    
    private final PruebaRepository pruebaRepository;        
    private final IncidenciaRepository incidenciaRepository;    
    private final InteresadoRepository interesadoRepository;

    private final NotificacionService notificacionService;

    @Autowired
    public PruebaService(
        PruebaRepository pruebaRepository,        
        IncidenciaRepository incidenciaRepository,
        InteresadoRepository interesadoRepository,
        NotificacionService notificacionService
    ) {
        this.pruebaRepository = pruebaRepository;        
        this.incidenciaRepository = incidenciaRepository;
        this.interesadoRepository = interesadoRepository;
        this.notificacionService = notificacionService;
    }    

    /**
     * Verifica la posición actual del vehículo respecto a las zonas permitidas y restringidas.
     * 
     * Este método realiza dos validaciones principales:
     * 1. Comprueba que el vehículo se encuentre dentro del radio permitido desde la agencia
     * 2. Valida que el vehículo no se encuentre dentro de ninguna zona geográfica restringida
     * 
     * En caso de que el vehículo incumpla alguna de estas condiciones, el método:
     * - Marca al interesado como restringido
     * - Registra una incidencia en el sistema
     * - Envía una notificación al interesado
     * - Lanza una excepción indicando la razón de la restricción
     * 
     * De ser necesario enviar una notificación, se utiliza el microservicio de notificaciones.
     * 
     * @param entity {@link PosicionDTO} que contiene las coordenadas actuales del vehículo (latitud y longitud)
     * @param interesado {@link Interesado} propietario o responsable del vehículo
     * @param prueba {@link Prueba} entidad de prueba asociada al vehículo
     * @param distanceAPIData {@link JsonNode} datos con configuración de distancias, coordenadas de la agencia 
     *                        y definición de zonas restringidas
     * @return {@link String} "OK" si la posición del vehículo es válida
     * @throws IllegalStateException si el vehículo se encuentra fuera del radio permitido 
     *                               o dentro de una zona restringida
     */
    public boolean checkPosicionVehiculoService(
        Posicion posicion,
        Interesado interesado, 
        Prueba prueba,
        JsonNode distanceAPIData
        ) {            
            Logger logger = org.slf4j.LoggerFactory.getLogger(PruebaService.class);

            logger.info("************************************************************************************");
            logger.info("REVISANDO POSICIÓN DEL VEHÍCULO...");
            logger.info("************************************************************************************");

            int radioAtmitido = distanceAPIData.has("radioAdmitidoKm") ? distanceAPIData.get("radioAdmitidoKm").asInt() : 0;

            JsonNode coordenadasAgencia = distanceAPIData.get("coordenadasAgencia");
            double latAgencia = coordenadasAgencia.get("lat").asDouble();
            double lonAgencia = coordenadasAgencia.get("lon").asDouble();

            double latVehiculo = posicion.getLatitud();
            double lonVehiculo = posicion.getLongitud();

            double distanciaAgencia = HaversineCalculator.calcularDistancia(latAgencia, lonAgencia, latVehiculo, lonVehiculo);

            logger.info("**************************************************************************************");
            logger.info(radioAtmitido + " km de radio admitido.");
            logger.info("Coordenadas agencia: lat " + latAgencia + ", lon " + lonAgencia);
            logger.info("Coordenadas vehículo: lat " + latVehiculo + ", lon " + lonVehiculo);
            logger.info("Distancia desde la agencia: " + distanciaAgencia + " km");
            logger.info("**************************************************************************************");


            if (distanciaAgencia > radioAtmitido) {
                interesado.setRestringido(true);
                this.interesadoRepository.save(interesado);
                
                this.incidenciaRepository.save(
                    Incidencia.builder()
                        .prueba(prueba)
                        .esZonaRestringida(false)
                        .distanciaAgenciaKm(distanciaAgencia)
                        .posicion(posicion)
                        .fechaHora(LocalDateTime.now())                        
                        .build()
                );
                
                this.notificacionService.enviarNotificacion(
                    "El vehículo está fuera del área permitida. Distancia: " + distanciaAgencia + " km",
                    interesado.getTelefonoContacto()
                );
                logger.warn("********************************************************************************************");                
                logger.warn("El vehículo está fuera del área permitida. Distancia: " + distanciaAgencia + " km");
                logger.warn("********************************************************************************************");    
                throw new IllegalStateException("El vehículo está fuera del área permitida. Distancia: " + distanciaAgencia + " km");
            }

            JsonNode zonasRestringidas = distanceAPIData.get("zonasRestringidas");
            for (JsonNode zona : zonasRestringidas) {
                JsonNode esquina1 = zona.get("noroeste");
                JsonNode esquina2 = zona.get("sureste");
                double latMin = Math.min(esquina1.get("lat").asDouble(), esquina2.get("lat").asDouble());
                double lonMin = Math.min(esquina1.get("lon").asDouble(), esquina2.get("lon").asDouble());
                double latMax = Math.max(esquina1.get("lat").asDouble(), esquina2.get("lat").asDouble());
                double lonMax = Math.max(esquina1.get("lon").asDouble(), esquina2.get("lon").asDouble());

                if (InsideAreaCalculator.isInsideArea(latVehiculo, lonVehiculo, latMin, lonMin, latMax, lonMax)) {
                    interesado.setRestringido(true);
                    this.interesadoRepository.save(interesado);

                    this.incidenciaRepository.save(
                        Incidencia.builder()
                            .prueba(prueba)
                            .esZonaRestringida(true)
                            .distanciaAgenciaKm(distanciaAgencia)
                            .posicion(posicion)          
                            .fechaHora(LocalDateTime.now())                     
                            .build()
                    );                    
                    
                    this.notificacionService.enviarNotificacion(
                        "El vehículo está dentro de una zona restringida.",
                        interesado.getTelefonoContacto()
                    );

                    logger.warn("********************************************************************************************");
                    logger.warn("El vehículo está dentro de una zona restringida.");
                    logger.warn("********************************************************************************************");
                    throw new IllegalStateException("El vehículo está dentro de una zona restringida.");
                }
            }
            return true;        
    }
}
