package car.selling.notificaciones.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import car.selling.notificaciones.models.Notificacion;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    
}
