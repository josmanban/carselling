import RequestService from './RequestService';
import { InteresadosApiUrls } from '../urls';

export default function InteresadoService() {

    const { getInteresadosUrl } = InteresadosApiUrls();
    const {
        doAuthenticatedGet
    } = RequestService();

    const getInteresados = async () => {
        return await doAuthenticatedGet(getInteresadosUrl());
    };

    return {
        getInteresados
    };
}