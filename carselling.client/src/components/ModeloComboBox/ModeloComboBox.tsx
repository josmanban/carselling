'use client'
import { useState, useEffect } from "react"
import CustomComboBox from "../customComboBox/customComboBox"
import useModelo from "@/src/hooks/useModelo";
import {Modelo} from "@/src/models/Modelo";
import { SelectChangeEvent } from "@mui/material";

function ModeloComboBox(
    props: {
        handleChange: (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement> | SelectChangeEvent<string>) => void;
        selectedValue: string | number | undefined;
        hasEmptyOption?: boolean;
        name:string;
        id:string;
    }
){

    const { getModelos } = useModelo();
    const [modelos, setModelos] = useState<Array<Modelo>>([]);

    useEffect(() => {
        const fetchData = async () => {
            const modelos = await getModelos();
            setModelos(modelos);
        };
        fetchData();
    }, []);


    return (
        <CustomComboBox<Modelo>
            options={modelos}
            getOptionLabel={(option: Modelo) => `${option.descripcion} ${option.marca?.nombre}`}
            getValue={(option: Modelo) => option.id}
            handleChange={props.handleChange}
            selectedValue={props.selectedValue}
            label="Modelo"
            hasEmptyOption={props.hasEmptyOption}
            name={props.name}
            id={props.id}
        />
    )
}

export default ModeloComboBox;