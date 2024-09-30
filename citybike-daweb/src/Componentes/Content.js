import React from 'react';
import './css/Content.css'; 

const Content = () => {
  return (
    <div className="content">
      <section id="home">
        <h2>Inicio</h2>
        <p>Contenido de la sección Inicio.</p>
      </section>
      <section id="about">
        <h2>Acerca de</h2>
        <p>Contenido de la sección Acerca de.</p>
      </section>
      <section id="services">
        <h2>Servicios</h2>
        <p>Contenido de la sección Servicios.</p>
      </section>
      <section id="contact">
        <h2>Contacto</h2>
        <p>Contenido de la sección Contacto.</p>
      </section>
    </div>
  );
};

export default Content;
