
import {Prueba} from "../../models/Prueba";
import { useEffect, useState, useContext } from "react";
import PruebaService from "@/src/services/PruebaService";
import useAuth from "@/src/hooks/useAuth";
import GlobalContext from "@/src/contexts/GlogalContext";

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
    FormControl,
    TextField,
    Grid
 } from "@mui/material";
import HttpError from "@/src/services/HttpError";

const PruebasTable = (props:{
    pruebas: Prueba[];
    onFinalizarPrueba?: () => void;
}) => {

    const { esEmpleado } = useAuth();  
    
    const [idPruebaFinalizar, setIdPruebaFinalizar] = useState<number|undefined>(undefined);
    const [comentarios, setComentarios] = useState<string>("");

    const { finalizarPrueba } = PruebaService();

    const { setToastProps } = useContext(GlobalContext);

    const handleFinalizarPrueba = async (pruebaId: number) => {
        setIdPruebaFinalizar(pruebaId);
    }

    const finalizar = async () => {
        if(idPruebaFinalizar){
            try {
                await finalizarPrueba(idPruebaFinalizar, {comentarios: comentarios});
                setComentarios("");
                setIdPruebaFinalizar(undefined);
                if(props.onFinalizarPrueba){
                    props.onFinalizarPrueba();
                }
                setToastProps({
                    open: true,
                    severity: 'success',
                    message: 'Prueba finalizada con éxito'
                });
            } catch (error) {
                console.error("Error finalizando la prueba:", error);
                if(error instanceof HttpError){
                    setToastProps({
                        open: true,
                        severity: 'error',
                        message: `${error.body.message}`
                    });            
                }else{
                    setToastProps({
                        open: true,
                        severity: 'error',
                        message: 'Error finalizando la prueba'
                    });            
                }
            }
        }
    };


    return (
        <>
        {idPruebaFinalizar && (
            <>

                    <Grid container spacing={2} sx={{ mb: 2, mt: 1 }}>
                        <Grid size={6}>
                            <Typography variant="h6">Finalizar prueba {idPruebaFinalizar}</Typography>                                
                            <TextField
                                label="Comentarios"
                                fullWidth
                                multiline
                                value={comentarios}
                                rows={3}
                                onChange={(e) => setComentarios(e.target.value) }
                                />
                        </Grid>
                        <Grid size={12}>
                            <Button
                                variant="outlined"
                                onClick={() => setIdPruebaFinalizar(undefined)}
                                sx={{ mr: 2 }}
                                >
                                Cancelar                                
                            </Button>
                            <Button                        
                                variant="contained"
                                onClick={finalizar}
                                >
                                Finalizar
                            </Button>   
                        </Grid>
                    </Grid>
                    

            </>
        )}        

        <TableContainer component={Paper} sx={{ maxHeight: 500 }}>
            <Table stickyHeader sx={{ minWidth: 650 }} size="small" aria-label="simple table">
                <TableHead>
                <TableRow>                    
                    <TableCell>Id</TableCell>
                    <TableCell align="right">Vehiculo</TableCell>
                    <TableCell align="right">Fecha Hora Inicio</TableCell>
                    <TableCell align="right">Fecha Hora Fin</TableCell>
                    <TableCell align="right">Interesado</TableCell>
                    <TableCell align="right">Empleado</TableCell>
                    <TableCell align="right">Comentarios</TableCell>
                    <TableCell align="right">Acción</TableCell>
                </TableRow>
                </TableHead>
                <TableBody>
                    {props.pruebas.map((prueba) => (
                        <TableRow
                        key={prueba.id}
                        sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                        >
                        <TableCell>{prueba.id}</TableCell>
                        <TableCell align="right">{prueba.vehiculo.patente}</TableCell>
                        <TableCell align="right">{prueba.fechaHoraInicio}</TableCell>
                        <TableCell align="right">{prueba.fechaHoraFin ? prueba.fechaHoraFin : "En curso"}</TableCell>
                        <TableCell align="right">{prueba.interesado.nombre} {prueba.interesado.apellido}</TableCell>
                        <TableCell align="right">{prueba.empleado.nombre} {prueba.empleado.apellido}</TableCell>
                        <TableCell align="right">{prueba.comentarios ? prueba.comentarios : "Sin comentarios"}</TableCell>
                        <TableCell align="right">
                            {(esEmpleado()) && !prueba.fechaHoraFin && (
                                <Button
                                    onClick={() => handleFinalizarPrueba(prueba.id)}
                                    variant="contained"
                                    fullWidth                                    
                                >
                                    Finalizar Prueba                                
                                </Button> 
                            )}
                        </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
            </TableContainer>
        </>
    )
};

export default PruebasTable;