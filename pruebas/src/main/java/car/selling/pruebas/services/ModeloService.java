package car.selling.pruebas.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import car.selling.pruebas.repositories.ModeloRepository;
import car.selling.pruebas.models.Modelo;

@Service
public class ModeloService {
    private final ModeloRepository modeloRepository;

    @Autowired
    public ModeloService(ModeloRepository modeloRepository) {
        this.modeloRepository = modeloRepository;
    }

    public Iterable<car.selling.pruebas.models.Modelo> findAll() {
        return modeloRepository.findAll();
    }

    public Modelo findById(Integer id) {
        return modeloRepository.findById(id);
    }
}