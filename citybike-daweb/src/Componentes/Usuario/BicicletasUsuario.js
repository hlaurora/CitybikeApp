import React from 'react';
import { useState, useEffect } from 'react';
import Bicicleta from '../Bicicleta';
import '../css/Content.css';
import { useParams } from 'react-router-dom';
import NavbarUsuario from './NavbarUsuario';

const BicicletasUsuario = () => {
    const token = sessionStorage.getItem('jwt');

    const { idEstacion } = useParams(); // Obtener el parámetro idEstacion de la URL
    const [bicicletas, setBicicletas] = useState([]);
     
    useEffect(() => {
        const obtenerBicicletas = async () => {
            try {
                const respuesta = await fetch('http://localhost:8090/estaciones/' + idEstacion + '/bicicletas/disponibles', {
                    mode: 'cors',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json'
                    }
                });
                if (respuesta.ok) {
                    const datos = await respuesta.json();
                    setBicicletas(datos);
                } else {
                    console.error('Error al obtener los datos de la API');
                }
            } catch (error) {
                console.error('Error al conectar con la API', error);
            }
        };
        obtenerBicicletas();
    }, [token, idEstacion]);


    return (
        <div>
            <div className='menu'>
                <NavbarUsuario />
            </div>
            <div className="content">
            <div className='listado ps-5 pt-4'>
                <div className='titulo'>
                    <h1>Listado de Bicicletas</h1>
                </div>
                {bicicletas.map((bici, index) => (
                    <Bicicleta
                        key={index}
                        id={bici.id}
                        modelo={bici.modelo}
                        disponible={bici.disponible}
                        rol='usuario'
                    />
                ))}
            </div>
            </div>
        </div >
    );
}

export default BicicletasUsuario;