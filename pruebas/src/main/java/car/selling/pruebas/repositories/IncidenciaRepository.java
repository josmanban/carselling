package car.selling.pruebas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import car.selling.pruebas.models.Incidencia;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {
    List<Incidencia> findByPrueba_Empleado_Legajo(Integer legajo);

    

}



