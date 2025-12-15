'use client'
import { useEffect, useState } from "react";
import PruebaService from "@/src/services/PruebaService";
import { PosicionData } from "@/src/models/PosicionData";
import { 
    TextField,
    Button,
    InputLabel,
    Select,
    MenuItem,
    FormControl,
    SelectChangeEvent,
    Paper
} from "@mui/material";
import { useContext } from "react";
import GlobalContext from "@/src/contexts/GlogalContext";

import HttpError from "@/src/services/HttpError";

export default function ActualizarPosicion() {

    const { setToastProps } = useContext(GlobalContext);

    const { actualizarPosicionVehiculo, getPruebaEnCurso } = PruebaService();
    const [posicion, setPosicion] = useState<PosicionData>({
        latitud: undefined,
        longitud: undefined,
        fechaHora: new Date().toISOString(),
        vehiculoId: undefined
    });

    const [pruebaEnCurso, setPruebaEnCurso] = useState<any>(null);

    useEffect(() => {
        const fetchPruebaEnCurso = async () => {
            const prueba = await getPruebaEnCurso();
            setPruebaEnCurso(prueba);
        };
        fetchPruebaEnCurso();
    }, []);

    useEffect(() => {
        const actualizarPosicion = async () => {
            if (navigator.geolocation && pruebaEnCurso) {
                navigator.geolocation.getCurrentPosition(async (position) => {
                    const nuevaPosicion: PosicionData = {
                        latitud: position.coords.latitude,
                        longitud: position.coords.longitude,
                        fechaHora: new Date().toISOString(),
                        vehiculoId: pruebaEnCurso?.vehiculo.id
                    };
                    setPosicion(nuevaPosicion);        
                });
            }
        };
        actualizarPosicion();
    },[pruebaEnCurso]);   

    const onSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            if(posicion.vehiculoId){
                await actualizarPosicionVehiculo(posicion);
                console.log("Posicion actualizada:", posicion);
            } else {
                console.error("No hay prueba en curso o vehiculoId es undefined");
                setToastProps({
                    open: true,
                    message: `No hay prueba en curso o vehiculoId es undefined.`,
                    severity: "error"
                });
            }
        } catch (err){
            if (err instanceof HttpError) {
                console.error("Error HTTP actualizando posicion:", err);
                setToastProps({
                    open: true,
                    message: `Error actualizando posicion: ${err.status} - ${err.body?.message || err.message}`,
                    severity: "error"
                });
            } else {
                console.error("Error desconocido actualizando posicion:", err);
                setToastProps({
                    open: true,
                    message: `Error desconocido actualizando posicion.`,
                    severity: "error"
                });
            }
        }
    }


    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setPosicion((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    }

    return (
        <>
        <h1>Actualizar posicion vehiculo</h1>
        <Paper elevation={3} style={{ padding: '16px', marginBottom: '16px' }}>
            <p>Latitud: {posicion.latitud}</p>
            <p>Longitud: {posicion.longitud}</p>
            <p>Fecha y Hora: {posicion.fechaHora}</p>
            <p>Vehiculo ID: {posicion.vehiculoId}</p>
        </Paper>
        <form>        
            <TextField
                type="number"
                name="latitud"
                value={posicion.latitud || ''}
                onChange={handleChange}
                label="Latitud"
                variant="outlined"                
                margin="normal"
                fullWidth
            />

            <TextField
                type="number"
                name="longitud"
                value={posicion.longitud || ''}
                onChange={handleChange}
                label="Longitud"
                variant="outlined"                
                margin="normal"
                fullWidth
            />            
            <TextField
                type="datetime-local"
                name="fechaHora"
                value={posicion.fechaHora ? new Date(posicion.fechaHora).toISOString().slice(0,16) : ''}
                onChange={handleChange}
                label="Fecha y Hora"
                variant="outlined"                
                margin="normal"
                fullWidth
            />
            <Button
                type="button"
                onClick={onSubmit}
                variant="contained"                
            >
                Enviar Posicion
            </Button>
                    
        </form>
        </>
    );}

        