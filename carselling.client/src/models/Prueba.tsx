import { Interesado } from "./Interesado";
import { Empleado } from "./Empleado";
import { Vehiculo } from "./Vehiculo";

export interface Prueba{
    id: number;
    interesado: Interesado;
    empleado: Empleado;
    vehiculo: Vehiculo;
    fechaHoraInicio: string;
    fechaHoraFin: string | null;
    comentarios: string | null;
}
