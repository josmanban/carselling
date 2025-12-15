import { useState, useEffect } from "react"
import CustomComboBox from "../customComboBox/customComboBox"
import useEmpleado from "@/src/hooks/useEmpleado"
import {Empleado} from "@/src/models/Empleado";
import { SelectChangeEvent } from "@mui/material";

function EmpleadoComboBox(
    props: {
        handleChange: (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement> | SelectChangeEvent<string>) => void;
        selectedValue: string | number | undefined;
        hasEmptyOption?: boolean;
        name:string;
        id:string;
    }
){

    const { getEmpleados } = useEmpleado();
    const [empleados, setEmpleados] = useState<Array<Empleado>>([]);

    useEffect(() => {
        const fetchData = async () => {
            const empleados = await getEmpleados();
            setEmpleados(empleados);
        };
        fetchData();
    }, []);


    return (
        <CustomComboBox<Empleado>
            options={empleados}
            getOptionLabel={(option: Empleado) => `${option.nombre} ${option.apellido}`}
            getValue={(option: Empleado) => option.legajo}
            handleChange={props.handleChange}
            selectedValue={props.selectedValue}
            label="Empleado"
            hasEmptyOption={props.hasEmptyOption}
            name={props.name}
            id={props.id}
        />
    )
}

EmpleadoComboBox.prototype = {};

export default EmpleadoComboBox;