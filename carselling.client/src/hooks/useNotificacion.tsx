import { useState, useEffect } from "react";
import NotificacionService from "../services/NotificacionService";
import { Notificacion } from "../models/Notificacion";
import { NotificacionFormData } from "../models/NotificacionFormData";

export default function useNotificacion() {
    
    const service = NotificacionService();
    
    const getNotificaciones = async () => {
        try {
            const notificaciones: Notificacion[] = await service.getNotificaciones();
            return notificaciones;
        } catch (error) {
            console.error("Error fetching notificaciones:", error);
            throw error;
        }
    }

    const enviarNotificaciones = async (data: NotificacionFormData) => {
        try {
            await service.enviarNotificaciones(data);
        } catch (error) {
            console.error("Error sending notificaciones:", error);
            throw error;
        }
    }

    return {
        getNotificaciones,
        enviarNotificaciones
    };
}