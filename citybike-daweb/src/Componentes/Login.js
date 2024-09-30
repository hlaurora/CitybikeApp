import React from 'react';
import 'bootstrap-icons/font/bootstrap-icons.css';
import './css/Login.css';
import { useState } from 'react';

function Login() {
  const [rol, setRol] = useState('usuario');

  const handleOauth = () => {
    sessionStorage.setItem("Rol", rol);
    const authUrl = 'http://localhost:8090/auth/oauth2';
    window.location.href = authUrl;
  };

  const handleCambioRol = (e) => {
    setRol(e.target.value);
  };

  return (
    <div className="container-fluid vh-100">
      <div className="row h-100">
        <div className="col-md-6">
          <div className="gradient-form h-100 d-flex flex-column ps-5">
            <div className="d-flex flex-row align-items-center justify-content-center pb-4 mt-4">
              <img src="bicicleta.png" alt='Bicicleta' width="90" height="90"></img>
            </div>
            <div className="d-flex flex-row align-items-center justify-content-center pb-2 mb-2">
              <h4 className="titulo-city mt-1 mb-5 pb-1">Bienvenido/a a CityBike</h4>
            </div>
            <div className="d-flex flex-row align-items-center justify-content-center ">
              <p>Iniciar sesión como:</p>
            </div>
            <div className="d-flex flex-row align-items-center justify-content-center pb-4 mb-4">
              <form>
                <input type="radio" id="gestor" name="role" value="gestor" onChange={handleCambioRol} checked={rol === 'gestor'} />
                <label className='me-4' htmlFor="gestor">Gestor</label>
                <input type="radio" id="usuario" name="role" value="usuario" onChange={handleCambioRol} checked={rol === 'usuario'} />
                <label htmlFor="usuario">Usuario</label>
              </form>
            </div>
            <div className="d-flex flex-row align-items-center justify-content-center pb-4 mb-4">
              <p className="mb-0">Identifícate con GitHub</p>
              <button className="btn btn-outline mx-2" onClick={handleOauth}>
                <i className="bi bi-github"></i>
              </button>
            </div>
          </div>
        </div>
        <div className="col-md-6">
          <div className="d-flex flex-column justify-content-center gradient-custom-2 h-100">
            <div className="text-white px-3 py-4 p-md-5 mx-md-4">
              <h4 className="mb-4">Alquila una bicicleta para hacer deporte, contribuir al medioambiente y desplazarte por la ciudad de manera saludable y divertida</h4>
              <p className="small mb-0">
                ¡Únete a nosotros para un estilo de vida más ecológico y activo!
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>

  );
}

export default Login;