package car.selling.pruebas.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "pruebas")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Prueba {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_vehiculo")
    private Vehiculo vehiculo;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_interesado")
    private Interesado interesado;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_empleado")
    private Empleado empleado;

    @Column(name = "fecha_hora_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaHoraInicio;

    @Column(name = "fecha_hora_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaHoraFin;

    @Column(name = "comentarios", length = 500)
    private String comentarios;

    public Prueba(Vehiculo vehiculo, Interesado interesado, Empleado empleado, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, String comentarios) {        
        this.vehiculo = vehiculo;
        this.interesado = interesado;
        this.empleado = empleado;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.comentarios = comentarios;
    }

    public Prueba(Vehiculo vehiculo, Interesado interesado) {        
        this.vehiculo = vehiculo;
        this.interesado = interesado;        
    }

    public Prueba(Vehiculo vehiculo, Empleado empleado, Interesado interesado, LocalDateTime fechaHoraInicio) {        
        this.vehiculo = vehiculo;
        this.interesado = interesado;
        this.empleado = empleado;
        this.fechaHoraInicio = fechaHoraInicio;        
    }
}