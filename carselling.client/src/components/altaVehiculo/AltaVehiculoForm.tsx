'use client'
import { useState } from "react";
import { TextField, Button, Grid } from "@mui/material";
import ModeloComboBox from "../ModeloComboBox/ModeloComboBox";
import useVehiculo from "@/src/hooks/useVehiculo";
import { Vehiculo } from "@/src/models/Vehiculo";
import { AltaVehiculo } from "@/src/models/AltaVehiculo";
import { SelectChangeEvent } from "@mui/material";
import { useContext } from "react";
import GlobalContext from "@/src/contexts/GlogalContext";
import HttpError from "@/src/services/HttpError";

export default function AltaVehiculoForm(props:{
    onSuccess?: (vehiculo: Vehiculo) => void;
}) {
    const { registrarVehiculo } = useVehiculo();
    const { setToastProps } = useContext(GlobalContext);

    const [data, setData] = useState<AltaVehiculo>({
        patente: "",
        modeloId: undefined,
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement> | SelectChangeEvent<string>) => {
        const { name, value } = e.target;
        setData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    }

    const onSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const vehiculo:Vehiculo = await registrarVehiculo(data);
            setData({
                patente: "",
                modeloId: undefined,
            });
            setToastProps({
                open: true,
                severity: 'success',
                message: 'Vehículo registrado con éxito'
            });
            if(props.onSuccess){
                props.onSuccess(vehiculo);
            }
        } catch (error) {
            console.error("Error registrando vehículo:", error);
            if(error instanceof HttpError){
                setToastProps({
                    open: true,
                    severity: 'error',
                    message: `${error.body.message}`
                });
                return;
            }
            setToastProps({
                open: true,
                severity: 'error',
                message: 'Error registrando el vehículo'
            });
        }
    }

    return (
        <form onSubmit={onSubmit}>
            <Grid container spacing={2}>
                <Grid  size={6}>
                    <TextField
                        label="Patente"
                        name="patente"
                        value={data.patente}
                        onChange={handleChange}
                        fullWidth
                        required
                    />
                    <ModeloComboBox
                        handleChange={handleChange}
                        selectedValue={data.modeloId}
                        name="modeloId"
                        id="modelo-combobox"
                        hasEmptyOption={true}
                    />               
                    <Button type="submit" variant="contained" color="primary">
                        Registrar Vehículo
                    </Button>
                </Grid>
            </Grid>
        </form>
    );
}