package car.selling.pruebas.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import car.selling.pruebas.repositories.InteresadoRepository;
import car.selling.pruebas.models.Interesado;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Interesados", description = "Operaciones relacionadas con los interesados en los vehículos")
@RestController
public class InteresadoController {

    private final InteresadoRepository interesadoRepository;

    public InteresadoController(InteresadoRepository interesadoRepository) {
        this.interesadoRepository = interesadoRepository;
    }

    /**
     * Obtiene una lista de todos los interesados.
     * @return Lista de interesados.
     */
    @Operation(
        summary = "Lista de Interesados",
        description = "Obtiene una lista de todos los interesados registrados en el sistema.")
    @GetMapping("/interesados")
    public Iterable<Interesado> getAllInteresados() {
        return interesadoRepository.findAll();
    }
    
}
