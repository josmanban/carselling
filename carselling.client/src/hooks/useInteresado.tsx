import { useState } from "react";
import InteresadoService from "../services/InteresadoService";
import { Interesado } from "../models/Interesado";

const useInteresado = () => {
    const service = InteresadoService();
    
    const getInteresados = async () => {
        try {
            const interesados: Interesado[] = await service.getInteresados();
            return interesados;
        } catch (error) {
            console.error("Error fetching interesados:", error);
            throw error;
        }
    }

    return {
        getInteresados
    };
}

export default useInteresado;