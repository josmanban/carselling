import { AltaEmpleado } from "@/src/models/AltaEmpleado";
import { useState } from "react";
import useAuth from "@/src/hooks/useAuth";
import { TextField, Button, InputLabel, Select, MenuItem, FormControl, SelectChangeEvent } from "@mui/material";
import { useContext } from "react";
import GlobalContext from "@/src/contexts/GlogalContext";


export default function AltaEmpleadoForm() {
    const {registrarEmpleado} = useAuth();
    const { setToastProps } = useContext(GlobalContext);

    const [data,setData] = useState<AltaEmpleado>({
        nombre: "",
        apellido: "",
        email: "",
        username: "",
        password: "",
        telefonoContacto: ""
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    }

    const onSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const response = await registrarEmpleado(data);
            console.log("Empleado registrado:", response);
            setToastProps({open:true, severity:"success", message:"Empleado registrado correctamente"});
            setData({
                nombre: "",
                apellido: "",
                email: "",
                username: "",
                password: "",
                telefonoContacto: ""
            });
        } catch (error) {
            console.error("Error registrando empleado:", error);
            setToastProps({open:true, severity:"error", message:"Error registrando empleado"});
        }
    }


  return (
    <form onSubmit={onSubmit}>        
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
            label="Nombre de usuario"
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