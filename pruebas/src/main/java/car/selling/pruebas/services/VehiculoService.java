package car.selling.pruebas.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import car.selling.pruebas.repositories.VehiculoRepository;
import jakarta.persistence.PersistenceContext;
import car.selling.pruebas.models.Vehiculo;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;


@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }


    @Transactional(readOnly = true)
    public Vehiculo findById(Integer id) { return entityManager.find(Vehiculo.class, id); }

    @Transactional
    public Vehiculo crear(Vehiculo v) {
        if (v.getPatente() == null || v.getPatente().trim().isEmpty()) {
            throw new IllegalArgumentException("La patente es obligatoria");
        }
        if (v.getModelo() == null || v.getModelo().getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo es obligatorio");
        }
        
        if (v.getId() == null) { entityManager.persist(v); return v; }
        return entityManager.merge(v);
    }

    @Transactional
    public Vehiculo actualizar(Long id, Vehiculo v) {
        if (v.getPatente() == null || v.getPatente().trim().isEmpty()) {
            throw new IllegalArgumentException("La patente es obligatoria");
        }
        if (v.getModelo() == null || v.getModelo().getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo es obligatorio");
        }

        Vehiculo existing = entityManager.find(Vehiculo.class, id);
        if (existing == null) {
            throw new IllegalArgumentException("Vehículo no encontrado");
        }

        entityManager.merge(v);
        return v;
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> findByPatente(Optional<String> patente) {
        return entityManager.createQuery("SELECT v FROM Vehiculo v WHERE v.patente = :p", Vehiculo.class)
                .setParameter("p", patente).getResultList();
    }

    @Transactional
    public void eliminar(Long id) {
        Vehiculo v = entityManager.find(Vehiculo.class, id);
        if (v != null) entityManager.remove(v);
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> findAll() {
        return vehiculoRepository.findAll();
    }

}
