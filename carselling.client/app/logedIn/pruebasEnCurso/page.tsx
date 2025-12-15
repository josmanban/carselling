'use client'

import PruebaListadoFiltros from "@/src/components/PruebaListadoFiltros/pruebaListadoFiltros";
import { Typography } from "@mui/material";

export default function PruebasEnCurso() {
  return (
      <>
        <Typography variant="h5">Pruebas en curso</Typography>
        <PruebaListadoFiltros esEnCurso={true} />
      </>
    
  );
}