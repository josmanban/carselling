'use client'
import { Vehiculo } from "@/src/models/Vehiculo";
import {Table, 
    TableBody, 
    TableCell, 
    TableHead, 
    TableRow, 
    TableContainer, 
    Paper} from "@mui/material";

const VehiculoTable =(
    props: {
        vehiculos: Array<Vehiculo>;
    }
    )=>{

    return (
        <TableContainer component={Paper} sx={{ maxHeight: 500 }}>
            <Table size="small" stickyHeader>
                <TableHead>
                    <TableRow>                        
                        <TableCell>Id</TableCell>
                        <TableCell>Patente</TableCell>
                        <TableCell>Modelo</TableCell>
                        <TableCell>Marca</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {props.vehiculos.map((vehiculo) => (
                    <TableRow key={vehiculo.id}>
                        <TableCell>{vehiculo.id}</TableCell>
                        <TableCell>{vehiculo.patente}</TableCell>
                        <TableCell>{vehiculo.modelo?.descripcion}</TableCell>
                        <TableCell>{vehiculo.modelo?.marca?.nombre}</TableCell>                
                    </TableRow>
                ))}
                </TableBody>
            </Table>
        </TableContainer>        
    );
};

export default VehiculoTable;