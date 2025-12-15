'use client'
import AltaEmpleado from "@/src/components/altaEmpleado/AltaEmpleadoForm";
import AltaInteresadoForm from "@/src/components/altaInteresado/altaInteresadoForm";
import LogInForm from "@/src/components/logInForm/logInForm";

import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import { Grid, Link, Tab } from "@mui/material";
import {useRouter} from "next/navigation";
import {Table, TableBody, TableCell, TableHead, TableRow} from "@mui/material";

export default function RegistrarEmpleado() {
  const { push } = useRouter();
  return (
        <>
        <Card>
          <CardContent>
            <Typography variant="h5">Iniciar Sesión</Typography>            
            <LogInForm />
            <Link
              component="button"
              variant="body2"
              onClick={() => push('/auth/signUp')}
            >Registrar Usuario</Link>
          </CardContent>
        </Card>
        <Card sx={{ marginTop: 2 }}>
          <CardContent>
            <Typography variant="h5">Ayuda usuarios</Typography>            
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Rol</TableCell>
                  <TableCell>Nombre de usuario</TableCell>
                  <TableCell>Contraseña</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                <TableRow>
                  <TableCell>admin</TableCell>
                  <TableCell>admin</TableCell>
                  <TableCell>admin</TableCell>
                </TableRow>
                <TableRow>
                  <TableCell>empleado</TableCell>
                  <TableCell>juanperez</TableCell>
                  <TableCell>password</TableCell>
                </TableRow>
                <TableRow>
                  <TableCell>interesado</TableCell>
                  <TableCell>carlossanchez</TableCell>
                  <TableCell>password</TableCell>
                </TableRow>
              </TableBody>
            </Table>
          </CardContent>
        </Card>
        </>    
  );
}