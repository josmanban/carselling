import { Notificacion } from "../models/Notificacion";
import { NotificacionesApiUrls } from "../urls";
import RequestService from "./RequestService";
import { NotificacionFormData } from "../models/NotificacionFormData";

export default function NotificacionService() {

    const {
        getNotificacionesUrl,
        enviarNotificacionesUrl
    } = NotificacionesApiUrls();

    const {
        doAuthenticatedGet,
        doAuthenticatedPost} = RequestService();
        
    const getNotificaciones = async () => {
        return await doAuthenticatedGet(getNotificacionesUrl());
    };

    const enviarNotificaciones = async (data: NotificacionFormData) => {        
        return await doAuthenticatedPost(enviarNotificacionesUrl(), data);
    }

    return {
        getNotificaciones,
        enviarNotificaciones
    };
}