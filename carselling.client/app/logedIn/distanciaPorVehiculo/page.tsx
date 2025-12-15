'use client'
import { useState } from "react"
import VehiculoComboBox from "@/src/components/vehiculoComboBox/VehiculoComboBox";
import { DistanciaVehiculo } from "@/src/models/DistanciaVehiculo";
import PruebaService from "@/src/services/PruebaService";
import { TextField, Button, InputLabel, Select, MenuItem, FormControl, SelectChangeEvent, Typography, Grid } from "@mui/material";
import { useContext } from "react";
import GlobalContext from "@/src/contexts/GlogalContext";
import { HttpError } from "@/src/services/HttpError";

export default function DistanciaPorVehiculo() {
    const {setToastProps} = useContext(GlobalContext);
    const { getDistanciaRecorrida } = PruebaService();
    const [data, setData] = useState<DistanciaVehiculo>({
        vehiculoId: undefined,
        desde: undefined,
        hasta: undefined
    });
    const [distancia, setDistancia] = useState<number|undefined>(undefined);
    
    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement> | SelectChangeEvent<string>) => {
        const { name, value } = e.target;
        setData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    }

    const onSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {            
            const distanciaRecorridaData: DistanciaVehiculo = await getDistanciaRecorrida(data);
            setDistancia(distanciaRecorridaData.distanciaRecorrida);            
        }catch (error) {
          setDistancia(undefined);
          console.error("Error obteniendo distancia recorrida:", error);
          if(error instanceof HttpError){
            setToastProps({
                open: true,
                severity: 'error',
                message: error.body.message
            });
            return;
          }
          setToastProps({
            open: true,
            severity: 'error',
            message: 'Error obteniendo distancia recorrida.'
            });
        }
    }

    return (
        <>
            <Typography variant="h5">
                Distancia Recorrida por Vehículo
            </Typography>            
            <Grid container mb={2} spacing={2}>
                <Grid size={6}>
                  <form 
                    className=""
                    onSubmit={onSubmit}
                  >
                    
                    <VehiculoComboBox
                      handleChange={handleChange}
                      selectedValue={data.vehiculoId}
                      hasEmptyOption={true}
                      name="vehiculoId"
                      id="vehiculoId"
                    />
                    <TextField
                      label="Desde"
                      type="datetime-local"
                      name="desde"
                      value={data.desde || ""}
                      onChange={handleChange}
                      fullWidth
                      variant="outlined"
                      margin="normal"
                      
                    />
                    <TextField
                      label="Hasta"
                      type="datetime-local"
                      name="hasta"
                      value={data.hasta || ""}
                      onChange={handleChange}
                      fullWidth
                      variant="outlined"
                      margin="normal"
                    />
                    <Button
                      type="submit"
                      variant="contained"
                    >
                      Calcular Distancia
                    </Button>
                  </form>
          
                  {distancia !== undefined && (
                    <Typography variant="h6" mt={2}>
                      Distancia Recorrida: {distancia} km
                    </Typography>              
                  )}
                </Grid>
            </Grid>
            
          </>
      );
}
    
