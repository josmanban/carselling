import { Posicion } from "./Posicion";
import { Prueba } from "./Prueba";

export interface Incidencia{
    id: number;
    prueba: Prueba;
    distanciaAgenciaKm: number;
    esZonaRestringida: boolean;
    fechaHora: string;
    posicion: Posicion
}