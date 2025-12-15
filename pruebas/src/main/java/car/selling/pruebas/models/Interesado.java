package car.selling.pruebas.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "interesados")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Interesado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tipo_documento")
    private String tipoDocumento;

    private String documento;
    private String nombre;
    private String apellido;
    private Boolean restringido;

    @Column(name = "nro_licencia")
    private String nroLicencia;

    @Column(name = "telefono_contacto")
    private String telefonoContacto;

    @Column(name = "fecha_vencimiento_licencia")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaVencimientoLicencia;

    public Interesado(String tipoDocumento, String documento, String nombre, String apellido, Boolean restringido, String nroLicencia, LocalDate fechaVencimientoLicencia) {        
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.restringido = restringido;
        this.nroLicencia = nroLicencia;
        this.fechaVencimientoLicencia = fechaVencimientoLicencia;
    }
}