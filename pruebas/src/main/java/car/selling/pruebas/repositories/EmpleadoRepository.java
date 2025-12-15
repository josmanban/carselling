package car.selling.pruebas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import car.selling.pruebas.models.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Empleado findByLegajo(int legajo);
}
