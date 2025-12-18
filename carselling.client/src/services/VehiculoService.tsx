import RequestService from './RequestService';
import { VehiculosApiUrls } from '../urls';
import { AltaVehiculo } from '../models/AltaVehiculo';

export default function VehiculoService() {

    const { getVehiculosUrl, registarVehiculoUrl } = VehiculosApiUrls();
    const {
        doAuthenticatedGet,
        doAuthenticatedPost
    } = RequestService();

    const getVehiculos = async () => {
        return await doAuthenticatedGet(getVehiculosUrl());
    };

    const registrarVehiculo = async (vehiculo: AltaVehiculo) => {
        return await doAuthenticatedPost(registarVehiculoUrl(), vehiculo);
    };

    return {
        getVehiculos,
        registrarVehiculo
    };
}