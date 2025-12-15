package car.selling.auth.controllers;

import car.selling.auth.dataTransferObjects.AdminAltaDTO;
import car.selling.auth.dataTransferObjects.EmpleadoAltaDTO;
import car.selling.auth.dataTransferObjects.InteresadoAltaDTO;
import car.selling.auth.services.KeycloakUserService;
import car.selling.pruebas.models.Empleado;
import car.selling.pruebas.models.Interesado;
import car.selling.pruebas.repositories.EmpleadoRepository;
import car.selling.pruebas.repositories.InteresadoRepository;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class AuthController {

    private final KeycloakUserService keycloakUserService;
    private final EmpleadoRepository empleadoRepository;
    private final InteresadoRepository interesadoRepository;

    public AuthController(
        KeycloakUserService keycloakUserService,
        EmpleadoRepository empleadoRepository,
        InteresadoRepository interesadoRepository
    ) {
        this.keycloakUserService = keycloakUserService;
        this.empleadoRepository = empleadoRepository;
        this.interesadoRepository = interesadoRepository;
    }

    /*
    public Empleado altaEmpleado(@RequestBody EmpleadoAltaDTO entity) {
    try {
        // ... save local entity
        String userId = keycloakUserService.createUser(...);
        keycloakUserService.assignRealmRoleToUser(userId, "empleado");
        return empleadoNuevo;
    } catch (RuntimeException e) {
        throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
            "Error creando usuario en Keycloak: " + e.getMessage(), e);
    }
}
     */

    @PostMapping("/auth/alta-empleado")
    public Empleado altaEmpleado(@RequestBody EmpleadoAltaDTO entity) {
        Empleado empleadoNuevo = Empleado.builder()
            .nombre(entity.nombre)
            .apellido(entity.apellido)
            .telefonoContacto(entity.telefonoContacto)
            .build();               
        this.empleadoRepository.save(empleadoNuevo);

        String userId = keycloakUserService.createUser(
            entity.username,
            entity.email,
            entity.password,
            entity.nombre,
            entity.apellido,
            empleadoNuevo.getLegajo()
        );

        keycloakUserService.assignRealmRoleToUser(
            userId,
            "empleado"            
        );

        return empleadoNuevo;
    }

    @PostMapping("/auth/alta-interesado")
    public Interesado altaInteresado(@RequestBody InteresadoAltaDTO entity) {
        Interesado interesadoNuevo = Interesado.builder()
            .tipoDocumento(entity.tipoDocumento)
            .documento(entity.documento)
            .nombre(entity.nombre)
            .apellido(entity.apellido)
            .nroLicencia(entity.nroLicencia)
            .telefonoContacto(entity.telefonoContacto)
            .restringido(false)
            .fechaVencimientoLicencia(entity.fechaVencimientoLicencia)
            .build();

        this.interesadoRepository.save(interesadoNuevo);

        String userId = keycloakUserService.createUser(
            entity.username,
            entity.email,
            entity.password,
            entity.nombre,
            entity.apellido,
            interesadoNuevo.getId()
        );

        keycloakUserService.assignRealmRoleToUser(
            userId,
            "interesado"            
        );

        return interesadoNuevo;
    }

    @PostMapping("/auth/alta-admin")
    public AdminAltaDTO altaAdmin(@RequestBody AdminAltaDTO entity) {
        String userId = keycloakUserService.createUser(
            entity.username,
            entity.email,
            entity.password,
            entity.nombre,
            entity.apellido,
            0
        );

        keycloakUserService.assignRealmRoleToUser(
            userId,
            "admin"
        );

        entity.password = null; // no devolver la password

        return entity;
    }   
    
    
}
