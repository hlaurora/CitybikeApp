# CitybikeApp

Proyecto desarrollado para las asignaturas de Aplicaciones Distribuidas(AADD), Arquitectura del Software(ArSo) y Desarrollo de Aplicaciones Web(DAWEB), curso 2023-2024. La aplicación gestiona un servicio de préstamo de bicicletas en la ciudad. La web permite dar de alta estaciones de aparcamiento y bicicletas, consultar la información relativa a las mismas, gestionar los alquileres y reservas de las bicicletas. 

**Despliegue**

cd al directorio pasarela donde está el docker compose y ejecutar `docker-compose up` 
Abrir directorio citybike-daweb y `npm start`

Para poner en marcha el proyecto, primero hay que poner en marcha el servidor. Para
ello, tenemos que tener instalado *Docker Desktop*, luego, accedemos a la terminal y, en la carpeta del proyecto (Citybike → **pasarela**),
ejecutamos `docker-compose up -d`. Esto desplegará todos los contenedores necesarios para
las bases de datos, los microservicios, y la comunicación entre ellos.
Por otro lado, en la carpeta en la que se encuentra el proyecto React de esta asignatura,
ejecutamos `npm install `para instalar todas las dependencias del proyecto. Una vez que esté
en marcha el contenedor, ejecutamos en esta misma carpeta `npm start` y la aplicación estará
en marcha.

# Arquitectura

La arquitectura usada tiene dos partes, el frontend desarrollado en la asignatura DAWEB y el
backend desarrollado en Arquitectura del Software. El backend se desarrolló siguiendo las
especificaciones de AS, con varios microservicios implementados en Java 8 y un canal de
comunicación con API REST. Para almacenar los datos, se usan MySQL y MongoDB dentro
de contenedores Docker.
Para el frontend se ha creado una aplicación React, haciendo uso de las tecnologías
JavaScript, Bootstrap basado en grid layout y CSS. En la carpeta Componentes se incluyen
los componentes creados para cada página. Las carpetas Componentes/Gestor y
Componentes/Usuario contienen las páginas que son exclusivas para cada rol.

## Contenedores y microservicios

Dentro del directorio citybike-arso, encontramos los distintos servicios que conforman el backend, los cuales se integran mediante contenedores.

* Estaciones: Gestiona las bicicletas y estaciones, ofrece funcionalidades como dar de
alta/modificar/eliminar estaciones o añadir/dar de baja bicicletas sobre estaciones. El servicio que está integrado con los demás en la aplicación final es el que se encuentra en el directorio estaciones-2, ya que el otro contiene funcionalidad que no está en uso.
* Alquileres: Gestiona las reservas y alquileres que los usuarios pueden hacer sobre las
bicicletas de las estaciones.
* MongoDB (2): Base de datos que gestiona las entidades Usuario, Estación, y
Bicicleta.
* Mysql: Base de datos que gestiona las entidades Alquiler y Reserva.
* RabbitMQ: Gestiona la comunicación entre los servicios Alquileres y Estaciones,
intercambiando eventos mediante colas. Estos eventos avisan al otro servicio de
cuando una bicicleta ha sido alquilada, estacionada o desactivada, para que realice las
operaciones necesarias.
* Pasarela: Recibe todas las peticiones y gestiona a qué servicio corresponde la
funcionalidad.


# Funcionalidad implementada

**Gestor**

* Dar de alta y modificar estaciones con los datos: Nombre, Dirección, Código Postal y
Número de puestos.
* Borrar estaciones, comprobando que no tengan bicicletas estacionadas.
* Añadir bicicletas o darlas de baja sobre una estación, indicando el modelo de la
bicicleta. Para dar de baja una bicicleta se pide confirmación y se debe indicar el
motivo de baja de la bicicleta.
* Obtener un listado de estaciones existentes.
* Obtener un listado de las bicicletas estacionadas en una estación concreta.

**Usuario**

* Consultar las estaciones existentes.
2
* Obtener la información de una estación, mostrando los campos mencionados
anteriormente.
* Listar las bicicletas disponibles para reserva/alquiler de una estación.
* Reservar una bicicleta de una estación (solo se puede tener una reserva activa).
* Cancelar la reserva activa.
* Confirmar la reserva para realizar un alquiler sobre dicha bicicleta.
* Alquilar (sin reservar previamente) una bicicleta de una estación.
* Finalizar el alquiler activo, estacionando la bicicleta en una estación. Hay que indicar
el id de la estación de una lista en la que se muestran las estaciones disponibles. La
operación comprueba que la Estación seleccionada tenga huecos libres para la
bicicleta.
* Listar el historial de alquileres previos realizados por el usuario.
* Visualizar el alquiler/reserva activo en caso de que tenga alguno.
* Filtrar las estaciones mostradas por número mínimo de puestos disponibles, por
código postal y por nombre parcial. Estos filtros se pueden combinar y se muestra un
listado de las estaciones que cumplan los filtros que no estén vacíos.
