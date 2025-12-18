'use client'

import {useState, useEffect} from "react";
import AltaVehiculoForm from "@/src/components/altaVehiculo/AltaVehiculoForm";
import {Vehiculo} from "@/src/models/Vehiculo";
import VehiculoTable from "@/src/components/VehiculoTable/VehiculoTable";
import useVehiculo from "@/src/hooks/useVehiculo";
import { useContext } from "react";
import GlobalContext from "@/src/contexts/GlogalContext";
import { Typography } from "@mui/material";

export default function VehiculosPage() {
    const [vehiculos, setVehiculos] = useState<Array<Vehiculo>>([]);
    const {getVehiculos} = useVehiculo();
    const { setToastProps } = useContext(GlobalContext);

    useEffect(() => {
        fetchVehiculos();
    }, []);
    
    const fetchVehiculos = async () => {
        try {
            const data = await getVehiculos();
            setVehiculos(data);
        } catch (error) {
            console.error("Error fetching vehículos:", error);
            setToastProps({
                open: true,
                severity: 'error',
                message: 'Error al cargar los vehículos'
            });
        }
    };

    return(
        <>
            <Typography variant="h5" gutterBottom>
                Registrar Vehículo
            </Typography>
            <AltaVehiculoForm onSuccess={fetchVehiculos} />
            <VehiculoTable vehiculos={vehiculos} />
        </>
    );
}