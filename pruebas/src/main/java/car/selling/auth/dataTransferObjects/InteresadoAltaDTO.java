package car.selling.auth.dataTransferObjects;

import java.time.LocalDate;

public class InteresadoAltaDTO {
    public String tipoDocumento;
    public String documento;
    public String nombre;
    public String apellido;    
    public String nroLicencia;
    public LocalDate fechaVencimientoLicencia;
    public String telefonoContacto;

    public String username;
    public String email;
    public String password;
}