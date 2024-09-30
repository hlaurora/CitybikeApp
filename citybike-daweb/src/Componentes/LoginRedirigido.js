import React from 'react';
import 'bootstrap-icons/font/bootstrap-icons.css';
import './css/Login.css';
import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';

function LoginRedirigido() {
    const navigate = useNavigate();

    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const token = urlParams.get('token');
        if (token) {
          sessionStorage.setItem('jwt', token);
          if(sessionStorage.getItem("Rol")==='gestor'){
            navigate("/gestor/estaciones");
          }else{
            navigate("/usuario/estaciones");
          }
        }
      }, []);

  return (
    <div className="login">
        <h1>Redirigiendo...</h1>
      </div>
  );
}

export default LoginRedirigido;