
import { Incidencia } from "@/src/models/Incidencia";
import {Table, 
    TableBody, 
    TableCell, 
    TableHead, 
    TableRow, 
    TableContainer, 
    Paper} from "@mui/material";

const IncidenciaTable =(
    props: {
        incidencias: Array<Incidencia>;
    }
    )=>{

    return (
        <TableContainer component={Paper} sx={{ maxHeight: 500 }}>
            <Table size="small" stickyHeader>
                <TableHead>
                    <TableRow>                        
                        <TableCell>Id</TableCell>
                        <TableCell>Vehiculo</TableCell>
                        <TableCell>Distancia desde Agencia [Km]</TableCell>
                        <TableCell>Tipo</TableCell>
                        <TableCell>Fecha Hora</TableCell>
                        <TableCell>Posición</TableCell>            
                        <TableCell>Interesado</TableCell>
                        <TableCell>Empleado</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {props.incidencias.map((incidencia) => (
                    <TableRow key={incidencia.id}>
                        <TableCell>{incidencia.id}</TableCell>
                        <TableCell>{incidencia.prueba.vehiculo.patente}</TableCell>
                        <TableCell>{incidencia.distanciaAgenciaKm}</TableCell>
                        <TableCell>{incidencia.esZonaRestringida?"En zona Restringida":"Fuera area permitida"}</TableCell>
                        <TableCell>{incidencia.fechaHora}</TableCell>
                        <TableCell>Lat: {incidencia.posicion.latitud}, Long: {incidencia.posicion.longitud}</TableCell>
                        <TableCell>{incidencia.prueba.interesado.nombre} {incidencia.prueba.interesado.apellido}</TableCell>
                        <TableCell>{incidencia.prueba.empleado.nombre} {incidencia.prueba.empleado.apellido}</TableCell>                
                    </TableRow>
                ))}
                </TableBody>
            </Table>
        </TableContainer>        
    );
};

export default IncidenciaTable;