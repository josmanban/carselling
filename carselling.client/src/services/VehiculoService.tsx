import RequestService from './RequestService';
import { VehiculosApiUrls } from '../urls';

export default function VehiculoService() {

    const { getVehiculosUrl } = VehiculosApiUrls();
    const {
        doAuthenticatedGet
    } = RequestService();

    const getVehiculos = async () => {
        return await doAuthenticatedGet(getVehiculosUrl());
    };

    return {
        getVehiculos
    };
}