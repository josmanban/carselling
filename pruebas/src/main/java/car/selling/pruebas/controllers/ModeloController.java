package car.selling.pruebas.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import car.selling.pruebas.services.ModeloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Modelo Controller", description = "Operaciones relacionadas con los modelos de vehículos")
@RestController
public class ModeloController {
    private final ModeloService modeloService;

    public ModeloController(ModeloService modeloService) {
        this.modeloService = modeloService;
    }

    /**
     * Obtiene todos los modelos de vehículos.
     * @return
     */
    @Operation(summary = "Obtener todos los modelos", description = "Devuelve una lista de todos los modelos de vehículos disponibles.")
    @GetMapping("/modelos")
    public Iterable<car.selling.pruebas.models.Modelo> getAllModelos() {
        return modeloService.findAll();
    }
}
