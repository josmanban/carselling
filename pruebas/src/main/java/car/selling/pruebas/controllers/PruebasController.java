package car.selling.pruebas.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import car.selling.pruebas.base.HaversineCalculator;
import car.selling.pruebas.base.InsideAreaCalculator;
import car.selling.pruebas.dataTransferObjects.DistanciaRecorridaDTO;
import car.selling.pruebas.dataTransferObjects.PosicionDTO;
import car.selling.pruebas.dataTransferObjects.PruebaDTO;
import car.selling.pruebas.models.Empleado;
import car.selling.pruebas.models.Incidencia;
import car.selling.pruebas.models.Interesado;
import car.selling.pruebas.models.Posicion;
import car.selling.pruebas.models.Prueba;
import car.selling.pruebas.models.Vehiculo;
import car.selling.pruebas.repositories.EmpleadoRepository;
import car.selling.pruebas.repositories.IncidenciaRepository;
import car.selling.pruebas.repositories.InteresadoRepository;
import car.selling.pruebas.repositories.PosicionRepository;
import car.selling.pruebas.repositories.PruebaRepository;
import car.selling.pruebas.repositories.VehiculoRepository;
import car.selling.pruebas.services.DistanceAPIInfoService;
import car.selling.pruebas.services.NotificacionService;
import car.selling.pruebas.services.PruebaService;

import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;   

@Tag(name = "Pruebas", description = "Operaciones relacionadas con las pruebas de manejo de vehículos a vender")
@RestController
public class PruebasController {
    
    private final InteresadoRepository interesadoRepository;
    
    private final PruebaRepository pruebaRepository;
    private final EmpleadoRepository empleadoRepository;
    private final PosicionRepository posicionRepository;
    private final VehiculoRepository vehiculoRepository;
    
    private final PruebaService pruebaService;
    private final DistanceAPIInfoService distanceAPIInfoService;
    private final NotificacionService notificacionService;

    PruebasController(
        PruebaRepository pruebaRepository, 
        EmpleadoRepository empleadoRepository, 
        InteresadoRepository interesadoRepository,         
        PosicionRepository posicionRepository,
        VehiculoRepository vehiculoRepository,
        PruebaService pruebaService,
        DistanceAPIInfoService distanceAPIInfoService,
        NotificacionService notificacionService
        ) {
        this.pruebaRepository = pruebaRepository;
        this.empleadoRepository = empleadoRepository;        
        this.interesadoRepository = interesadoRepository;        
        this.posicionRepository = posicionRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.pruebaService = pruebaService;
        this.distanceAPIInfoService = distanceAPIInfoService;
        this.notificacionService = notificacionService;
    }
    
    /**
     * Registra una nueva prueba de manejo para un vehículo y un interesado específicos.
     *
     * @param entity el objeto {@link PruebaDTO} que contiene los datos de la nueva prueba
     * @param jwt el token JWT del usuario autenticado
     * @return el objeto {@link Prueba} registrado
     * @throws ResponseStatusException con estado 404 si no se encuentra el vehículo o el interesado
     * @throws ResponseStatusException con estado 400 si el vehículo ya está en uso o el interesado está restringido
     */
    @Operation(summary = "Registrar nueva prueba", description = "Registra una nueva prueba de manejo para un vehículo y un interesado específicos")
    @PostMapping("/pruebas")
    public Prueba registrarPrueba(@RequestBody PruebaDTO entity, @AuthenticationPrincipal Jwt jwt) {        
        
        int vehiculoId = entity.vehiculoId;
        Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId);
        if (vehiculo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No se encontró el vehículo");
        }

        if (pruebaRepository.existsByVehiculoAndFechaHoraFinIsNull(vehiculo)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,            
                "El vehículo ya está siendo usado en otra prueba.");
        }

        int interesadoId = entity.interesadoId;
        Interesado interesado = interesadoRepository.findById(interesadoId);
        if (interesado == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No se encontró el interesado");
        }

        if (interesado.getRestringido()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "El interesado está restringido y no puede iniciar una nueva prueba.");
        }

        String idEntityCarSelling = jwt.getClaimAsString("idEntityCarSelling");
        Empleado emp = empleadoRepository.findByLegajo(Integer.parseInt(idEntityCarSelling));
        LocalDateTime ahora = LocalDateTime.now();
        
        Prueba pruebaNueva = Prueba.builder()
            .vehiculo(vehiculo)
            .interesado(interesado)
            .empleado(emp)
            .fechaHoraInicio(ahora)            
            .build();

        return pruebaRepository.save(pruebaNueva);        
    }


    /**
     * Obtiene todas las pruebas que están en curso (sin fecha de fin).
     *
     * @return una lista de objetos {@link Prueba} que representan las pruebas en curso.
     */
    @Operation(summary = "Pruebas en curso", description = "Obtiene todas las pruebas que están en curso (sin fecha de fin)")
    @GetMapping("/pruebas/en-curso")
    public List<Prueba> getPruebasEnCurso() {        
        return pruebaRepository.findByFechaHoraFinIsNull();
    }
    

    /**
     * Finaliza una prueba específica proporcionando comentarios.
     *
     * @param id el identificador único de la prueba a finalizar
     * @param comentarios los comentarios asociados a la finalización de la prueba
     * @return el objeto {@link Prueba} actualizado con la fecha y hora de fin y los comentarios
     * @throws ResponseStatusException con estado 404 si no se encuentra la prueba con el id proporcionado
     */
    @Operation(summary = "Finalizar prueba", description = "Finaliza una prueba específica proporcionando comentarios")
    @PutMapping("/pruebas/{id}/finalizar")
    public Prueba putFinalizarPrueba(@PathVariable String id, @RequestBody String comentarios) {        

        Prueba entity = pruebaRepository.findById(Long.valueOf(id))
            .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND,"No se encontró la prueba con id: " + id));
        entity.setComentarios(comentarios);
        entity.setFechaHoraFin(LocalDateTime.now());
        pruebaRepository.save(entity);
        return entity;
    }

    /**     
     * Verifica y registra la posición actual del vehículo respecto a las zonas permitidas y restringidas.
     * 
     * Este API realiza dos validaciones principales:
     * 1. Comprueba que el vehículo se encuentre dentro del radio permitido desde la agencia
     * 2. Valida que el vehículo no se encuentre dentro de ninguna zona geográfica restringida
     * 
     * En caso de que el vehículo incumpla alguna de estas condiciones, el API:
     * - Marca al interesado como restringido
     * - Registra una incidencia en el sistema
     * - Envía una notificación al interesado
     * - Lanza una excepción indicando la razón de la restricción
     * 
     * Para ello se apoya en el servicio DistanceAPIInfoService para obtener la informacion del
     * radio permitido y las zonas restringidas.
     * 
     * De ser necesario enviar una notificación, se utiliza el microservicio de notificaciones.
     *
     * @param entity {@link PosicionDTO} que contiene las coordenadas actuales del vehículo (latitud y longitud)
     * @param jwt el token JWT del usuario autenticado
     * @return {@link String} "OK" si la posición del vehículo es válida
     * @throws ResponseStatusException con estado 404 si no se encuentra una prueba en curso para el vehículo
     * @throws ResponseStatusException con estado 404 si no se encuentra el interesado asociado a la prueba
     * @throws ResponseStatusException con estado 400 si el vehículo está fuera del radio permitido 
     *         o dentro de una zona restringida
     */
    @Operation(summary = "Check posición vehículo", description = "Verifica y registra la posición actual del vehículo respecto a las zonas permitidas y restringidas")
    @PostMapping("/pruebas/check-posicion-vehiculo")
    public Posicion checkPosicionVehiculo(@RequestBody PosicionDTO entity, @AuthenticationPrincipal Jwt jwt) {
        // busco la prueba en curso para el vehículo
        Prueba prueba = pruebaRepository.findByVehiculo_IdAndFechaHoraFinIsNull(entity.vehiculoId);

        if (prueba == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No se encontró una prueba en curso para el vehículo con id: " + entity.vehiculoId);
        }
        
        String idEntityCarSelling = jwt.getClaimAsString("idEntityCarSelling");
        
        Interesado interesado = this.interesadoRepository.findById(Integer.parseInt(idEntityCarSelling));
        if (interesado == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No se encontró el interesado con id: " + idEntityCarSelling);
        }

        // obtengo la info de distancias y zonas desde el DistanceAPIInfoService
        JsonNode distanceAPIData = this.distanceAPIInfoService.getInfo();
        try{
            // guardo la posición
            Posicion posicionNueva = Posicion.builder()
                .vehiculo(prueba.getVehiculo())
                .latitud(entity.latitud)
                .longitud(entity.longitud)
                .fechaHora(LocalDateTime.now())
                .build();
            Posicion nuevaPosicion = this.posicionRepository.save(posicionNueva);

            pruebaService.checkPosicionVehiculoService(
                nuevaPosicion, interesado, prueba, distanceAPIData);

            return nuevaPosicion;

        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    

    /**
     * Obtiene todas las pruebas asociadas a un vehículo específico.
     *
     * @param vehiculoId el identificador único del vehículo
     * @return una lista de pruebas asociadas al vehículo
     * @throws ResponseStatusException con estado 404 si el vehículo no existe
     * @throws ResponseStatusException con estado 404 si no hay pruebas para el vehículo
     */
    @Operation(summary = "REPORTE: Pruebas por vehiculo", description = "Obtiene todas las pruebas asociadas a un vehículo específico")
    @GetMapping("/pruebas/vehiculo")
    public List<Prueba> getPruebaByVehiculo(@RequestParam(required = false) Integer vehiculoId) {
        List<Prueba> pruebas;
        if (vehiculoId != null) {
            Vehiculo vehiculo = this.vehiculoRepository.findById(vehiculoId);
            if (vehiculo == null) {
                throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontró el vehículo");
                }
                pruebas = pruebaRepository.findByVehiculo_Id(vehiculoId);
        }else{
            pruebas = pruebaRepository.findAll();
        }

        if (pruebas == null || pruebas.size() == 0) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No se encontraron pruebas");
        }
        return pruebas;
    }


    /**
     * Calcula la distancia total recorrida por un vehículo durante un período específico.
     * 
     * Este método obtiene todas las pruebas realizadas por un vehículo dentro del rango de fechas
     * especificado y suma la distancia recorrida entre cada par de posiciones consecutivas
     * utilizando la fórmula de Haversine.
     *
     * @param vehiculoId el identificador único del vehículo para el cual se desea calcular la distancia
     * @param desde la fecha y hora de inicio del período en formato ISO 8601 (YYYY-MM-DDTHH:MM:SS)
     * @param hasta la fecha y hora de fin del período en formato ISO 8601 (YYYY-MM-DDTHH:MM:SS)
     * 
     * @return un objeto {@link DistanciaRecorridaDTO} que contiene:
     *         - la distancia total recorrida en la unidad calculada por Haversine (kilómetros)
     *         - el identificador del vehículo
     *         - la fecha y hora de inicio del período
     *         - la fecha y hora de fin del período
     * 
     * @throws ResponseStatusException con código 400 (BAD_REQUEST) si las fechas no pueden 
     *         ser parseadas en formato ISO 8601
     * @throws ResponseStatusException con código 404 (NOT_FOUND) si no se encuentran pruebas
     *         para el vehículo en el período indicado
     * 
     * @see HaversineCalculator#calcularDistancia(double, double, double, double)
     */
    @Operation(summary = "REPORTE: Distancia recorrida por periodo", description = "Calcula la distancia total recorrida por un vehículo en pruebas durante un período específico")
    @GetMapping("/pruebas/distancia-recorridas-por-periodo")
    public DistanciaRecorridaDTO getDistanciaRecorridaEnPeriodo(
        @RequestParam int vehiculoId,
        @RequestParam String desde,
        @RequestParam String hasta) {        

        LocalDateTime startDate;
        LocalDateTime endDate;

        try{
            startDate = LocalDateTime.parse(desde);
            endDate = LocalDateTime.parse(hasta); 
        } catch(DateTimeException e){
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Error al parsear las fechas. Use el formato ISO 8601: YYYY-MM-DDTHH:MM:SS");
        }


        List<Prueba> pruebas = pruebaRepository.findByVehiculo_IdAndFechaHoraInicioGreaterThanEqualAndFechaHoraFinLessThanEqual(vehiculoId, startDate, endDate);
        if (pruebas == null || pruebas.size() == 0) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No se encontraron pruebas para el vehículo en el período indicado");
        }

        double distanciaTotal = 0.0;
        for (Prueba prueba : pruebas) {
            List<Posicion> posiciones = this.posicionRepository.findByVehiculoAndFechaHoraBetween(
                prueba.getVehiculo(), 
                prueba.getFechaHoraInicio(), 
                prueba.getFechaHoraFin()
            );

            for (int i = 1; i < posiciones.size(); i++) {                
                Posicion pos1 = posiciones.get(i - 1);
                Posicion pos2 = posiciones.get(i);                
                double distancia = HaversineCalculator.calcularDistancia(
                    pos1.getLatitud(), pos1.getLongitud(), 
                    pos2.getLatitud(), pos2.getLongitud()
                );
                distanciaTotal += distancia;
            }        
        }

        return new DistanciaRecorridaDTO(
            distanciaTotal, 
            vehiculoId, 
            startDate, 
            endDate
        );
    }

    /**
     * Obtiene la prueba en curso asociada al interesado autenticado.
     *
     * @param jwt el token JWT del usuario autenticado
     * @return el objeto {@link Prueba} en curso del interesado
     * @throws ResponseStatusException con estado 404 si no se encuentra el interesado
     * @throws ResponseStatusException con estado 404 si no se encuentra una prueba en curso para el interesado
     */
    @Operation(summary = "Obtener prueba en curso del interesado", description = "Obtiene la prueba en curso asociada al interesado autenticado")
    @GetMapping("/pruebas/prueba-en-curso")
    public Prueba getPruebaEnCurso(@AuthenticationPrincipal Jwt jwt) {
        String idEntityCarSelling = jwt.getClaimAsString("idEntityCarSelling");

        Interesado interesado = this.interesadoRepository.findById(Integer.parseInt(idEntityCarSelling));
        if (interesado == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No se encontró el interesado con id: " + idEntityCarSelling);
        }

        Prueba prueba = this.pruebaRepository.findByInteresadoAndFechaHoraFinIsNull(interesado);
        if (prueba == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No se encontró una prueba en curso para el interesado con id: " + idEntityCarSelling);
        }

        return prueba;
    }
    
    
    
}
