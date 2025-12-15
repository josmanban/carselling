package car.selling.pruebas.controllers;

import org.springframework.web.bind.annotation.RestController;

import car.selling.pruebas.models.Incidencia;
import car.selling.pruebas.repositories.IncidenciaRepository;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;


@Tag(name = "Incidencias", description = "Operaciones relacionadas con las incidencias registradas durante las pruebas de vehículos")
@RestController
public class IncidenciaController {
    
    private final IncidenciaRepository incidenciaRepository;

    IncidenciaController(IncidenciaRepository incidenciaRepository) {
        this.incidenciaRepository = incidenciaRepository;
    }

    
    
    /**
     * Obtiene una lista de incidencias, opcionalmente filtradas por el legajo del empleado asociado a la prueba.
     * @param legajo Legajo del empleado para filtrar las incidencias (opcional).
     * @return Lista de incidencias que cumplen con el filtro especificado.
     */
    @Operation(
        summary = "Incidencias por Empleado",
        description = "Obtiene una lista de incidencias permitiendo filtrar por el legajo del empleado asociado a la prueba.")
    @GetMapping("/incidencias")
    public List<Incidencia> getIncidencias(@RequestParam(required = false) Integer legajo) {
        if(legajo == null){
            return incidenciaRepository.findAll();
        }
        return incidenciaRepository.findByPrueba_Empleado_Legajo(legajo);
    }



}
