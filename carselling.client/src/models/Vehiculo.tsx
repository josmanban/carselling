import { Modelo } from "./Modelo";
export interface Vehiculo{
    id: number;
    patente: string;
    modelo?: Modelo;
}