package car.selling.pruebas.models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import car.selling.pruebas.models.Prueba;
import car.selling.pruebas.models.Posicion;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "incidencias")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Incidencia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_prueba")
    private Prueba prueba;    
    
    private boolean esZonaRestringida;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_posicion")
    private Posicion posicion;

    @Column(name = "mensaje", nullable = false)
    private double distanciaAgenciaKm;

    @Column(name = "fecha_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaHora;


    public Incidencia(String mensaje, Prueba prueba) {                
        this.prueba = prueba;
    }
}
