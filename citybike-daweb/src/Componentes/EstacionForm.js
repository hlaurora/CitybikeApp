import React from 'react';
import {
    Label,
    Input
} from "reactstrap";
import './css/Estacion.css';

const EstacionForm = ({ nombre, direccion, codigoPostal, numPuestos, editar, handleCambios }) => {
    if (nombre === undefined) { nombre = 'Nombre de la estación' }
    if (direccion === undefined) { direccion = 'Dirección'}
    if (codigoPostal === undefined) { codigoPostal = 'Código postal' }
    if (numPuestos === undefined) { numPuestos = 1 }
    return (
        <div className="estacion-formulario">
            <div className="row pb-4 mb-4">
                <div className="col-md-5">
                    <Label>Nombre:</Label>
                </div>
                <div className="col-md-7">
                    <Input className="w-100" name="nombre" value={nombre} id="form1" type="text" onChange={handleCambios} readOnly={!editar} />
                </div>
            </div>
            <div className="row pb-4 mb-4">
                <div className="col-md-5">
                    <Label>Dirección:</Label>
                </div>
                <div className="col-md-7">
                    <Input className="w-100" name="direccion" value={direccion} id="form2" type="text" onChange={handleCambios} readOnly={!editar} />
                </div>
            </div>
            <div className="row pb-4 mb-4">
                <div className="col-md-5">
                    <Label>Código Postal:</Label>
                </div>
                <div className="col-md-7">
                    <Input className="w-100" name="codigoPostal" value={codigoPostal} id="form2" type="text" onChange={handleCambios} readOnly={!editar} />
                </div>
            </div>
            <div className="row pb-4 mb-4">
                <div className="col-md-5">
                    <Label>Número de puestos:</Label>
                </div>
                <div className="col-md-7">
                    <Input className="w-100" name="numPuestos" value={numPuestos} id="form3" type="number" onChange={handleCambios} readOnly={!editar} />
                </div>
            </div>
        </div>
    );
}

export default EstacionForm;
