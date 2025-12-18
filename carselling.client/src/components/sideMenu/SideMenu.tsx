'use client'
import {
  ArchiveBoxXMarkIcon,
  ChevronDownIcon,
  PencilIcon,
  Square2StackIcon,
  TrashIcon,
} from '@heroicons/react/16/solid'
import Link from '@mui/material/Link';
import useAuth from '@/src/hooks/useAuth';

import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import InboxIcon from '@mui/icons-material/MoveToInbox';
import MailIcon from '@mui/icons-material/Mail';
import Divider from '@mui/material/Divider';
import { useRouter } from 'next/navigation';
import { DisplaySettings } from '@mui/icons-material';


export default function SideMenu(){
    const {push} = useRouter();
    const {esAdmin, esInteresado, esEmpleado, logout } = useAuth();
    return (
        <List>
            { esInteresado() &&  
            <>                
              <ListItem key={1} disablePadding>
                <ListItemButton>                  
                  <Link
                      color="inherit"
                      underline="none"
                      href="#"                      
                      onClick={(e) => {
                        e.preventDefault();
                        push("/logedIn/actualizarPosicion");
                      }}
                      >
                        <span>Actualizar posición vehículo</span>
                  </Link>
                </ListItemButton>
              </ListItem>                
            </>
            }
            { esAdmin() &&  
            <>                
              <ListItem key={1} disablePadding>
                <ListItemButton>                  
                  <Link
                      color="inherit"
                      underline="none"
                      href="#"                        
                      onClick={(e) => {
                        e.preventDefault();
                        push("/logedIn/pruebasPorVehiculo");
                      }}
                      >
                        <span>Pruebas por vehículo</span>
                  </Link>
                </ListItemButton>
              </ListItem>
              <ListItem key={2} disablePadding>
                <ListItemButton>                  
                  <Link
                      color="inherit"
                      underline="none"
                      href="#"                      
                      onClick={(e) => {
                        e.preventDefault();
                        push("/logedIn/distanciaPorVehiculo");
                      }}
                      >
                        <span>Distancia recorrida por vehiculo en periodo</span>
                  </Link>
                </ListItemButton>
              </ListItem>
              <ListItem key={3} disablePadding>
                <ListItemButton>                  
                  <Link
                      color="inherit"
                      underline="none"
                      href="#"                      
                      onClick={(e) => {
                        e.preventDefault();
                        push("/logedIn/incidencias");
                      }}
                      >
                        <span>Incidencias</span>
                  </Link>
                </ListItemButton>
              </ListItem>
            </>
            }
            {esEmpleado() &&
            <>
                <ListItem key={1} disablePadding>
                  <ListItemButton>
                    <ListItemText>                    
                    <Link
                        color="inherit"
                        underline="none"
                        href="#"                        
                        onClick={(e) => {
                          e.preventDefault();
                          push("/logedIn/registrarPrueba");
                        }}                       
                        >
                          Registrar prueba
                    </Link>
                          </ListItemText>
                  </ListItemButton>
                </ListItem>                
                <ListItem key={2} disablePadding>
                  <ListItemButton>                    
                    <Link
                        color="inherit"
                        underline="none"
                        href="#"                        
                        onClick={(e) => {
                          e.preventDefault();
                          push("/logedIn/pruebasEnCurso");
                        }}
                        >
                          <span>Pruebas en curso</span>
                    </Link>
                  </ListItemButton>
                </ListItem>
                <ListItem key={3} disablePadding>
                  <ListItemButton>                    
                    <Link
                        color="inherit"
                        underline="none"
                        href="#"                        
                        onClick={(e) => {
                          e.preventDefault();
                          push("/logedIn/notificaciones");
                        }}
                        >
                          <span>Notificaciones</span>
                    </Link>
                  </ListItemButton>
                </ListItem>
                <ListItem key={4} disablePadding>
                  <ListItemButton>                    
                    <Link
                        color="inherit"
                        underline="none"
                        href="#"                        
                        onClick={(e) => {
                          e.preventDefault();
                          push("/logedIn/vehiculos");
                        }}
                        >
                          <span>Vehiculos</span>
                    </Link>
                  </ListItemButton>
                </ListItem>
            </>
            }
            <Divider />
            <ListItem key={99} disablePadding>
              <ListItemButton>                
                <Link
                    color="inherit"
                    underline="none"
                    href="#"                    
                    onClick={(e) => {
                      e.preventDefault();
                      logout();
                      push("/auth/logIn");
                    }}
                    >
                      <span>Cerrar sesión</span>
                </Link>
              </ListItemButton>
            </ListItem>            
        </List>
    );
}