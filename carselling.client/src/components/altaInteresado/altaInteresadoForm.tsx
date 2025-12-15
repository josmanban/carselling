import { useState } from "react";
import { AltaInteresado } from "@/src/models/AltaInteresado";
import TipoDocumento from "@/src/models/TipoDocumento";
import useAuth from "@/src/hooks/useAuth";
import { TextField, Button, InputLabel, Select, MenuItem, FormControl, SelectChangeEvent } from "@mui/material";
import { useContext } from "react";
import GlobalContext from "@/src/contexts/GlogalContext";

export default function AltaInteresadoForm() {
    const { registrarInteresado } = useAuth();
    const { setToastProps } = useContext(GlobalContext);
    
    const [data, setData] = useState<AltaInteresado>({
        nombre: "",
        apellido: "",
        email: "",
        username: "",
        telefonoContacto: "",
        password: "",
        nroLicencia: "",
        fechaVencimientoLicencia: "",
        documento: "",
        tipoDocumento: ""
    });
    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement> | SelectChangeEvent<string> ) => {
        const { name, value } = e.target;
        setData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };    

    const onSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const response = await registrarInteresado(data);
            console.log("Interesado registrado:", response);
            setToastProps({open:true, severity:"success", message:"Interesado registrado correctamente"});
        } catch (error) {
            console.error("Error registrando interesado:", error);
            setToastProps({open:true, severity:"error", message:"Error registrando interesado"});
        }
    };

    return (
        <form onSubmit={onSubmit} className="space-y-4">

            <TextField
                type="text"
                id="nombre"
                name="nombre"
                value={data.nombre}
                onChange={handleChange}
                label="Nombre"
                variant="outlined"
                fullWidth
                margin="normal"
            />
            <TextField
                type="text"
                id="apellido"
                name="apellido"
                value={data.apellido}
                onChange={handleChange}
                label="Apellido"
                variant="outlined"
                fullWidth
                margin="normal"
            />
            <TextField
                type="email"
                id="email"
                name="email"
                value={data.email}
                onChange={handleChange}
                label="Email"
                variant="outlined"
                fullWidth
                margin="normal"
            />
            <TextField
                type="text"
                id="username"
                name="username"
                value={data.username}
                onChange={handleChange}
                label="Username"
                variant="outlined"
                fullWidth
                margin="normal"
            />
            <TextField
                type="password"
                id="password"
                name="password"
                value={data.password}
                onChange={handleChange}
                label="Contraseña"
                variant="outlined"
                fullWidth
                margin="normal"
            />
            <TextField
                type="text"
                id="telefonoContacto"
                name="telefonoContacto"
                value={data.telefonoContacto}
                onChange={handleChange}
                label="Telefono de contacto"
                variant="outlined"
                fullWidth
                margin="normal"
            />
            <TextField
                type="text"
                id="nroLicencia"
                name="nroLicencia"
                value={data.nroLicencia}
                onChange={handleChange}
                label="Numero de licencia"
                variant="outlined"
                fullWidth
                margin="normal"
            />
            <TextField
                type="date"
                id="fechaVencimientoLicencia"
                name="fechaVencimientoLicencia"
                value={data.fechaVencimientoLicencia}
                onChange={handleChange}
                label="Fecha de vencimiento de licencia"
                variant="outlined"
                fullWidth
                margin="normal"
            />

            <FormControl fullWidth
            margin="normal">
                <InputLabel id="demo-simple-select-label">Age</InputLabel>
                <Select
                    id="tipoDocumento"
                    name="tipoDocumento"
                    value={data.tipoDocumento}
                    onChange={handleChange}
                >
                    {Object.values(TipoDocumento).map((tipo) => (
                        <MenuItem key={tipo} value={tipo}>
                            {tipo}
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>

            <TextField
                type="text"
                id="documento"
                name="documento"
                value={data.documento}
                onChange={handleChange}
                label="Documento"
                variant="outlined"
                fullWidth
                margin="normal"
            />
            <Button
                type="submit"
                variant="contained"
                fullWidth
            >
                Aceptar
            </Button>
            
        </form>
    );
}