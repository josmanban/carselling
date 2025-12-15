package car.selling.pruebas.dataTransferObjects;

import java.time.LocalDateTime;

public class DistanciaRecorridaDTO {
    public double distanciaRecorrida;
    public int vehiculoId;
    public LocalDateTime desde;
    public LocalDateTime hasta;

    public DistanciaRecorridaDTO() {
    }
    
    public DistanciaRecorridaDTO(double distanciaRecorrida, int vehiculoId, LocalDateTime desde, LocalDateTime hasta) {
        this.distanciaRecorrida = distanciaRecorrida;
        this.vehiculoId = vehiculoId;
        this.desde = desde;
        this.hasta = hasta;
    }
}
