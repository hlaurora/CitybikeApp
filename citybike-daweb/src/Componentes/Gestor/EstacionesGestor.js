import React from 'react';
import { useState, useEffect } from 'react';
import Estacion from '../Estacion';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import NavbarGestor from './NavbarGestor';
import '../css/Content.css';
import '../css/Estacion.css';
import EstacionForm from '../EstacionForm';

const EstacionesGestor = () => {

    const token = sessionStorage.getItem('jwt');

    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    const [estacion, setEstacion] = useState({
        id: "id",
        nombre: 'nombre estacion',
        direccion: 'direccion',
        codigoPostal: 'Código postal',
        numPuestos: 1,
    });
    const [estaciones, setEstaciones] = useState([]);
    useEffect(() => {
        const obtenerEstaciones = async () => {
            try {
                const respuesta = await fetch('http://localhost:8090/estaciones', {
                    mode: 'cors',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json'
                    }
                });

                if (respuesta.ok) {
                    const datos = await respuesta.json();
                    setEstaciones(datos);
                } else {
                    console.error('Error al obtener los datos de la API');
                }
            } catch (error) {
                console.error('Error al conectar con la API', error);
            }
        };
        obtenerEstaciones();
    }, [token]);
    const handleCambios = (e) => {
        const { name, value } = e.target;
        setEstacion((prevEstacion) => ({
            ...prevEstacion,
            [name]: value
        }));
    };

    const handleDarDeAlta = async () => {
        try {
            const cuerpo = JSON.stringify({
                nombre: estacion.nombre,
                numPuestos: estacion.numPuestos,
                direccion: estacion.direccion,
                codigoPostal: estacion.codigoPostal,
            });
            const respuesta = await fetch('http://localhost:8090/estaciones/', {
                method: 'POST',
                mode: 'cors',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json'
                },
                body: cuerpo
            });

            if (respuesta.ok) {
                const datos = await respuesta.json();
                setEstacion(datos);
            } else {
                console.error('Error al obtener los datos de la API');
            }
        } catch (error) {
            console.error('Error al conectar con la API', error);
        }
        handleClose();
        window.location.reload();
    };


    return (
        <div>
            <div className='menu'>
                <NavbarGestor />
            </div>
            <div className="content">
                <div className='listado ps-5 pt-4'>
                    <div className='titulo'>
                        <h1>Listado de Estaciones</h1>
                        <button className="button-nueva" onClick={handleShow}> <i class="bi bi-plus-circle-fill" /> Nueva Estación</button>
                    </div>
                    {estaciones.map((esta, index) => (
                        <Estacion
                            key={index}
                            id={esta.id}
                            nombre={esta.nombre}
                            direccion={esta.direccion}
                            puestosDisponibles={esta.puestosDisponibles}
                            codigoPostal={esta.codigoPostal}
                            rol="gestor"
                        />
                    ))}
                </div>
                <Modal show={show} onHide={handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Nueva Estación</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <EstacionForm
                            nombre={estacion.nombre}
                            direccion={estacion.direccion}
                            codigoPostal={estacion.codigoPostal}
                            numPuestos={estacion.numPuestos}
                            //latitud={estacion.latitud}
                            //longitud={estacion.longitud}
                            crear={true}
                            editar={true}
                            handleCambios={handleCambios} />
                    </Modal.Body>

                    <Modal.Footer>
                        <Button variant="secondary" className="estacion-button" onClick={handleClose}>
                            Cancelar
                        </Button>
                        <Button variant="secondary" className="estacion-button" onClick={handleDarDeAlta}>
                            Dar de alta
                        </Button>
                    </Modal.Footer>
                </Modal>
            </div>
        </div >
    );
}

export default EstacionesGestor;