import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import Login from './Componentes/Login';
import LoginRedirigido from './Componentes/LoginRedirigido';
import EstacionesGestor from './Componentes/Gestor/EstacionesGestor';
import EstacionesUsuario from './Componentes/Usuario/EstacionesUsuario';
import BicicletasGestor from './Componentes/Gestor/BicicletasGestor';
import BicicletasUsuario from './Componentes/Usuario/BicicletasUsuario';
import AlquileresUsuario from './Componentes/Usuario/AlquileresUsuario';


const App = () => {

  return (
        <Router>
          <div className="App">
            <Routes>
              <Route path="/" element={<Login />} />
              <Route path="/login" element={<LoginRedirigido />} />
              <Route path="/gestor/estaciones" element={<EstacionesGestor/>} />
              <Route path="/usuario/estaciones" element={<EstacionesUsuario/>} />
              <Route path="/gestor/bicicletas/:idEstacion" element={<BicicletasGestor/>} /> 
              <Route path="/usuario/bicicletas/:idEstacion" element={<BicicletasUsuario/>} /> 
              <Route path="/usuario/alquileres" element={<AlquileresUsuario/>} />
           </Routes>
          </div>
        </Router>
  );
};

export default App;




