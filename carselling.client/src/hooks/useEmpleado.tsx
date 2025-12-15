import { useState, useEffect } from 'react';
import EmpleadoService from '@/src/services/EmpleadoService';
import { Empleado } from '@/src/models/Empleado';
import { trackAllowedDynamicAccess } from 'next/dist/server/app-render/dynamic-rendering';

const useEmpleado = () => {
    const service = EmpleadoService();
    
    const getEmpleados = async () => {
        try {
            const empleados: Empleado[] = await service.getEmpleados();
            return empleados;
        } catch (error) {
            console.error("Error fetching empleados:", error);
            throw error;
        }
    }

    return {
        getEmpleados
    };
}

export default useEmpleado;