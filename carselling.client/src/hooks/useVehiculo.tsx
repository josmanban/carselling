'use client'

import VehiculoService from "@/src/services/VehiculoService";
import { Vehiculo } from "@/src/models/Vehiculo";
import { AltaVehiculo } from "@/src/models/AltaVehiculo";

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

    const registrarVehiculo = async (vehiculo: AltaVehiculo): Promise<Vehiculo> => {
        try {
            return await service.registrarVehiculo(vehiculo);
        } catch (error) {
            console.error("Error registering vehiculo:", error);
            throw error;
        }
    };

    return {
        getVehiculos,
        registrarVehiculo
    };
}

export default useVehiculo;