import { Vehiculo } from "./Vehiculo";

export interface Posicion{
    id: number;
    latitud: number;
    longitud: number;
    vehiculo?: Vehiculo;
    fechaHora: string;
}