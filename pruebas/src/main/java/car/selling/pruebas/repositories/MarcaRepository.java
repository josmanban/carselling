package car.selling.pruebas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import car.selling.pruebas.models.Marca;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
    Marca findById(long id);
    
}
