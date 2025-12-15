'use client'
import { useEffect, useState } from "react";
import useIncidencia from "@/src/hooks/useIncidencia";
import IncidenciaTable from "@/src/components/IncidenciaTable/IncidenciaTable";
import EmpleadoComboBox from "@/src/components/empladoComboBox/EmpleadoComboBox";
import { Typography, Grid } from "@mui/material";
import { useContext } from "react";
import GlobalContext from "@/src/contexts/GlogalContext";
import HttpError from "@/src/services/HttpError";

const IncidenciasPage = () => {
    const [legajo, setLegajo] = useState<number | undefined>(undefined);
    const [incidencias, setIncidencias] = useState<Array<any>>([]);
    const { getIncidencias } = useIncidencia();
    const {setToastProps} = useContext(GlobalContext);

    useEffect(() => {        
        try{

            const fetchIncidencias = async () => {            
                const incidenciasData = await getIncidencias(legajo? legajo : undefined);
                setIncidencias(incidenciasData);
            };
            fetchIncidencias();
        }catch(error){
            console.error("Error fetching incidencias:", error);
            if(error instanceof HttpError){
                setToastProps({
                    open: true,
                    severity: 'error',
                    message: error.body.message
                });
                return;
            }
            setToastProps({
                open: true,
                severity: 'error',
                message: 'Error fetching incidencias.'
            });
        }

    }, [legajo]);

    return (
        <>
            <Typography variant="h5" mb={2}>
                Listado de Incidencias
            </Typography> 
            <Grid container mb={2}>
                <Grid size={6}>
                <EmpleadoComboBox
                    handleChange={(e) => {
                        const { value } = e.target;
                        setLegajo(value ? parseInt(value) : undefined);
                    }}
                    selectedValue={legajo}
                    hasEmptyOption={true}
                    name="legajo"
                    id="legajo"
                    />
                </Grid>
            </Grid>           
        
            <IncidenciaTable
                incidencias={incidencias}
            />

        </>
    );
};
export default IncidenciasPage;