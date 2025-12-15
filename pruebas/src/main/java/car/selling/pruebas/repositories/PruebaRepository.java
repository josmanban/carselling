package car.selling.pruebas.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import car.selling.pruebas.models.Prueba;
import car.selling.pruebas.models.Vehiculo;
import car.selling.pruebas.models.Interesado;

import java.util.List;

public interface PruebaRepository extends JpaRepository<Prueba, Long> {
    boolean existsByVehiculoAndFechaHoraFinIsNull(Vehiculo vehiculo);
    List<Prueba> findByFechaHoraFinIsNull();
    Prueba findByVehiculo_IdAndFechaHoraFinIsNull(int vehiculoId);
    Prueba findByVehiculo(Vehiculo vehiculo);
    List<Prueba> findByVehiculo_Id(int vehiculoId);
    List<Prueba> findByVehiculo_IdAndFechaHoraInicioGreaterThanEqualAndFechaHoraFinLessThanEqual(
        int vehiculoId, 
        java.time.LocalDateTime startDate, 
        java.time.LocalDateTime endDate
    );

    List<Prueba> findByFechaHoraInicioLessThanEqualAndFechaHoraFinGreaterThanEqual(        
        java.time.LocalDateTime startDate, 
        java.time.LocalDateTime endDate
    );

    Prueba findByIdAndFechaHoraFinIsNull(Vehiculo vehiculo);
    Prueba findByInteresadoAndFechaHoraFinIsNull(Interesado interesado);

}
