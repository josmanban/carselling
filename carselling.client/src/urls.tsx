const PROVEDOR_IDENTIDAD_URL = "http://identity.provider:8080";
const PRUEBAS_API_URL = "http://localhost:8084/pruebas";
const AUTH_API_URL = "http://localhost:8084/auth";
const INCIDENCIAS_API_URL = "http://localhost:8084/incidencias";
const VEHICULOS_API_URL = "http://localhost:8084/vehiculos";
const EMPLEADOS_API_URL = "http://localhost:8084/empleados";
const INTERESADOS_API_URL = "http://localhost:8084/interesados";
const NOTIFICACIONES_API_URL = "http://localhost:8084/notificaciones";
const MODELOS_API_URL = "http://localhost:8084/modelos";

export function ModelosApiUrls(){

    const getModelosUrl = () => {
        return MODELOS_API_URL;
    };

    return {
        getModelosUrl
    };
}

export function VehiculosApiUrls(){

    const registarVehiculoUrl = () => {
        return VEHICULOS_API_URL;
    }

    const getVehiculosUrl = () => {
        return VEHICULOS_API_URL;
    };

    return {
        registarVehiculoUrl,
        getVehiculosUrl
    };
}

export function EmpleadosApiUrls(){

    const getEmpleadosUrl = () => {
        return EMPLEADOS_API_URL;
    };

    return {
        getEmpleadosUrl
    };
}

export function InteresadosApiUrls(){

    const getInteresadosUrl = () => {
        return INTERESADOS_API_URL;
    };

    return {
        getInteresadosUrl
    };
}

export function AuthUrls(){

    const getTokensUrl = () => {
        return `${PROVEDOR_IDENTIDAD_URL}/realms/carsellingrealm/protocol/openid-connect/token`;
    };
    const getAltaEmpleadoUrl = () => {
        return `${AUTH_API_URL}/alta-empleado`;
    };
    
    const getAltaInteresadoUrl = () => {
        return `${AUTH_API_URL}/alta-interesado`;
    };

    return {
        getAltaEmpleadoUrl,
        getAltaInteresadoUrl,
        getTokensUrl,
    };
}


export function PruebasApiUrls(){

    const registrarPruebaUrl = () => {
        return PRUEBAS_API_URL;
    }

    const getPruebasEnCursoUrl = () => {
        return `${PRUEBAS_API_URL}/en-curso`;
    };

    const getPruebaEnCursoUrl = () => {
        return `${PRUEBAS_API_URL}/prueba-en-curso`;
    }

    const finalizarPruebaUrl = (pruebaId: number) => {
        return `${PRUEBAS_API_URL}/${pruebaId}/finalizar`;
    }

    const actualizarPosicionVehiculoUrl = () => {
        return `${PRUEBAS_API_URL}/check-posicion-vehiculo`;
    };

    const getPruebasPorVehiculoUrl = (vehiculoId: number|undefined) => {
        if(vehiculoId === undefined){
            return `${PRUEBAS_API_URL}/vehiculo`;
        }
        return `${PRUEBAS_API_URL}/vehiculo?vehiculoId=${vehiculoId}`;
    };

    const getDistanciaRecorridaUrl = (vehiculoId: number, fechaInicio: string, fechaFin: string) => {
        return `${PRUEBAS_API_URL}/distancia-recorridas-por-periodo?vehiculoId=${vehiculoId}&desde=${fechaInicio}&hasta=${fechaFin}`;
    }

    return {
        getPruebasEnCursoUrl,
        registrarPruebaUrl,
        finalizarPruebaUrl,
        actualizarPosicionVehiculoUrl,
        getPruebasPorVehiculoUrl,
        getDistanciaRecorridaUrl,
        getPruebaEnCursoUrl
    };
}

export function IncidenciasApiUrls(){
    const getIncidenciasUrl = (legajo?:number) => {

        return legajo ? `${INCIDENCIAS_API_URL}?legajo=${legajo}` : INCIDENCIAS_API_URL;
    }
    return {
        getIncidenciasUrl
    };  
}

export function NotificacionesApiUrls(){

    const getNotificacionesUrl = () => {
        return NOTIFICACIONES_API_URL;
    };

    const enviarNotificacionesUrl = () => {
        return `${NOTIFICACIONES_API_URL}/enviar-notificaciones`;
    }

    return {
        getNotificacionesUrl,
        enviarNotificacionesUrl
    };  
}