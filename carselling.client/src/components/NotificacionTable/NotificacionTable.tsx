import {Notificacion} from "@/src/models/Notificacion";
import { TableContainer,
    Table,
    TableHead,
    TableRow,
    TableCell,
    TableBody,
    Paper,
    Button,
    Typography,
    TextareaAutosize,
    FormControl
 } from "@mui/material";

const NotificacionTable = (props:{
    notificaciones: Notificacion[];
}) => {
    return (
        <TableContainer component={Paper} sx={{ maxHeight: 500 }}>
            <Table size="small" stickyHeader>
                <TableHead>
                    <TableRow>
                        <TableCell>ID</TableCell>
                        <TableCell>Mensaje</TableCell>
                        <TableCell>Fecha y Hora</TableCell>
                        <TableCell>Teléfono</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {props.notificaciones.map((notificacion) => (
                    <TableRow    key={notificacion.id}>
                        <TableCell className="border border-gray-300 px-4 py-2">{notificacion.id}</TableCell>
                        <TableCell className="border border-gray-300 px-4 py-2">{notificacion.mensaje}</TableCell>
                        <TableCell className="border border-gray-300 px-4 py-2">{notificacion.fechaHora}</TableCell>
                        <TableCell className="border border-gray-300 px-4 py-2">{notificacion.telefonoContacto}</TableCell>
                    </TableRow>
                ))}
                </TableBody>
            </Table>
        </TableContainer>
    )
};

export default NotificacionTable;