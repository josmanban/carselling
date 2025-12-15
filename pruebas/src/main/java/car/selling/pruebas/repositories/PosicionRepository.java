package car.selling.pruebas.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import car.selling.pruebas.models.Posicion;
import car.selling.pruebas.models.Vehiculo;

public interface PosicionRepository extends JpaRepository<Posicion, Long>  {
    List<Posicion> findByVehiculoAndFechaHoraBetween(Vehiculo vehiculo, LocalDateTime startDate, LocalDateTime endDate);
}
