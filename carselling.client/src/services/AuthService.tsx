'use client'
import { AltaEmpleado } from "../models/AltaEmpleado";
import { PruebasApiUrls } from "../urls"
import { AuthUrls } from "../urls";
import HttpError from "./HttpError";
export default function AuthService() {
    // Authentication service logic would go here
    const login = async (username: string, password: string) => {
        // Get access and refresh tokens
        const authUrls = AuthUrls();
        const url = authUrls.getTokensUrl();    
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams({
                grant_type: 'password',
                client_id: 'carsellingclient',
                username: username,
                password: password,
            }),
        });
        if(response.status !== 200){
            const contentType = response.headers.get("content-type");
            const body = contentType?.includes("application/json") ? await response.json() : await response.text();
            throw new HttpError(response.status, response.statusText, body);
        }

        return response.json();        
    }

    const registrarEmpleado= async (empleadoData: AltaEmpleado) =>{
        // Implement employee registration logic

        const apiUrls = AuthUrls();
        const url = apiUrls.getAltaEmpleadoUrl();
        try{            
            const newEmpleadoData = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(empleadoData),
            }).then(response => response.json());
            return newEmpleadoData;
        } catch (error) {
            console.error("Error registering employee:", error);
            throw error;
        }        
    }

    const registrarInteresado= async (interesadoData: any) =>{
        // Implement interested party registration logic

        const apiUrls = AuthUrls();
        const url = apiUrls.getAltaInteresadoUrl();
        try{            
            const newInteresadoData = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(interesadoData),
            }).then(response => response.json());
            return newInteresadoData;
        } catch (error) {
            console.error("Error registering interesado:", error);
            throw error;
        }        
    }

    const reloadAccessToken = async () => {
        const authUrls = AuthUrls();
        const url = authUrls.getTokensUrl();
        const refreshToken = localStorage.getItem("refreshToken");
        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams({
                    grant_type: 'refresh_token',
                    client_id: 'carsellingclient',
                    refresh_token: refreshToken || '',
                }),
            }).then(response => response.json());
            const newAccessToken = response.access_token;
            const newRefreshToken = response.refresh_token;
            localStorage.setItem("accessToken", newAccessToken);
            localStorage.setItem("refreshToken", newRefreshToken);
            return newAccessToken;
        }     catch (error) {
            console.error("Error reloading access token:", error);
            throw error;
        }
    }

    return {
        login,
        registrarEmpleado,
        registrarInteresado,
        reloadAccessToken,
    };

}