package car.selling.notificaciones;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.geo.Distance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import car.selling.notificaciones.models.Notificacion;
import car.selling.notificaciones.repositories.NotificacionRepository;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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


import java.util.Map;
import java.util.List;
import java.util.HashMap;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")

public class NotificacionControllerTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private NotificacionRepository notificacionRepository;

    private String[] mensajesNotificaciones;

    @BeforeEach
    public void setup() {        
        notificacionRepository.deleteAll();
        jdbcTemplate.execute("ALTER TABLE notificaciones ALTER COLUMN id RESTART WITH 1");        
        notificacionRepository.flush();        

        this.mensajesNotificaciones = new String[] {
            "Su coche está listo para ser recogido.",
            "Recordatorio: su cita de mantenimiento es mañana a las 10 AM.",
            "Oferta especial: 20% de descuento en servicios de reparación este mes."
        };

        for (String mensaje : mensajesNotificaciones) {
            Notificacion notificacion = Notificacion.builder()
                .mensaje(mensaje)                
                .telefonoContacto("123456789")
                .build();
            notificacionRepository.save(notificacion);            
        }
        notificacionRepository.flush();


    }

    @Test
    public void testEnviarNotificacionesSuccess() throws Exception {
        String mensaje = "Su coche está listo para ser recogido.";
        String[] telefonos = {"123456789", "987654321", "555555555"};

        Map<String, Object> payload = new HashMap<>();
        payload.put("mensaje", mensaje);
        payload.put("telefonos", telefonos);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(payload);

        mockMvc.perform(
            post("/notificaciones/enviar-notificaciones")
            .with(jwt().authorities(
                new SimpleGrantedAuthority("ROLE_empleado")
            ))
            .contentType("application/json")
            .content(jsonPayload)
        )
        .andExpect(status().isOk())
        .andExpect(content().json("[\"123456789\",\"987654321\",\"555555555\"]"));

        List<Notificacion> notificaciones = notificacionRepository.findAll();
        assertEquals(6, notificaciones.size()); // 3 iniciales + 3 nuevas

        List<String> telefonosGuardados = new ArrayList<>();
        for (Notificacion notificacion : notificaciones) {
            telefonosGuardados.add(notificacion.getTelefonoContacto());
        }

        assert(telefonosGuardados.contains("123456789"));
        assert(telefonosGuardados.contains("987654321"));
        assert(telefonosGuardados.contains("555555555"));
    }

    @Test
    public void testListarNotificacionesSuccess() throws Exception {
        mockMvc.perform(
            get("/notificaciones")
            .with(jwt().authorities(
                new SimpleGrantedAuthority("ROLE_empleado")
            ))
            .param("param", "value") // Parámetro requerido
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(mensajesNotificaciones.length))
        .andExpect(jsonPath("$[0].mensaje").value(mensajesNotificaciones[0]))
        .andExpect(jsonPath("$[1].mensaje").value(mensajesNotificaciones[1]))
        .andExpect(jsonPath("$[2].mensaje").value(mensajesNotificaciones[2]));
    }
}
