'use client'

import { useState } from "react";
import InteresadoComboBox from "@/src/components/interesadoComboBox/InteresadoComboBox";
import VehiculoComboBox from "@/src/components/vehiculoComboBox/VehiculoComboBox";
import { AltaPrueba } from "@/src/models/AltaPrueba";
import PruebaService from "@/src/services/PruebaService";
import { Button, Typography } from "@mui/material";
import { useContext } from "react";
import GlobalContext from "@/src/contexts/GlogalContext";
import HttpError from "@/src/services/HttpError";
import { SelectChangeEvent } from "@mui/material";
import {Grid} from '@mui/material';


export default function PruebasEnCurso() {

  const { setToastProps } = useContext(GlobalContext);

  const { registrarPrueba } = PruebaService();

  const [data, setData] = useState<AltaPrueba>({
    interesadoId: undefined,
    vehiculoId: undefined
  });


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
      const nuevaPrueba: any = await registrarPrueba(data);
      console.log("Prueba registrada:", nuevaPrueba);
      setToastProps({
        open: true,
        severity: 'success',
        message: 'Prueba registrada con éxito'
      });
    }catch (error) {      
      console.error("Error registrando prueba:", error);
      if (error instanceof HttpError) {
        setToastProps({
          open: true,
          severity: 'error',
          message: `${error.body.message}`
        });
      }else {
        setToastProps({
          open: true,
          severity: 'error',
          message: 'Error registrando la prueba'
        });
      }
    }
  }


  return (
    <>  
      <Typography variant="h5" gutterBottom>Registrar Prueba</Typography>    
      <Grid container sx={{ mb: 4 }}>
            <Grid size={6}>
          <form 
            className=""
            onSubmit={onSubmit}
          >
            
              <InteresadoComboBox
                handleChange={handleChange}
                selectedValue={data.interesadoId}
                hasEmptyOption={true}
                name="interesadoId"
                id="interesadoId"
              />
            
              <VehiculoComboBox
                handleChange={handleChange}
                selectedValue={data.vehiculoId}
                hasEmptyOption={true}
                name="vehiculoId"
                id="vehiculoId"
              />          

                <Button
                    type="submit"
                    variant="contained"
                    fullWidth                  
                >
                    Registrar
                </Button>
          </form>
        </Grid>
      </Grid>
      </>
  );
}