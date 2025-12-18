package car.selling.pruebas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import car.selling.pruebas.models.Vehiculo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    public Vehiculo findById(int id);
}
