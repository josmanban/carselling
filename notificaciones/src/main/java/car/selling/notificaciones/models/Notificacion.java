package car.selling.notificaciones.models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Data 
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notificaciones")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Notificacion {    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensaje;

    @Column(name = "telefono_contacto", length = 20, nullable = false)
    private String telefonoContacto;

    @Column(name = "fecha_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaHora;

    @Builder
    public Notificacion(String mensaje, String telefonoContacto, LocalDateTime fechaHora) {
        this.mensaje = mensaje;
        this.telefonoContacto = telefonoContacto;
        this.fechaHora = fechaHora;
    }    
}