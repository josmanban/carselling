package car.selling.pruebas.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import car.selling.pruebas.repositories.VehiculoRepository;
import car.selling.pruebas.models.Vehiculo;

@RestController
public class VehiculoController {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoController(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }
    
    @GetMapping("/vehiculos")
    public Iterable<Vehiculo> getAllVehiculos() {
        return vehiculoRepository.findAll();
    }
}
