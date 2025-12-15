import { useState } from "react";
import useAuth from "@/src/hooks/useAuth";
import LogInData from "@/src/models/LogInData";
import {useRouter} from "next/navigation";
import { TextField } from "@mui/material";
import { Button } from "@mui/material";
import { useContext } from "react";
import GlobalContext from "@/src/contexts/GlogalContext";

export default function LogInForm() {
    const router = useRouter()
    const { login,esAdmin, esEmpleado, esInteresado } = useAuth();
    const [data, setData] = useState<LogInData>({
        username: "",
        password: ""
    });
    const {setToastProps} = useContext(GlobalContext);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    const onSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await login(data);
            console.log("Login successful");            
            setToastProps({
                open: true,
                severity: 'success',
                message: 'Inicio de sesión exitoso'
            });
            router.push("/logedIn/home");
        } catch (error) {
            console.error("Error during login:", error);
            setToastProps({
                open: true,
                severity: 'error',
                message: 'Error al iniciar sesión. Por favor, verifica tus credenciales.'
            });
        }
    };

    return (
        <form onSubmit={onSubmit} className="space-y-4">            
            <TextField
                label="Username"
                type="text"
                id="username"
                name="username"
                value={data.username}
                onChange={handleChange}
                fullWidth
                variant="outlined"
                margin="normal"
            />
            <TextField
                label="password"
                type="password"
                id="password"
                name="password"
                value={data.password}
                onChange={handleChange}
                variant="outlined"
                fullWidth
                margin="normal"
            />           
            <Button
                type="submit"
                variant="contained"
                fullWidth               
            >
                Log In
            </Button>
        </form>
    );
}