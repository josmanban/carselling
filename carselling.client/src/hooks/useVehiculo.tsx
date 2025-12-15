import { useState } from "react";
import VehiculoService from "@/src/services/VehiculoService";
import { Vehiculo } from "@/src/models/Vehiculo";

const useVehiculo = () => {
    const service = VehiculoService();
    
    const getVehiculos = async () => {
        try {
            const vehiculos: Vehiculo[] = await service.getVehiculos();
            return vehiculos;
        } catch (error) {
            console.error("Error fetching vehiculos:", error);
            throw error;
        }
    }

    return {
        getVehiculos
    };
}

export default useVehiculo;