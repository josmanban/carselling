package car.selling.pruebas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import car.selling.pruebas.models.Modelo;

public interface ModeloRepository extends JpaRepository<Modelo, Long> {
    public Modelo findById(long id);
}
