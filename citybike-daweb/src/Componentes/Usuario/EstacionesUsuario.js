import React from 'react';
import { useState, useEffect } from 'react';
import Estacion from '../Estacion';
import NavbarUsuario from './NavbarUsuario';
import '../css/Content.css';
import '../css/Estacion.css';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import {
    Label,
    Input
} from "reactstrap";

const EstacionesUsuario = () => {
    const token = sessionStorage.getItem('jwt');

    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    const [minPuestos, setMinPuestos] = useState(0);
    const [codigoPostalFiltro, setCodigoPostalFiltro] = useState('');
    const [nombreEstacionFiltro, setNombreEstacionFiltro] = useState('');


    const [estaciones, setEstaciones] = useState([]);
    const [estacionesOriginales, setEstacionesOriginales] = useState([]);
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
                    setEstacionesOriginales(datos);
                } else {
                    console.error('Error al obtener los datos de la API');
                }
            } catch (error) {
                console.error('Error al conectar con la API', error);
            }
        };
        obtenerEstaciones();
    }, [token]);


    const handleFiltros = async () => {
        const filtradas = estacionesOriginales.filter(esta => {
            let cumpleFiltros = true;

            if (minPuestos > 0) {
                cumpleFiltros = cumpleFiltros && (esta.puestosDisponibles >= minPuestos);
            }
            if (codigoPostalFiltro !== "") {
                cumpleFiltros = cumpleFiltros && (esta.codigoPostal === codigoPostalFiltro);
            }
            if (nombreEstacionFiltro !== "") {
                cumpleFiltros = cumpleFiltros && (esta.nombre && esta.nombre.includes(nombreEstacionFiltro));
            }

            return cumpleFiltros;
        });

        setEstaciones(filtradas);
        handleClose();
    };

    return (
        <div>
            <div className='menu'>
                <NavbarUsuario />
            </div>
            <div className="content">
                <div className='listado ps-5 pt-4'>
                    <div className='titulo'>
                        <h1>Listado de Estaciones</h1>
                        <button className="button-filtros" onClick={handleShow}> <i class="bi bi-filter"></i>  Filtros</button>
                    </div>
                    {estaciones.map((esta, index) => (
                        <Estacion
                            key={index}
                            id={esta.id}
                            nombre={esta.nombre}
                            direccion={esta.direccion}
                            codigoPostal={esta.codigoPostal}
                            puestosDisponibles={esta.puestosDisponibles}
                            rol="usuario"
                        />
                    ))}
                </div>
                <Modal show={show} onHide={handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Filtros del listado</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <div className="row pb-4 mb-4">
                            <div className="col-md-8">
                                <Label>Número mínimo de puestos disponibles:</Label>
                            </div>
                            <div className="col-md-4">
                                <Input className="w-100" name="nombre" value={minPuestos} id="form1" type="number" onChange={e => setMinPuestos(e.target.value)} />
                            </div>
                        </div>
                        <div className="row pb-4 mb-4">
                            <div className="col-md-8">
                                <Label>Código postal:</Label>
                            </div>
                            <div className="col-md-4">
                                <Input className="w-100" name="nombre" value={codigoPostalFiltro} id="form1" type="text" onChange={e => setCodigoPostalFiltro(e.target.value || '')} />
                            </div>
                        </div>
                        <div className="row pb-4 mb-4">
                            <div className="col-md-8">
                                <Label>Nombre parcial de la estación:</Label>
                            </div>
                            <div className="col-md-4">
                                <Input className="w-100" name="nombre" value={nombreEstacionFiltro} id="form1" type="text" onChange={e => setNombreEstacionFiltro(e.target.value || '')} />
                            </div>
                        </div>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" className="estacion-button" onClick={handleClose}>
                            Cancelar
                        </Button>
                        <Button variant="secondary" className="estacion-button" onClick={handleFiltros}>
                            Aplicar filtros
                        </Button>
                    </Modal.Footer>
                </Modal>
            </div>
        </div >
    );
}

export default EstacionesUsuario;