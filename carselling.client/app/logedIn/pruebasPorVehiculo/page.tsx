'use client'

import PruebaListadoFiltros from "@/src/components/PruebaListadoFiltros/pruebaListadoFiltros";
import { Typography } from "@mui/material";
export default function PruebasPorVehiculo() {
  return (
    <>
      <Typography variant="h5">Pruebas por vehículo</Typography>        
      <PruebaListadoFiltros esEnCurso={false} />    
    </>
  );
}