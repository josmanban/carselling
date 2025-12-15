package car.selling.pruebas.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.stream.Collectors;

import car.selling.pruebas.models.Empleado;
import car.selling.pruebas.repositories.EmpleadoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Empleados", description = "Operaciones relacionadas con los empleados del sistema")
@RestController
public class EmpleadoController {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoController(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    /**
     * Obtiene una lista de todos los empleados.
     * @return Lista de empleados.
     */
    @Operation(
        summary = "Lista de Empleados",
        description = "Obtiene una lista de todos los empleados registrados en el sistema.")
    @GetMapping("/empleados")
    public Iterable<Empleado> getAllEmpleados() {
        return empleadoRepository.findAll();
    }    
}
