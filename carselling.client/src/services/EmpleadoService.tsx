import { EmpleadosApiUrls } from "../urls"
import RequestService from "./RequestService";

export default function EmpleadoService() {
    const { getEmpleadosUrl } = EmpleadosApiUrls();
    const {
        doAuthenticatedGet
    } = RequestService();

    const getEmpleados = async () => {
        return await doAuthenticatedGet(getEmpleadosUrl());
    };

    return {
        getEmpleados
    };
}