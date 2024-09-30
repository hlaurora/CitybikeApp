import React from 'react';
import { useState, useEffect } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Bicicleta from '../Bicicleta';
import '../css/Content.css';
import { useParams } from 'react-router-dom';
import NavbarGestor from './NavbarGestor';
import BicicletaForm from './BicicletaForm';

const BicicletasGestor = () => {
    const token = sessionStorage.getItem('jwt');

    const { idEstacion } = useParams(); // Obtener el parámetro idEstacion de la URL
    const [show, setShow] = useState(false);
    const [showError, setShowError] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const handleCloseError = () => setShowError(false);

    const [bicicleta, setBicicleta] = useState({
        modelo: "Modelo de la bicicleta"
    });
    const [bicicletas, setBicicletas] = useState([]);
   
    useEffect(() => {
        const obtenerBicicletas = async () => {
            try {
                const respuesta = await fetch('http://localhost:8090/estaciones/' + idEstacion + '/bicicletas', {
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

    const handleCambios = (e) => {
        const { name, value } = e.target;
        setBicicleta((prevBicicleta) => ({
            ...prevBicicleta,
            [name]: value
        }));
    };

    const handleDarDeAlta = async () => {
        try {
            const cuerpo = JSON.stringify({
                modelo: bicicleta.modelo,
                idEstacion: idEstacion,
            });
            const respuesta = await fetch('http://localhost:8090/estaciones/bicicletas', {
                method: 'POST',
                mode: 'cors',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json'
                },
                body: cuerpo
            });
            if (respuesta.ok) {
                handleClose();
                window.location.reload();
            }
            else if (respuesta.status === 500) {
                handleClose();
                setShowError(true);
            }
            else {
                console.error('Error al obtener los datos de la API');
                handleClose();
            }
        } catch (error) {
            console.error('Error al conectar con la API', error);
        }
    };

    return (
        <div>
            <div className='menu'>
                <NavbarGestor />
            </div>
            <div className="content">
                <div className='listado ps-5 pt-4'>
                    <div className='titulo'>
                        <h1>Listado de Bicicletas</h1>
                        <button className="button-nueva" onClick={handleShow}> <i class="bi bi-plus-circle-fill" /> Nueva Bicicleta</button>
                    </div>
                    {bicicletas.map((bici, index) => (
                        <Bicicleta
                            key={index}
                            id={bici.id}
                            modelo={bici.modelo}
                            disponible={bici.disponible}
                            rol='gestor'
                        />
                    ))}
                </div>
                <Modal show={show} onHide={handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Nueva Bicicleta</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <BicicletaForm
                            idEstacion={idEstacion}
                            modelo={bicicleta.modelo}
                            disponible={bicicleta.disponible}
                            handleCambios={handleCambios}
                        />
                    </Modal.Body>

                    <Modal.Footer>
                        <Button variant="secondary" className="estacion-button" onClick={handleClose}>
                            Cancelar
                        </Button>
                        <Button variant="secondary" className="estacion-button" onClick={handleDarDeAlta}>
                            Añadir
                        </Button>
                    </Modal.Footer>
                </Modal>
                <Modal id="modal_error" show={showError} onHide={handleCloseError}>
                    <Modal.Header closeButton>
                        <Modal.Title>Error</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        No se ha podido dar de alta la bicicleta ya que la estación no tiene puestos libres
                    </Modal.Body>
                </Modal>
            </div>
        </div >
    );
}

export default BicicletasGestor;