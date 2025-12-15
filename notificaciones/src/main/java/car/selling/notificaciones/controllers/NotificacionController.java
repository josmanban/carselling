package car.selling.notificaciones.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import car.selling.notificaciones.models.Notificacion;
import car.selling.notificaciones.repositories.NotificacionRepository;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.http.MediaType;
import java.time.LocalDateTime;
import car.selling.notificaciones.dataTransferObjects.NotificacionBatchDTO;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Notificaciones", description = "Operaciones relacionadas con las notificaciones")
@RestController
public class NotificacionController {

    private final NotificacionRepository notificacionRepository;

    NotificacionController(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;        
    }

    /**
     * Endpoint para enviar notificaciones a una lista de teléfonos.
     * @param payload
     * @return
     */
    @Operation(summary = "Enviar Notificaciones", description = "Envía una notificación a una lista de números de teléfono proporcionados.")
    @PostMapping(path ="/notificaciones/enviar-notificaciones", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String[] enviarNotificaciones(@RequestBody NotificacionBatchDTO payload) {        
        String mensaje = payload.mensaje;

        if(mensaje == null || mensaje.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El mensaje no puede estar vacío"
            );
        }
        
        String[] telefonos = payload.telefonos;
        if(telefonos == null || telefonos.length == 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "La lista de teléfonos no puede estar vacía"
            );
        }

        List<String> telefonosEnviados = new java.util.ArrayList<>();
        for (String telefono : telefonos) {
            Notificacion notificacion = Notificacion.builder()
                .mensaje(mensaje)
                .telefonoContacto(telefono)
                .fechaHora(LocalDateTime.now())
                .build();
            notificacionRepository.save(notificacion);
            telefonosEnviados.add(telefono);
        }
        return telefonosEnviados.toArray(new String[0]);
    }
    
    /**
     * Endpoint para listar todas las notificaciones.
     * 
     * @return Lista de notificaciones.
     */
    @Operation(summary = "Listar Notificaciones", description = "Obtiene una lista de todas las notificaciones almacenadas.")
    @GetMapping("/notificaciones")
    public List<Notificacion> listarNotificaciones() {
        return notificacionRepository.findAll();
    }
    
}
