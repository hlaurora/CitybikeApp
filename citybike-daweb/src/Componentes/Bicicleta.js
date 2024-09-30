import React from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Alert from 'react-bootstrap/Alert';
import {
    Label,
    Input
} from "reactstrap";
import { useState } from 'react';
import { jwtDecode }  from 'jwt-decode';

import './css/Bicicleta.css';

const Bicicleta = ({ id, modelo, disponible, rol }) => {
    const token = sessionStorage.getItem('jwt');

    const [show, setShow] = useState(false);
    const [showAlert, setShowAlert] = useState(false);
    const [showAlertOk, setShowAlertOk] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const [moti, setMoti] = useState('');
    const handleCambios = (event) => {
        setMoti(event.target.value);
    };

    const [accion, setAccion] = useState('reservar');

    const handleBajaOk = async () => {
        try {
            const cuerpo = JSON.stringify({
                motivo: moti
            });
            const respuesta = await fetch('http://localhost:8090/estaciones/bicicletas/' + id, {
                method: 'PATCH',
                mode: 'cors',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json'
                },
                body: cuerpo
            });

            if (!respuesta.ok) {
                console.error('Error al dar de baja la bicicleta');
            }
        } catch (error) {
            console.error('Error al conectar con la API', error);
        }
        handleClose();
        window.location.reload();
    };

    const handleReserva = async () => {
        try {
            const token_decodificado = jwtDecode(token);
            setAccion('reservar');
            const cuerpo = JSON.stringify({
                idUsuario: token_decodificado.sub,
                idBicicleta: id
            });
            const respuesta = await fetch('http://localhost:8090/api/alquileres/reservas', {
                method: 'POST',
                mode: 'cors',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json'
                },
                body: cuerpo
            });

            if (respuesta.ok) {
                setShowAlertOk(true);
            }
            else if (respuesta.status === 500) {
                setShowAlert(true);
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
            console.error('Error al conectar con la API', error);
        }
    };

    const handleAlquiler = async () => {
        try {
            const token_decodificado = jwtDecode(token);
            setAccion('alquilar');
            const cuerpo = JSON.stringify({
                idUsuario: token_decodificado.sub,
                idBicicleta: id
            });
            const respuesta = await fetch('http://localhost:8090/api/alquileres', {
                method: 'POST',
                mode: 'cors',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json'
                },
                body: cuerpo
            });

            if (respuesta.ok) {
                setShowAlertOk(true);
            }
            else if (respuesta.status === 500) {
                setShowAlert(true);
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
            console.error('Error al conectar con la API', error);
        }
    };

    return (
        <div className="elemento row">
            <div className="elemento-info col-md-9">
                <h2>{id}</h2>
                <p>Modelo: {modelo}</p>
                <p>Disponibilidad: {disponible ? "Sí" : "No"}</p>
            </div>
            {rol === 'gestor' &&
                <button className="elemento-button" onClick={handleShow}>
                    Eliminar <i class="bi bi-trash-fill"></i>
                </button>

            }
            {rol === 'usuario' &&
                <div className='elemento-buttons col-md-3'>
                    <button className="elemento-button" onClick={handleReserva}>
                        Reservar
                    </button>
                    <button className="elemento-button" onClick={handleAlquiler}>
                        Alquilar
                    </button>
                </div>
            }
            <Modal id="modal_baja" show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>{id}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    ¿Seguro que deseas dar de baja la bicicleta "{id}"?

                    <div className="row pb-4 mb-4">
                        <div className="col-md-5">
                            <Label>Indique el motivo:</Label>
                        </div>
                    </div>
                    <div className="row pb-4 mb-4">
                        <div className="col-md-12">
                            <Input className="w-100" name="moti" value={moti} id="form1" type="textarea" rows="3" onChange={handleCambios} />
                        </div>
                    </div>

                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" className="elemento-button" onClick={handleClose}>
                        Cancelar
                    </Button>
                    <Button variant="secondary" className="elemento-button" onClick={handleBajaOk}>
                        Dar de baja
                    </Button>
                </Modal.Footer>
            </Modal>
            <Alert show={showAlert} variant="danger" onClose={() => setShowAlert(false)} dismissible>
                <Alert.Heading>¡Error! No se ha podido {accion} la bicicleta</Alert.Heading>
                <p>
                    Ya tienes una reserva o alquiler activo.
                </p>
            </Alert>
            <Alert show={showAlertOk} variant="success" onClose={() => { setShowAlertOk(false); window.location.reload() }} dismissible>
                <Alert.Heading>¡Bravo!</Alert.Heading>
                <p>
                    Se ha podido {accion} la bicicleta con id: {id}.
                </p>
            </Alert>
        </div>
    );
}

export default Bicicleta;
