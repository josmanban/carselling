package car.selling.pruebas.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "modelos")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Modelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_marca")
    private Marca marca;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    public Modelo(Marca marca, String descripcion) {        
        this.marca = marca;
        this.descripcion = descripcion;
    }
}