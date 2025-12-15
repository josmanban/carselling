import { useState } from "react";
import IncidenciaService from "../services/IncidenciaService";
import { Incidencia } from "../models/Incidencia";
import { get } from "http";

export default function useIncidencia() {
    
    const service = IncidenciaService();
    
    const getIncidencias = async (legajo?: number) => {
        try {
            const incidencias: Incidencia[] = await service.getIncidencias(legajo);
            return incidencias;
        } catch (error) {
            console.error("Error fetching incidencias:", error);
            throw error;
        }
    }

    return {
        getIncidencias
    };
}   
    

    