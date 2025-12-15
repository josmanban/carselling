'use client'
import { useEffect, useState } from "react";
import { Notificacion } from "@/src/models/Notificacion";
import useNotificacion from "@/src/hooks/useNotificacion";
import NotificacionesForm from "@/src/components/NotificacionesForm/NotificacionesForm";
import NotificacionTable from "@/src/components/NotificacionTable/NotificacionTable";
import { Typography } from "@mui/material";

const NotificacionesPage = () => {
    const [notificaciones, setNotificaciones] = useState<Array<Notificacion>>([]);
    const { getNotificaciones } = useNotificacion();

    const fetchNotificaciones = async () => {
        const notificacionesData = await getNotificaciones();
        setNotificaciones(notificacionesData);      
    }

    useEffect(() => {
        fetchNotificaciones();
    }, []);

    return (
        <>
            <Typography variant="h5" gutterBottom>
                Enviar Notificaciones
            </Typography>
            <NotificacionesForm onSave={fetchNotificaciones}/>
            <NotificacionTable notificaciones={notificaciones} />
        </>
    );
};

export default NotificacionesPage;
