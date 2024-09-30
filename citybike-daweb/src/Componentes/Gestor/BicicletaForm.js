import React from 'react';
import {
    Label,
    Input
} from "reactstrap";
import '../css/Bicicleta.css';

const BicicletaForm = ({ modelo, idEstacion, disponible, handleCambios }) => {
    if (modelo === undefined) { modelo = 'Modelo de la bicicleta' }
    if (idEstacion === undefined) { idEstacion = 'Estación en la que se encuentra' }
    if (disponible === undefined) { disponible = true }
    return (
        <div className="bicicleta-formulario">
            <div className="row pb-4 mb-4">
                <div className="col-md-5">
                    <Label>Modelo:</Label>
                </div>
                <div className="col-md-7">
                    <Input className="w-100" name="modelo" value={modelo} id="form1" type="text" onChange={handleCambios}/>
                </div>
            </div>
            <div className="row pb-4 mb-4">
                <div className="col-md-5">
                    <Label>Estacion:</Label>
                </div>
                <div className="col-md-7">
                    <Input className="w-100" name="idEstacion" value={idEstacion} id="form2" type="text" onChange={handleCambios} readOnly={true}/>
                </div>
            </div>
            <div className="row pb-4 mb-4">
                <div className="col-md-5">
                    <Label>Disponible:</Label>
                </div>
                <div className="col-md-7">
                    <Input className="w-100" name="disponible" value={disponible} id="form3" type="boolean" onChange={handleCambios} readOnly={true} />
                </div>
            </div>
           
        </div>
    );
}

export default BicicletaForm;
