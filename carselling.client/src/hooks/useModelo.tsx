'use client'

import ModeloService from "@/src/services/ModeloService";
import { Modelo } from "@/src/models/Modelo";

const useModelo = () => {
    const service = ModeloService();
    
    const getModelos = async () => {
        try {
            const modelos: Modelo[] = await service.getModelos();
            return modelos;
        } catch (error) {
            console.error("Error fetching modelos:", error);
            throw error;
        }
    }

    return {
        getModelos,
    };
}

export default useModelo;