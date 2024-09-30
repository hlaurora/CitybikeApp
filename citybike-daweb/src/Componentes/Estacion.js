import React from 'react';
import { useState, useEffect } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import EstacionForm from './EstacionForm';
import './css/Estacion.css';
import { Link } from 'react-router-dom';

const Estacion = ({ id, nombre, direccion, codigoPostal, puestosDisponibles, rol }) => {
    const token = sessionStorage.getItem('jwt');

    const [showEditar, setShowEditar] = useState(false);
    const [showEliminar, setShowEliminar] = useState(false);
    const [showError, setShowError] = useState(false);
    const [showInfo, setShowInfo] = useState(false);

    const handleCloseEditar = () => setShowEditar(false);
    const handleShowEditar = () => setShowEditar(true);

    const handleCloseInfo = () => setShowInfo(false);
    const handleShowInfo = () => setShowInfo(true);

    const handleCloseEliminar = () => setShowEliminar(false);
    const handleShowEliminar = () => setShowEliminar(true);

    const handleCloseError = () => setShowError(false);


    const [estacion, setEstacion] = useState([]);
    useEffect(() => {
        const obtenerEstacion = async () => {
            try {
                const respuesta = await fetch('http://localhost:8090/estaciones/info/' + id, {
                    method: 'GET',
                    mode: 'cors',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json'
                    }
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
        };
        obtenerEstacion();
    }, [id, token]);

    const handleCambios = (e) => {
        const { name, value } = e.target;
        setEstacion((prevEstacion) => ({
            ...prevEstacion,
            [name]: value
        }));
    };

    const handleGuardarCambios = async () => {
        try {
            const cuerpo = JSON.stringify({
                nombre: estacion.nombre,
                numPuestos: estacion.numPuestos,
                direccion: estacion.direccion,
                codigoPostal: estacion.codigoPostal
            });
            const respuesta = await fetch('http://localhost:8090/estaciones/' + id, {
                method: 'PUT',
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
        handleCloseEditar();
        window.location.reload();
    };

    const handleEliminarOk = async () => {
        try {
            const respuesta = await fetch('http://localhost:8090/estaciones/' + id, {
                method: 'DELETE',
                mode: 'cors',
                headers: {
                    'Authorization': 'Bearer ' + token
                }
            });
            if (respuesta.ok) {
                handleCloseEliminar();
                window.location.reload();
            }
            else if (respuesta.status === 500) {
                setShowError(true);
                setShowEliminar(false);
            }
            else {
                console.error('Error al obtener los datos de la API');
                handleCloseEliminar();
                window.location.reload();
            }
        } catch (error) {
            console.error('Error al conectar con la API', error);
        }
    };

    return (
        <div className="estacion">
            <div className="estacion-info">
                <h2>{nombre} </h2>
                <p><i class="bi bi-geo-fill"></i>Ubicación: {direccion}</p>
                <p>Código postal: {codigoPostal}</p>
                <p>Puestos disponibles: {puestosDisponibles}</p>
            </div>
            {rol==="gestor" && 
            <div className="estacion-buttons">
                <button className="estacion-button" onClick={handleShowEditar}>Editar <i class="bi bi-pencil-fill"></i> </button>
                <button className="estacion-button" onClick={handleShowEliminar}> Eliminar <i class="bi bi-trash-fill"></i> </button>
                <button className="estacion-button" > <Link to={`/gestor/bicicletas/${estacion.id}`} style={{ textDecoration: 'none', color: 'black' }}>Ver bicis <i class="bi bi-search"></i></Link></button>
            </div>
            }
            {rol==="usuario" && 
            <div className="estacion-buttons">
                <button className="estacion-button" onClick={handleShowInfo}>Ver estación</button>
                <button className="estacion-button" > <Link to={`/usuario/bicicletas/${estacion.id}`} style={{ textDecoration: 'none', color: 'black' }}>Ver bicis <i class="bi bi-search"></i></Link></button>
            </div>
            }

            <Modal id="modal_editar" show={showEditar} onHide={handleCloseEditar}>
                <Modal.Header closeButton>
                    <Modal.Title>{nombre}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <EstacionForm
                        nombre={estacion.nombre}
                        direccion={estacion.direccion}
                        codigoPostal={estacion.codigoPostal}
                        numPuestos={estacion.numPuestos}
                        crear={false}
                        editar={true}
                        handleCambios={handleCambios} />
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" className="estacion-button" onClick={handleCloseEditar}>
                        Cancelar
                    </Button>
                    <Button variant="secondary" className="estacion-button" onClick={handleGuardarCambios}>
                        Guardar cambios
                    </Button>
                </Modal.Footer>
            </Modal>
            <Modal id="modal_eliminar" show={showEliminar} onHide={handleCloseEliminar}>
                <Modal.Header closeButton>
                    <Modal.Title>{nombre}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    ¿Seguro que deseas eliminar la estación "{nombre}"?
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" className="estacion-button" onClick={handleCloseEliminar}>
                        Cancelar
                    </Button>
                    <Button variant="secondary" className="estacion-button" onClick={handleEliminarOk}>
                        Eliminar
                    </Button>
                </Modal.Footer>
            </Modal>
            <Modal id="modal_error" show={showError} onHide={handleCloseError}>
                <Modal.Header closeButton>
                    <Modal.Title>Error</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    No se ha podido eliminar la estación ya que tiene bicicletas estacionadas
                </Modal.Body>
            </Modal>
            <Modal id="modal_info" show={showInfo} onHide={handleCloseInfo}>
            <Modal.Header closeButton>
                    <Modal.Title>{nombre}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <EstacionForm
                        nombre={estacion.nombre}
                        direccion={estacion.direccion}
                        codigoPostal={estacion.codigoPostal}
                        numPuestos={estacion.numPuestos}
                        crear={false}
                        editar={false} />
                </Modal.Body>
            </Modal>
        </div>  
    

    );
}

export default Estacion;
