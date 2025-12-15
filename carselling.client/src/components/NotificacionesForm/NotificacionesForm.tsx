import { useState } from "react";
import { NotificacionFormData } from "@/src/models/NotificacionFormData";
import useNotificacion from "@/src/hooks/useNotificacion";
import { Button, TextField } from "@mui/material";
import { useContext } from "react";
import GlobalContext from "@/src/contexts/GlogalContext";
import {Box, Grid} from '@mui/material';

const NotificacionesForm = (props: {
    onSave: () => void;    
}) => {
    const [notificacionData, setNotificacionData] = useState<NotificacionFormData>({
        telefonos: [],
        mensaje: ""
    });
    const { setToastProps } = useContext(GlobalContext);
    const {enviarNotificaciones} = useNotificacion();

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setNotificacionData({
            ...notificacionData,
            [name]: value
        });
    };

    const handleTelefonosChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        const telefonosArray = e.target.value.split(',').map(t => t.trim());
        setNotificacionData({
            ...notificacionData,
            telefonos: telefonosArray
        });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try{

            await enviarNotificaciones(notificacionData);
            setToastProps({
                open: true,
                severity: 'success',
                message: 'Notificaciones enviadas con éxito'
            });
            if(props.onSave){
                props.onSave();
            }
        }catch(error){
            console.error("Error sending notifications:", error);            
            setToastProps({
                open: true,
                severity: 'error',
                message: 'Error al enviar notificaciones'
            });
            
        }
    }

    return (
        <Grid container sx={{ mb: 4 }}>
            <Grid size={6}>

            <form onSubmit={handleSubmit}>
                
                    <TextField
                        fullWidth
                        multiline
                        rows={4}
                        name="telefonos"
                        label="Telefonos (separados por coma)"
                        value={notificacionData.telefonos.join(', ')}
                        onChange={handleTelefonosChange}
                        required
                        margin="normal"
                        />

                    <TextField
                        fullWidth
                        multiline
                        rows={4}
                        name="mensaje"
                        label="Mensaje"
                        value={notificacionData.mensaje}
                        onChange={handleInputChange}
                        required
                        margin="normal"
                        />
                
                <Button fullWidth type="submit" variant="contained">Enviar Notificaciones</Button>
            </form>
            </Grid>
        </Grid>
    );

};

export default NotificacionesForm;
