package car.selling.pruebas.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import car.selling.pruebas.dataTransferObjects.PruebaDTO;
import car.selling.pruebas.models.Vehiculo;
import car.selling.pruebas.services.VehiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.DELETE;

import org.springframework.web.server.ResponseStatusException;

import java.beans.ConstructorProperties;
import java.io.Serial;

import org.eclipse.angus.mail.util.QPEncoderStream;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import car.selling.pruebas.dataTransferObjects.VehiculoDTO;
import car.selling.pruebas.services.ModeloService;


@Tag(name = "Vehículo Controller", description = "Operaciones relacionadas con los vehículos")
@RestController
public class VehiculoController {

    private final VehiculoService vehiculoService;
    private final ModeloService modeloService;

    public VehiculoController(VehiculoService vehiculoService, ModeloService modeloService) {
        this.vehiculoService = vehiculoService;
        this.modeloService = modeloService;
    }   
    
    /**
     * Obtiene todos los vehículos.
     * @return
     */
    @Operation(summary = "Obtener todos los vehículos", description = "Devuelve una lista de todos los vehículos disponibles.")
    @GetMapping("/vehiculos")
    public Iterable<Vehiculo> getAllVehiculos() {
        return vehiculoService.findAll();
    }

    /**
     * Registra un nuevo vehículo.
     * @param v
     * @return
     */
    @Operation(summary = "Registrar un nuevo vehículo", description = "Registra un nuevo vehículo en el sistema.")
    @PostMapping("/vehiculos")
    public Vehiculo registrarVehiculo(@RequestBody VehiculoDTO entity) {
        try{
            Vehiculo vehiculo = new Vehiculo();
            vehiculo.setPatente(entity.patente);
            vehiculo.setModelo(modeloService.findById(entity.modeloId));
            return vehiculoService.crear(vehiculo);
        }catch(Exception e){
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    
}