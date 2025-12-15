import { useState, useEffect } from "react"
import CustomComboBox from "../customComboBox/customComboBox"
import useInteresado from "@/src/hooks/useInteresado";
import {Interesado} from "@/src/models/Interesado";
import { SelectChangeEvent } from "@mui/material";

function InteresadoComboBox(
    props: {
        handleChange: (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement> | SelectChangeEvent<string>) => void;
        selectedValue: string | number | undefined;
        hasEmptyOption?: boolean;
        name:string;
        id:string;
    }
){

    const { getInteresados } = useInteresado();
    const [interesados, setInteresados] = useState<Array<Interesado>>([]);

    useEffect(() => {
        const fetchData = async () => {
            const interesados = await getInteresados();
            setInteresados(interesados);
        };
        fetchData();
    }, []);


    return (
        <CustomComboBox<Interesado>
            options={interesados}
            getOptionLabel={(option: Interesado) => `${option.nombre} ${option.apellido}`}
            getValue={(option: Interesado) => option.id}
            handleChange={props.handleChange}
            selectedValue={props.selectedValue}
            label="Interesado"
            hasEmptyOption={props.hasEmptyOption}
            name={props.name}
            id={props.id}          
        />
    )
}

export default InteresadoComboBox;