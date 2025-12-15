import { useState, useEffect } from "react";
import PruebasTable from "../pruebasTable/PruebasTable";
import PruebaService from "@/src/services/PruebaService";
import {Prueba} from "@/src/models/Prueba";
import VehiculoComboBox from "../vehiculoComboBox/VehiculoComboBox";
import { SelectChangeEvent, Grid } from "@mui/material";
import { useContext } from "react";
import GlobalContext from "@/src/contexts/GlogalContext";

const PruebaListadoFiltros = ( 
    props: {
        esEnCurso: boolean;
    }
) => {

    const [pruebas, setPruebas] = useState<Array<Prueba>>([]);
    const { getPruebasEnCurso, getPruebasPorVehiculo} = PruebaService();
    const [vehiculoId, setVehiculoId] = useState<number|undefined>(undefined);
    const {setToastProps} = useContext(GlobalContext);
    
    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement> | SelectChangeEvent<string>) => {
        const { name, value } = e.target;
        setVehiculoId(value ? parseInt(value) : undefined);
    }

    const onFinalizarPrueba = async () => {
        let pruebasData: Array<Prueba> = [];
        if(props.esEnCurso){
            pruebasData = await getPruebasEnCurso();                
        }else {
            try{
                pruebasData = await getPruebasPorVehiculo(vehiculoId);                
            }
            catch(error){
                console.error("Error fetching pruebas por vehículo:", error);
                setToastProps({
                    open: true,
                    severity: 'error',
                    message: 'Error fetching pruebas por vehículo.'
                });
            }
            
        }            
        setPruebas(pruebasData);
    };

    useEffect(() => {
        onFinalizarPrueba();
    }, [vehiculoId,]);


    return (
        <>        
        {!props.esEnCurso && 
            <Grid container mb={2}>
                <Grid  size={6}>
                    <form>                
                        <VehiculoComboBox
                            handleChange={handleChange}
                            selectedValue={vehiculoId}
                            hasEmptyOption={true}
                            name="vehiculoId"
                            id="vehiculoId"
                        />                
                    </form>
                </Grid>
            </Grid>
        }    
        
        <div>
            <PruebasTable
                pruebas={pruebas}
                onFinalizarPrueba={onFinalizarPrueba}
            />
        </div>
        </>
    );
};

export default PruebaListadoFiltros;