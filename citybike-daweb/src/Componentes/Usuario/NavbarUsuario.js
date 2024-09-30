import React from 'react';
import '../css/Navbar.css'; 
import { Link } from 'react-router-dom';
import { jwtDecode }  from 'jwt-decode';

const NavbarUsuario = () => {
  const token = sessionStorage.getItem('jwt');
  const token_decodificado = jwtDecode(token);

return (
    <div className="navbar container-fluid">
      <div className="row w-100">
        <div className="col-md-4 d-flex align-items-center">
          <h1 className="mb-0 ms-2">CityBike</h1>
        </div>
        <div className="col-md-4 d-flex justify-content-center">
          <nav>
            <ul className="nav justify-content-end">
              <li className="nav-item">
                <Link className="nav-link" to="/usuario/estaciones">Estaciones</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/usuario/alquileres">Mis alquileres</Link>
              </li>
            </ul>
          </nav>
        </div>
        <div className="col-md-4 d-flex justify-content-end align-items-center">
          <p className="mb-0">{token_decodificado.Nombre}</p>
        </div>
      </div>
    </div>
  );
};

export default NavbarUsuario;
