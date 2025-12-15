'use client'
import AltaEmpleado from "@/src/components/altaEmpleado/AltaEmpleadoForm";
import AltaInteresadoForm from "@/src/components/altaInteresado/altaInteresadoForm";

import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import { Grid, Link } from "@mui/material";
import {useRouter} from "next/navigation";

export default function RegistrarEmpleado() {
  const { push } = useRouter();
  return (
    <Grid container spacing={2}>
      <Grid size={12}>
        <Link
              component="button"
              variant="body2"
              onClick={() => push('/auth/logIn')}
            >Iniciar sesion</Link>
      </Grid>
      <Grid size={6}>
        <Card>
          <CardContent>
            <Typography variant="h5">Registrar Empleado</Typography>
            <AltaEmpleado />
          </CardContent>
        </Card>
      </Grid>
      <Grid size={6}>
        <Card>
          <CardContent>
            <Typography variant="h5">Registrar Interesado</Typography>            
            <AltaInteresadoForm />
          </CardContent>
        </Card>
      </Grid>
    </Grid>
  );
}