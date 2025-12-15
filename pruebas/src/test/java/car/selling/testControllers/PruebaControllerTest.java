package car.selling.testControllers;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.geo.Distance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import car.selling.pruebas.models.Empleado;
import car.selling.pruebas.models.Incidencia;
import car.selling.pruebas.models.Interesado;
import car.selling.pruebas.models.Marca;
import car.selling.pruebas.models.Modelo;
import car.selling.pruebas.models.Posicion;
import car.selling.pruebas.models.Prueba;
import car.selling.pruebas.models.Vehiculo;
import car.selling.pruebas.repositories.EmpleadoRepository;
import car.selling.pruebas.repositories.IncidenciaRepository;
import car.selling.pruebas.repositories.InteresadoRepository;
import car.selling.pruebas.repositories.MarcaRepository;
import car.selling.pruebas.repositories.ModeloRepository;
import car.selling.pruebas.repositories.PosicionRepository;
import car.selling.pruebas.repositories.PruebaRepository;
import car.selling.pruebas.repositories.VehiculoRepository;
import car.selling.pruebas.services.DistanceAPIInfoService;
import car.selling.pruebas.services.NotificacionService;
import car.selling.pruebas.services.PruebaService;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PruebaControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mockMvc;    

    @Autowired
    private PruebaRepository pruebaRepository;
    @Autowired
    private EmpleadoRepository empleadoRepository;    
    @Autowired
    private InteresadoRepository interesadoRepository;
    @Autowired
    private IncidenciaRepository incidenciaRepository;
    @Autowired
    private PosicionRepository posicionRepository;
    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private MarcaRepository marcaRepository;
    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private PruebaService pruebaService;

    @MockBean
    private DistanceAPIInfoService distanceAPIInfoService;

    @MockBean
    private NotificacionService notificacionService;

    JsonNode distanceAPIData;
    
    @BeforeEach
    public void setup() {
        incidenciaRepository.deleteAll();        
        pruebaRepository.deleteAll();
        posicionRepository.deleteAll();
        vehiculoRepository.deleteAll();
        empleadoRepository.deleteAll();
        interesadoRepository.deleteAll();
        modeloRepository.deleteAll();
        marcaRepository.deleteAll();

        jdbcTemplate.execute("ALTER TABLE pruebas ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE posiciones ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE vehiculos ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE empleados ALTER COLUMN legajo RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE interesados ALTER COLUMN id RESTART WITH 1");        
        jdbcTemplate.execute("ALTER TABLE incidencias ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE modelos ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE marcas ALTER COLUMN id RESTART WITH 1");

        pruebaRepository.flush();
        empleadoRepository.flush();        
        interesadoRepository.flush();
        incidenciaRepository.flush();
        posicionRepository.flush();
        marcaRepository.flush();
        modeloRepository.flush();
        vehiculoRepository.flush();


        Marca marca1 = Marca.builder().nombre("Ford").build();
        Marca marca2 = Marca.builder().nombre("Chevrolet").build();
        marcaRepository.save(marca1);
        marcaRepository.save(marca2);

        Modelo modelo1 = Modelo.builder()
            .marca(marca1)
            .descripcion("Focus")
            .build();
        Modelo modelo2 = Modelo.builder()
            .marca(marca2)
            .descripcion("Fiesta")
            .build();        
        modeloRepository.save(modelo1);
        modeloRepository.save(modelo2);
        
        Vehiculo vehiculo1 = Vehiculo.builder()
        .patente("ABC123")
        .modelo(modelo1)
        .build();
        
        Vehiculo vehiculo2 = Vehiculo.builder()
        .patente("DEF456")
        .modelo(modelo2)
        .build();
        vehiculoRepository.save(vehiculo1);
        vehiculoRepository.save(vehiculo2);

        Vehiculo vehiculo3 = Vehiculo.builder()
        .patente("GHI789")
        .modelo(modelo1)
        .build();
        vehiculoRepository.save(vehiculo3);
        
        Empleado empleado1 = Empleado.builder()            
        .nombre("Test")
        .apellido("Empleado")
        .telefonoContacto("123456789")
        .build();
        
        Empleado empleado2 = Empleado.builder()            
        .nombre("Otro")
        .apellido("Empleado")
        .telefonoContacto("987654321")
        .build();
        empleadoRepository.save(empleado1);
        empleadoRepository.save(empleado2);
        
        Interesado interesado1 = Interesado.builder()
        .tipoDocumento("DNI")
        .documento("12345678")
        .nombre("Test")
        .apellido("Interesado")
        .nroLicencia("A1234567")
        .telefonoContacto("987654321")
        .fechaVencimientoLicencia(java.time.LocalDate.of(2025, 12, 31))
        .restringido(false)
        .build();
        
        Interesado interesado2 = Interesado.builder()
        .tipoDocumento("DNI")
        .documento("87654321")
        .nombre("Otro")
        .apellido("Interesado")
        .nroLicencia("B7654321")
        .telefonoContacto("987654322")
        .fechaVencimientoLicencia(java.time.LocalDate.of(2024, 11, 30))
        .restringido(false)
        .build();

        Interesado interesado3 = Interesado.builder()
        .tipoDocumento("DNI")
        .documento("11223344")
        .nombre("Otro")
        .apellido("Interesado")
        .nroLicencia("C1122334")
        .telefonoContacto("987654323")
        .fechaVencimientoLicencia(java.time.LocalDate.of(2023, 10, 15))                
        .restringido(false)
        .build();

        Interesado interesado4 = Interesado.builder()
        .tipoDocumento("DNI")
        .documento("44332211")
        .nombre("Restringido")
        .apellido("Interesado")
        .nroLicencia("D4433221")
        .telefonoContacto("987654324")
        .fechaVencimientoLicencia(java.time.LocalDate.of(2023, 9, 30))
        .restringido(true)
        .build();

        interesadoRepository.save(interesado1);
        interesadoRepository.save(interesado2);
        interesadoRepository.save(interesado3);
        interesadoRepository.save(interesado4);             
        
        Prueba prueba1 = Prueba.builder()
        .vehiculo(vehiculo1)
        .empleado(empleado1)
        .interesado(interesado1)
        .fechaHoraInicio(java.time.LocalDateTime.of(2023, 10, 1, 10, 0))
        .build();
        pruebaRepository.save(prueba1);        
        
        Prueba prueba2 = Prueba.builder()
            .vehiculo(vehiculo2)
            .empleado(empleado1)
            .interesado(interesado2)
            .fechaHoraInicio(java.time.LocalDateTime.of(2023, 10, 2, 11, 30))
            .build();
        pruebaRepository.save(prueba2);

        Prueba finalizada = Prueba.builder()
            .vehiculo(vehiculo3)
            .empleado(empleado2)
            .interesado(interesado3)
            .fechaHoraInicio(java.time.LocalDateTime.of(2023, 9, 20, 10, 0))
            .fechaHoraFin(java.time.LocalDateTime.of(2023, 9, 20, 11, 0))
            .comentarios("Prueba finalizada")
            .build();
        pruebaRepository.save(finalizada);

        Posicion pos1 = Posicion.builder()
            .vehiculo(vehiculo3)
            .latitud(-34.6037)
            .longitud(-58.3816)
            .fechaHora(java.time.LocalDateTime.of(2023, 9, 20, 10, 20))
            .build();
        posicionRepository.save(pos1);

        Posicion pos2 = Posicion.builder()
            .vehiculo(vehiculo3)
            .latitud(-34.6040)
            .longitud(-58.3820)
            .fechaHora(java.time.LocalDateTime.of(2023, 9, 20, 10, 40))
            .build();
        posicionRepository.save(pos2);

        Posicion pos3 = Posicion.builder()
            .vehiculo(vehiculo3)
            .latitud(-34.6045)
            .longitud(-58.3825)
            .fechaHora(java.time.LocalDateTime.of(2023, 9, 20, 11, 0))
            .build();
        posicionRepository.save(pos3);        
        
        
        Map<String, Object> distanceAPIMap = new HashMap<>();

        Map<String, Double> coordenadasAgencia = new HashMap<>();
        coordenadasAgencia.put("lat", 42.50886738457441);
        coordenadasAgencia.put("lon", 1.5347139324337429);
        distanceAPIMap.put("coordenadasAgencia", coordenadasAgencia);

        distanceAPIMap.put("radioAdmitidoKm", 5);

        List<Map<String, Object>> zonasRestringidas = new ArrayList<>();

        Map<String, Object> zona1 = new HashMap<>();
        Map<String, Double> noroeste1 = new HashMap<>();
        noroeste1.put("lat", 42.5100061756744);
        noroeste1.put("lon", 1.5366548639320794);
        Map<String, Double> sureste1 = new HashMap<>();
        sureste1.put("lat", 42.50874384583355);
        sureste1.put("lon", 1.5387755676026835);
        zona1.put("noroeste", noroeste1);
        zona1.put("sureste", sureste1);
        
        Map<String, Object> zona2 = new HashMap<>();
        Map<String, Double> noroeste2 = new HashMap<>();
        noroeste2.put("lat", 42.507647709544536);
        noroeste2.put("lon", 1.5341898505922056);
        Map<String, Double> sureste2 = new HashMap<>();
        sureste2.put("lat", 42.50724930962572);
        sureste2.put("lon", 1.5378015588544913);
        zona2.put("noroeste", noroeste2);
        zona2.put("sureste", sureste2);

        Map<String, Object> zona3 = new HashMap<>();
        Map<String, Double> noroeste3 = new HashMap<>();
        noroeste3.put("lat", 42.5103818437401);
        noroeste3.put("lon", 1.529033233491418);
        Map<String, Double> sureste3 = new HashMap<>();
        sureste3.put("lat", 42.50964884074852);
        sureste3.put("lon", 1.5321785196039148);
        zona3.put("noroeste", noroeste3);
        zona3.put("sureste", sureste3);

        zonasRestringidas.add(zona1);
        zonasRestringidas.add(zona2);
        zonasRestringidas.add(zona3);

        distanceAPIMap.put("zonasRestringidas", zonasRestringidas);

        ObjectMapper mapper = new ObjectMapper();
        this.distanceAPIData = mapper.valueToTree(distanceAPIMap);

    }    

    @Test
    public void testGetPruebasEnCurso() throws Exception {
        mockMvc.perform(get("/pruebas/en-curso")
        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_empleado"))))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(content().json("[{\"id\":1,\"vehiculo\":{\"id\":1,\"patente\":\"ABC123\",\"modelo\":{\"id\":1,\"marca\":{\"id\":1,\"nombre\":\"Ford\"},\"descripcion\":\"Focus\"}},\"interesado\":{\"id\":1,\"tipoDocumento\":\"DNI\",\"documento\":\"12345678\",\"nombre\":\"Test\",\"apellido\":\"Interesado\",\"restringido\":false,\"nroLicencia\":\"A1234567\",\"fechaVencimientoLicencia\":\"2025-12-31\"},\"empleado\":{\"legajo\":1,\"nombre\":\"Test\",\"apellido\":\"Empleado\",\"telefonoContacto\":\"123456789\"},\"fechaHoraInicio\":\"2023-10-01T10:00:00\",\"fechaHoraFin\":null,\"comentarios\":null},{\"id\":2,\"vehiculo\":{\"id\":2,\"patente\":\"DEF456\",\"modelo\":{\"id\":2,\"marca\":{\"id\":2,\"nombre\":\"Chevrolet\"},\"descripcion\":\"Fiesta\"}},\"interesado\":{\"id\":2,\"tipoDocumento\":\"DNI\",\"documento\":\"87654321\",\"nombre\":\"Otro\",\"apellido\":\"Interesado\",\"restringido\":false,\"nroLicencia\":\"B7654321\",\"fechaVencimientoLicencia\":\"2024-11-30\"},\"empleado\":{\"legajo\":1,\"nombre\":\"Test\",\"apellido\":\"Empleado\",\"telefonoContacto\":\"123456789\"},\"fechaHoraInicio\":\"2023-10-02T11:30:00\",\"fechaHoraFin\":null,\"comentarios\":null}]"));
    }        
    
    @Test
    public void testGetPruebasEnCurso_NoContent() throws Exception {
        pruebaRepository.deleteAll();
        pruebaRepository.flush();
        mockMvc.perform(get("/pruebas/en-curso")
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_empleado"))))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(content().json("[]"));          
    }

    @Test
    public void testGetPruebasByVehiculo_Success() throws Exception {
        mockMvc.perform(get("/pruebas/vehiculo?vehiculoId=1")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_admin"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("[{\"id\":1,\"vehiculo\":{\"id\":1,\"patente\":\"ABC123\",\"modelo\":{\"id\":1,\"marca\":{\"id\":1,\"nombre\":\"Ford\"},\"descripcion\":\"Focus\"}},\"interesado\":{\"id\":1,\"tipoDocumento\":\"DNI\",\"documento\":\"12345678\",\"nombre\":\"Test\",\"apellido\":\"Interesado\",\"restringido\":false,\"nroLicencia\":\"A1234567\",\"fechaVencimientoLicencia\":\"2025-12-31\"},\"empleado\":{\"legajo\":1,\"nombre\":\"Test\",\"apellido\":\"Empleado\",\"telefonoContacto\":\"123456789\"},\"fechaHoraInicio\":\"2023-10-01T10:00:00\",\"fechaHoraFin\":null,\"comentarios\":null}]"));
    }

    @Test
    public void testGetPruebasByVehiculo_Fail_VehiculoNoExistente() throws Exception {
        mockMvc.perform(get("/pruebas/vehiculo?vehiculoId=999")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_admin"))))
                .andExpect(status().isNotFound())
                .andExpect(result -> 
                    assertEquals(
                        "No se encontró el vehículo", 
                        result.getResponse().getErrorMessage()
                    )
                );
    }
    
    @Test
    public void testRegistrarPrueba_Success() throws Exception {
        // Prepare request body
        String requestBody = "{"
            + "\"vehiculoId\":3,"            
            + "\"interesadoId\":3"            
            + "}";

        mockMvc.perform(MockMvcRequestBuilders
                    .post("/pruebas")
                    .contentType("application/json")
                    .content(requestBody)
                    .with(jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_empleado"))
                        .jwt(jwt -> jwt.claim("idEntityCarSelling", 1))
                    )
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.vehiculo.id").value(3))
            .andExpect(jsonPath("$.empleado.legajo").value(1))
            .andExpect(jsonPath("$.interesado.id").value(3));
    }
    @Test
    public void testRegistrarPrueba_Fail_InvalidVehiculo() throws Exception {
        String requestBody = "{"
        + "\"vehiculoId\":999,"        
        + "\"interesadoId\":1"
        + "}";
        
        mockMvc.perform(MockMvcRequestBuilders
        .post("/pruebas")
        .contentType("application/json")
        .content(requestBody)
        .with(jwt()
            .authorities(new SimpleGrantedAuthority("ROLE_empleado"))
            .jwt(jwt -> jwt.claim("idEntityCarSelling", 1))
        ))        
        .andExpect(status().isNotFound())
        .andExpect(result ->
            assertEquals(
                "No se encontró el vehículo",
                result.getResponse().getErrorMessage()
                )
            );
    }    
    

    @Test
    public void testRegistrarPrueba_Fail_InvalidInteresado() throws Exception {
        String requestBody = "{"
            + "\"vehiculoId\":3,"            
            + "\"interesadoId\":999"
            + "}";

        mockMvc.perform(MockMvcRequestBuilders
                    .post("/pruebas")
                    .contentType("application/json")
                    .content(requestBody)
                    .with(jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_empleado"))
                        .jwt(jwt -> jwt.claim("idEntityCarSelling", 1))
                    ))
            .andExpect(status().isNotFound())
            .andExpect(result ->
                assertEquals(
                    "No se encontró el interesado",
                    result.getResponse().getErrorMessage()
                )
            );
    }

    @Test
    public void testRegistrarPrueba_Fail_VehiculoEnOtraPrueba() throws Exception {
        String requestBody = "{"
            + "\"vehiculoId\":1,"            
            + "\"interesadoId\":1"
            + "}";

        mockMvc.perform(MockMvcRequestBuilders
                    .post("/pruebas")
                    .contentType("application/json")
                    .content(requestBody)
                    .with(jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_empleado"))
                        .jwt(jwt -> jwt.claim("idEntityCarSelling", 1))
                    ))
            .andExpect(status().isBadRequest())
            .andExpect(result ->
                assertEquals(
                    "El vehículo ya está siendo usado en otra prueba.",
                    result.getResponse().getErrorMessage()
                )
            );
    }

    @Test
    public void testRegistrarPrueba_Fail_InteresadoRestringido() throws Exception {
        Interesado interesado = this.interesadoRepository.findById(3);        
        interesado.setRestringido(true);
        this.interesadoRepository.save(interesado);
        
        String requestBody = "{"
            + "\"vehiculoId\":3,"            
            + "\"interesadoId\":4"
            + "}";

        mockMvc.perform(MockMvcRequestBuilders
                    .post("/pruebas")
                    .contentType("application/json")
                    .content(requestBody)
                    .with(jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_empleado"))
                        .jwt(jwt -> jwt.claim("idEntityCarSelling", 1))
                    ))
            .andExpect(status().isBadRequest())
            .andExpect(result ->
                assertEquals(
                    "El interesado está restringido y no puede iniciar una nueva prueba.",
                    result.getResponse().getErrorMessage()
                )
            );
    }

    @Test
    public void testPutFinalizarPrueba_Success() throws Exception {        
        String comentarios = "Prueba finalizada correctamente";
        mockMvc.perform(put("/pruebas/1/finalizar")
                .contentType("text/plain")
                .content(comentarios)
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_empleado"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.comentarios").value(comentarios))
            .andExpect(jsonPath("$.fechaHoraFin").isNotEmpty());
    }

    @Test
    public void testPutFinalizarPrueba_Fail_NotFound() throws Exception {
        String comentarios = "Intento finalizar prueba inexistente";
        mockMvc.perform(put("/pruebas/999/finalizar")
                .contentType("text/plain")
                .content(comentarios)
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_empleado"))))
            .andExpect(status().isNotFound())
            .andExpect(result ->
                assertEquals(
                    "No se encontró la prueba con id: 999",
                    result.getResponse().getErrorMessage()
                )
            );
    }

    @Test
    public void testGetDistanciaRecorridaEnPeriodo_Success() throws Exception {
        // Prueba finalizada tiene id=3, vehiculo3 tiene id=3
        // Posiciones para vehiculo3 están en setup, fechas: 2023-09-20 10:20, 10:40, 11:00
        // Periodo: 2023-09-20T10:00:00 a 2023-09-20T11:00:00 (incluye todas posiciones)

        String desde = "2023-09-20T10:00:00";
        String hasta = "2023-09-20T11:20:00";

        mockMvc.perform(get("/pruebas/distancia-recorridas-por-periodo")
                .param("vehiculoId", "3")
                .param("desde", desde)
                .param("hasta", hasta)
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_admin"))))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.distanciaRecorrida").value(0.12153695579627621))            
            .andExpect(jsonPath("$.vehiculoId").value(3))
            .andExpect(jsonPath("$.desde").value(desde))
            .andExpect(jsonPath("$.hasta").value(hasta));
    }

    @Test
    public void testGetDistanciaRecorridaEnPeriodo_Fail_PruebaNoExiste() throws Exception {
        String desde = "2023-09-20T10:00:00";
        String hasta = "2023-09-20T11:00:00";

        mockMvc.perform(get("/pruebas/distancia-recorridas-por-periodo")
                .param("vehiculoId", "999")
                .param("desde", desde)
                .param("hasta", hasta)
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_admin"))))
            .andExpect(status().isNotFound())
            .andExpect(result ->
                assertEquals(
                    "No se encontraron pruebas para el vehículo en el período indicado",
                    result.getResponse().getErrorMessage()
                )
            );
    }

    @Test
    public void testGetDistanciaRecorridaEnPeriodo_Fail_ErrorEnFechas() throws Exception {        
        String desde = "bad date";
        String hasta = "2023-09-19T23:59:59";

        mockMvc.perform(get("/pruebas/distancia-recorridas-por-periodo")
                .param("vehiculoId", "3")
                .param("desde", desde)
                .param("hasta", hasta)
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_admin"))))
            .andExpect(status().isBadRequest())
            .andExpect(result ->
                assertEquals(
                    "Error al parsear las fechas. Use el formato ISO 8601: YYYY-MM-DDTHH:MM:SS",
                    result.getResponse().getErrorMessage()
                )
            );
    }
    
    @Test
    public void testCheckPosicionVehiculoService_Success() throws Exception {

        when(distanceAPIInfoService.getInfo()).thenReturn(this.distanceAPIData);

        // Prepare request body
        String requestBody = "{"
            + "\"vehiculoId\":1,"            
            + "\"latitud\":42.50668804557342,"
            + "\"longitud\":1.5343202612212627"
            + "}";

        mockMvc.perform(MockMvcRequestBuilders
                    .post("/pruebas/check-posicion-vehiculo")
                    .contentType("application/json")
                    .content(requestBody)
                    .with(jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_interesado"))
                        .jwt(jwt -> jwt.claim("idEntityCarSelling", 1))
                    )
                )
                .andExpect(status().isOk());
    }
    
    @Test
    public void testCheckPosicionVehiculoService_Fail_FueraDelAreaPermitida() throws Exception {

        when(distanceAPIInfoService.getInfo()).thenReturn(this.distanceAPIData);

        // Prepare request body
        String requestBody = "{"
            + "\"vehiculoId\":1,"            
            + "\"latitud\":35.682839,"
            + "\"longitud\":139.759455"
            + "}";

        mockMvc.perform(MockMvcRequestBuilders
                    .post("/pruebas/check-posicion-vehiculo")
                    .contentType("application/json")
                    .content(requestBody)
                    .with(jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_interesado"))
                        .jwt(jwt -> jwt.claim("idEntityCarSelling", 1))
                    )
            )
            .andExpect(status().isBadRequest())
            .andExpect(result ->
                assertEquals(
                    "El vehículo está fuera del área permitida. Distancia: 10341.584680690725 km",
                    result.getResponse().getErrorMessage()
                )
            );

            when(notificacionService.enviarNotificacion(Mockito.anyString(), Mockito.anyString())).thenReturn("Notificacion enviada");

            // controlamos que una incidencia fue creada y el interesado fue puesto como restringido
            Interesado interesado = this.interesadoRepository.findById(1);
            assertEquals(true, interesado.getRestringido());
            List<Incidencia> incidencias = this.incidenciaRepository.findAll();
            assertEquals(1, incidencias.size());
    }
    
    @Test
    public void testCheckPosicionVehiculoService_Fail_ZonaRestringida() throws Exception {

        when(distanceAPIInfoService.getInfo()).thenReturn(this.distanceAPIData);

        // Prepare request body
        String requestBody = "{"
            + "\"vehiculoId\":1,"            
            + "\"latitud\":42.5095,"
            + "\"longitud\":1.5377"
            + "}";

        mockMvc.perform(MockMvcRequestBuilders
                    .post("/pruebas/check-posicion-vehiculo")
                    .contentType("application/json")
                    .content(requestBody)
                    .with(jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_interesado"))
                        .jwt(jwt -> jwt.claim("idEntityCarSelling", 1))
                    )
            )
            .andExpect(status().isBadRequest())
            .andExpect(result ->
                assertEquals(
                    "El vehículo está dentro de una zona restringida.",
                    result.getResponse().getErrorMessage()
                )
            );
            
            when(notificacionService.enviarNotificacion(Mockito.anyString(), Mockito.anyString())).thenReturn("Notificacion enviada");
            
            // controlamos que una incidencia fue creada y el interesado fue puesto como restringido
            Interesado interesado = this.interesadoRepository.findById(1);
            assertEquals(true, interesado.getRestringido());
            List<Incidencia> incidencias = this.incidenciaRepository.findAll();
            assertEquals(1, incidencias.size());    


            
    }
}
