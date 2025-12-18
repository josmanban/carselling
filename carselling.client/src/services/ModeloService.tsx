'use client'

import RequestService from './RequestService';
import { ModelosApiUrls } from '../urls';

export default function ModeloService() {

    const { getModelosUrl } = ModelosApiUrls();
    const {
        doAuthenticatedGet,
    } = RequestService();

    const getModelos = async () => {
        return await doAuthenticatedGet(getModelosUrl());
    };

    return {
        getModelos,
    };
}