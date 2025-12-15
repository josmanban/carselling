import { useState, useEffect } from "react"
import CustomComboBox from "../customComboBox/customComboBox"
import useVehiculo from "@/src/hooks/useVehiculo";
import {Vehiculo} from "@/src/models/Vehiculo";
import { SelectChangeEvent } from "@mui/material";

function VehiculoComboBox(
    props: {
        handleChange: (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement> | SelectChangeEvent<string>) => void;
        selectedValue: string | number | undefined;
        hasEmptyOption?: boolean;
        name: string;
        id: string;
    }
){

    const { getVehiculos } = useVehiculo();
    const [vehiculos, setVehiculos] = useState<Array<Vehiculo>>([]);

    useEffect(() => {
        const fetchData = async () => {
            const vehiculos = await getVehiculos();
            setVehiculos(vehiculos);
        };
        fetchData();
    }, []);


    return (
        <CustomComboBox<Vehiculo>
            options={vehiculos}
            getOptionLabel={(option: Vehiculo) => option.patente}
            getValue={(option: Vehiculo) => option.id}
            handleChange={props.handleChange}
            selectedValue={props.selectedValue}
            label="Vehiculo"
            hasEmptyOption={props.hasEmptyOption}
            name={props.name}
            id={props.id}
        />
    )
}

export default VehiculoComboBox;