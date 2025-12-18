import { PruebasApiUrls } from "../urls";
import RequestService from "./RequestService";
import { AltaPrueba } from "../models/AltaPrueba";
import { PosicionData } from "../models/PosicionData";
import { FinalizarPruebaData } from "../models/FinalizarPruebaData";
import { DistanciaVehiculo } from "../models/DistanciaVehiculo";

export default function PruebaService() {

    const {
        getPruebasEnCursoUrl,
        registrarPruebaUrl,
        finalizarPruebaUrl,
        actualizarPosicionVehiculoUrl,
        getPruebasPorVehiculoUrl,
        getDistanciaRecorridaUrl,
        getPruebaEnCursoUrl
    } = PruebasApiUrls();

    const {
        doAuthenticatedGet,
        doAuthenticatedPost,
        doAuthenticatedPut,
        doAuthenticatedDelete,
        doGet,
        doPost,
        doPut,
        doDelete
    } = RequestService();


    const registrarPrueba = async (data: AltaPrueba) => {
        return await doAuthenticatedPost(registrarPruebaUrl(), data);
    };

    const getPruebasEnCurso = async () => {        
        return await doAuthenticatedGet(getPruebasEnCursoUrl());
    };

    const getPruebaEnCurso = async () => {        
        return await doAuthenticatedGet(getPruebaEnCursoUrl());
    };

    const finalizarPrueba = async (pruebaId: number, data: FinalizarPruebaData) => {        
        return await doAuthenticatedPut(finalizarPruebaUrl(pruebaId), data);
    };

    const actualizarPosicionVehiculo = async (data: PosicionData) => {
        return await doAuthenticatedPost(actualizarPosicionVehiculoUrl(), data);
    }

    const getPruebasPorVehiculo = async (vehiculoId: number|undefined) => {
        return await doAuthenticatedGet(getPruebasPorVehiculoUrl(vehiculoId));
    }

    const getDistanciaRecorrida = async (distanciaData:DistanciaVehiculo) => {
        return await doAuthenticatedGet(getDistanciaRecorridaUrl(distanciaData.vehiculoId!, distanciaData.desde!, distanciaData.hasta!));
    }

    return {
        registrarPrueba,
        getPruebasEnCurso,
        finalizarPrueba,
        actualizarPosicionVehiculo,
        getPruebasPorVehiculo,
        getDistanciaRecorrida,
        getPruebaEnCurso
    };

}