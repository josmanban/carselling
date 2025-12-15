"use client"
import { useState, useEffect } from "react";
import LogInData from "../models/LogInData";
import AuthService from "../services/AuthService";
import { AltaEmpleado } from "../models/AltaEmpleado";
import { AltaInteresado } from "../models/AltaInteresado";
import { useContext } from "react";
import GlobalContext from "../contexts/GlogalContext";
import HttpError from "../services/HttpError";

const useAuth = ()=> { 
  const {  setIsAuthenticated } = useContext(GlobalContext);  
  
  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    setIsAuthenticated(!!token);
  }, []);
  const login = async (data:LogInData) => {    
    const authService = AuthService();    
    const response = await authService.login(data.username, data.password);    
    const accessToken = response.access_token;
    const refreshToken = response.refresh_token;
    localStorage.setItem("accessToken", accessToken);
    localStorage.setItem("refreshToken", refreshToken);
    setIsAuthenticated(true);    
  };

  const logout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    setIsAuthenticated(false);
  };

  const registrarEmpleado = async (empleadoData: AltaEmpleado) => {
    const authService = AuthService();
    return await authService.registrarEmpleado(empleadoData);
  }

  const registrarInteresado = async (interesadoData: AltaInteresado) => {
    const authService = AuthService();
    return await authService.registrarInteresado(interesadoData);

  }

  const esRol = (rol:string) => {
      if (typeof window === 'undefined') {
          return false;  // En servidor, retorna false
      }
      const accessToken = localStorage.getItem("accessToken");
      if (!accessToken) {
          return false;
      }
      const payloadBase64 = accessToken.split('.')[1];
      const payloadJson = atob(payloadBase64);
      const payload = JSON.parse(payloadJson);
      const roles: Array<string> = payload.realm_access.roles;
      return roles.includes(rol);
  };    

  const esAdmin = () => {      
      return esRol("admin");
  };

  const esEmpleado = () => {      
      return esRol("empleado");
  };

  const esInteresado = () => {      
      return esRol("interesado");
  };

  const reloadAccessToken = async () => {
      const authService = AuthService();
      return await authService.reloadAccessToken();
  }

  return {
    login,
    logout,
    registrarEmpleado,
    registrarInteresado,
    esAdmin,
    esEmpleado,
    esInteresado,
    reloadAccessToken,
  };
}

export default useAuth;