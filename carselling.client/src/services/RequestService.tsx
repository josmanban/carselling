import useAuth from "../hooks/useAuth";
import HttpError from "./HttpError";

export default function Service(){

    const { reloadAccessToken } = useAuth();

    const doPost = async (url: string, data: any) => {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        });
        if (response.ok) {
            return response.json();
        }
        const contentType = response.headers.get("content-type");
        const body = contentType?.includes("application/json") ? await response.json() : await response.text();
        throw new HttpError(response.status, response.statusText, body);
    }

    const doAuthenticatedPut = async (url: string, data: any) => {
        const accessToken: string | null = localStorage.getItem("accessToken");
        const response = await fetch(url, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify(data),
        });
        if (response.ok) {
            return response.json();
        }
        else if (response.status === 401) {                
            // Token might be expired, try to reload it
            await reloadAccessToken();
            // Retry the request with the new token
            return doAuthenticatedPut(url, data);
        }else {
            const contentType = response.headers.get("content-type");
            const body = contentType?.includes("application/json") ? await response.json() : await response.text();
            throw new HttpError(response.status, response.statusText, body);
        }
    }

    const doPut = async (url: string, data: any) => {
        const response = await fetch(url, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        });
        if (response.ok) {
            return response.json();
        }
        const contentType = response.headers.get("content-type");
        const body = contentType?.includes("application/json") ? await response.json() : await response.text();
        throw new HttpError(response.status, response.statusText, body);
    }   

    const doDelete = async (url: string) => {
        const response = await fetch(url, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        });
        if (response.ok) {
            return response.json();
        }
        const contentType = response.headers.get("content-type");
        const body = contentType?.includes("application/json") ? await response.json() : await response.text();
        throw new HttpError(response.status, response.statusText, body);
    }

    const doAuthenticatedDelete = async (url: string) => {
        const accessToken: string | null = localStorage.getItem("accessToken");
        const response = await fetch(url, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
        });
        if (response.ok) {
            return response.json();
        }
        else if (response.status === 401) {                
            // Token might be expired, try to reload it
            await reloadAccessToken();
            // Retry the request with the new token
            return doAuthenticatedDelete(url);
        }else {
            const contentType = response.headers.get("content-type");
            const body = contentType?.includes("application/json") ? await response.json() : await response.text();
            throw new HttpError(response.status, response.statusText, body);
        }   
    }

    const doGet = async (url: string) => {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });
        if (response.ok) {
            return response.json();
        }
        const contentType = response.headers.get("content-type");
        const body = contentType?.includes("application/json") ? await response.json() : await response.text();
        throw new HttpError(response.status, response.statusText, body);
    }

    const doAuthenticatedPost = async (url: string, data: any) => {
        const accessToken: string | null = localStorage.getItem("accessToken");
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify(data),
        });
        if (response.ok) {
            return response.json();
        }
        else if (response.status === 401) {                
            // Token might be expired, try to reload it
            await reloadAccessToken();
            // Retry the request with the new token
            return doAuthenticatedPost(url, data);
        }else {
            const contentType = response.headers.get("content-type");
            const body = contentType?.includes("application/json") ? await response.json() : await response.text();
            throw new HttpError(response.status, response.statusText, body);
        }
    }

    const doAuthenticatedGet = async (url: string) => {
        const accessToken: string | null = localStorage.getItem("accessToken");
        
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                },
            });
            if (response.ok) {
                return response.json();
            }
            else if (response.status === 401) {                
                // Token might be expired, try to reload it
                await reloadAccessToken();
                // Retry the request with the new token
                return doAuthenticatedGet(url);
            }else {
                const contentType = response.headers.get("content-type");
                const body = contentType?.includes("application/json") ? await response.json() : await response.text();
                throw new HttpError(response.status, response.statusText, body);
            }        
    }    

    return {
        doPost,
        doGet,
        doAuthenticatedPost,
        doAuthenticatedGet,
        doPut,
        doAuthenticatedPut,
        doDelete,
        doAuthenticatedDelete,
    };
}