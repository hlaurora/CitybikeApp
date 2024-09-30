import React from 'react';
import { useState, useEffect } from 'react';
import Alquiler from './Alquiler';
import '../css/Content.css';
import NavbarUsuario from './NavbarUsuario';

const AlquileresUsuario = () => {
    const token = sessionStorage.getItem('jwt');

    const [alquileres, setAlquileres] = useState([]);
    const [activo, setActivo] = useState({});
    const [tipoActivo, setTipoActivo] = useState(0);

    useEffect(() => {
        const obtenerAlquileres = async () => {
            try {
                const respuesta = await fetch('http://localhost:8090/api/alquileres/usuario', {
                    mode: 'cors',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json'
                    }
                });
                if (respuesta.ok) {
                    const datos = await respuesta.json();
                    setAlquileres(datos.alquileres);
                } else {
                    console.error('Error al obtener los datos de la API');
                }
            } catch (error) {
                console.error('Error al conectar con la API', error);
            }
        };

        const obtenerActivo = async () => {
            try {
                const respuesta = await fetch('http://localhost:8090/api/alquileres/activo', {
                    mode: 'cors',
                    method: 'GET',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json'
                    }
                });
                if (respuesta.status === 204) {
                    const respuesta = await fetch('http://localhost:8090/api/alquileres/reservas/activa', {
                        mode: 'cors',
                        method: 'GET',
                        headers: {
                            'Authorization': 'Bearer ' + token,
                            'Content-Type': 'application/json'
                        }
                    });
                    if (respuesta.ok && respuesta.status!==204) {
                        const datos = await respuesta.json();
                        setActivo(datos);
                        setTipoActivo(1);
                    }
                }
                else if (respuesta.ok) {
                    const datos = await respuesta.json();
                    setActivo(datos);
                    setTipoActivo(2);
                }
            } catch (error) {
                console.error('Error al conectar con la API', error);
            }
        };
        obtenerAlquileres();
        obtenerActivo();
    }, [token]);

    return (
        <div>
            <div className='menu'>
                <NavbarUsuario />
            </div>
            <div className="content">
                {tipoActivo !== 0 &&
                    <div className='listado ps-5 pt-4'>
                        <div className='titulo'>
                            <h1>{tipoActivo===1?"Reserva activa:":"Alquiler activo:"}</h1>
                        </div>
                        <Alquiler
                            idBicicleta={activo.idBicicleta}
                            inicio={tipoActivo === 1 ? activo.creada : activo.inicio}
                            fin={tipoActivo === 1 ? activo.caducidad : activo.fin}
                            activo={true}
                            token={token}
                            tipo={tipoActivo}
                        />
                    </div>
                }
                <div className='listado ps-5 pt-4'>
                    <div className='titulo'>
                        <h1>Listado de Alquileres del usuario</h1>
                    </div>
                    {alquileres.map((alquiler, index) => (
                        <Alquiler
                            key={index}
                            idBicicleta={alquiler.idBicicleta}
                            inicio={alquiler.inicio}
                            fin={alquiler.fin}
                            activo={false}
                            token={token}
                            tipo={2}
                        />
                    ))}
                </div>
            </div>
        </div >
    );
}

export default AlquileresUsuario;