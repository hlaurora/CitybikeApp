import React from 'react';

import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import '../css/Bicicleta.css';
import { useState, useEffect } from 'react';

const Alquiler = ({ idBicicleta, inicio, fin, activo, tipo }) => {
    const token = sessionStorage.getItem('jwt');

    const [show, setShow] = useState(false);
    const [estacionSeleccionada, setEstacionSeleccionada] = useState('');

    const handleSelectChange = (event) => {
        setEstacionSeleccionada(event.target.value);
    };
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
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
    let labelFin = "Fecha";

    if (fin === null) { fin = "Sin finalizar" }
    if (tipo === 1) {
        tipo = "Reserva"
        labelFin = "Fecha de caducidad";
    }
    if (tipo === 2) {
        tipo = "Alquiler"
        labelFin = "Fecha de fin";
    }

    const handleCancelar = async () => {
        try {
            const respuesta = await fetch('http://localhost:8090/api/alquileres/reservas/activa', {
                method: 'DELETE',
                mode: 'cors',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json'
                }
            });

            if (respuesta.ok) {
                window.location.reload();
            }
            else {
                console.error('Error al cancelar la reserva');
            }
        } catch (error) {
            console.error('Error al conectar con la API', error);
        }

    };

    const handleConfirmar = async () => {
        try {
            const respuesta = await fetch('http://localhost:8090/api/alquileres/reservas/activa', {
                method: 'POST',
                mode: 'cors',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json'
                }
            });
            if (respuesta.ok) {
                window.location.reload();
            }
            else {
                console.error('Error al confirmar la reserva');
            }
        } catch (error) {
            console.error('Error al conectar con la API', error);
        }
    };

    const handleAparcar = async () => {
        if (estacionSeleccionada === '' || estacionSeleccionada === 'Selecciona una opción') {
            return;
        }
        try {
            console.log(estacionSeleccionada);
            const cuerpo = new URLSearchParams();
            cuerpo.append('idEstacion', estacionSeleccionada);
            const respuesta = await fetch('http://localhost:8090/api/alquileres/activo', {
                method: 'PATCH',
                mode: 'cors',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: cuerpo
            });
            if (respuesta.ok) {
                window.location.reload();
            }
            else {
                const contentType = respuesta.headers.get("content-type");
                if (contentType && contentType.indexOf("application/json") !== -1) {
                    const errorData = await respuesta.json();
                    console.error('Error al estacionar la bicicleta: ' + errorData.message);
                } else {
                    const errorText = await respuesta.text();
                    console.error('Error al estacionar la bicicleta (no JSON): ' + errorText);
                }
            }
        } catch (error) {
            console.error('Error ', error);
        }
    };

    return (
        <div className="elemento row">
            <div className="elemento-info col-md-9">
                <h2>{tipo}</h2>
                <p>Identificador de la bicicleta: {idBicicleta}</p>
                <p>Fecha de inicio: {inicio}</p>
                <p>{labelFin}: {fin}</p>
            </div>
            {
                activo && tipo === "Alquiler" &&
                <div className="elemento-buttons col-md-3">
                    <button className="elemento-button" onClick={handleShow}>
                        Aparcar
                    </button>
                </div>
            }
            {
                activo && tipo === "Reserva" &&
                <div className="elemento-buttons col-md-3">
                    <button className="elemento-button" onClick={handleConfirmar}>
                        Confirmar
                    </button>
                    <button className="elemento-button" onClick={handleCancelar}>
                        Cancelar
                    </button>
                </div>
            }
            <Modal id="modal_aparcar" show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Aparcar bicicleta</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    Selecciona la estación en la que aparcar la bicicleta:
                    <div className="row pb-4 mb-4">
                        <Form.Select aria-label="Selecciona una estacion"
                            value={estacionSeleccionada}
                            onChange={handleSelectChange}>
                            <option key={"Selecciona una opción"}>Selecciona una opción</option>
                            {estaciones.map((estacion) => (
                                <option key={estacion.id} value={estacion.id}>
                                    {`${estacion.nombre}: ${estacion.id}`}
                                </option>
                            ))}
                        </Form.Select>
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" className="elemento-button" onClick={handleClose}>
                        Cancelar
                    </Button>
                    <Button variant="secondary" className="elemento-button" onClick={handleAparcar}>
                        Aceptar
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}

export default Alquiler;
