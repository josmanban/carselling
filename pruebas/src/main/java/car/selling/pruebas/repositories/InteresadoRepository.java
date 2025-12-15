package car.selling.pruebas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import car.selling.pruebas.models.Interesado;

public interface InteresadoRepository extends JpaRepository<Interesado, Long> {
    public Interesado findById(long id);
}
