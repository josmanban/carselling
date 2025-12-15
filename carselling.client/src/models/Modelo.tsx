import { Marca } from "./Marca";

export interface Modelo {
    id: number;
    descripcion: string;    
    marca?: Marca;
}