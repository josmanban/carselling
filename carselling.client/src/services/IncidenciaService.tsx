import { IncidenciasApiUrls } from "../urls"
import RequestService from "./RequestService";

export default function IncidenciaService() {
    const { getIncidenciasUrl } = IncidenciasApiUrls();
    const {
        doAuthenticatedGet
    } = RequestService();

    const getIncidencias = async (legajo?: number) => {
        return await doAuthenticatedGet(getIncidenciasUrl(legajo));
    };

    return {
        getIncidencias
    };
}