using Usuarios.Modelo;
using Usuarios.Servicio;
using Microsoft.AspNetCore.Mvc;

namespace UsuariosAPI.Controllers
{
    [Route("api/usuarios")]
    [ApiController]
    public class UsuariosController : ControllerBase
    {
        private readonly IServicioUsuarios _servicio; //se pone _ a las variables privadas por convención
        public UsuariosController(IServicioUsuarios servicio)
        {
            this._servicio = servicio;
        }

        [HttpGet("{id}", Name = "GetUsuario")]
        public ActionResult<Usuario> Get(string id)
        {
            var entidad = _servicio.Get(id);
            if (entidad == null)
            {
                return NotFound();
            }
            return entidad;

        }

        // GESTOR
        //Solicitud código de activación
        [HttpGet("codigoactivacion")]
        public ActionResult<string> CodigoActivacion(string id)
        {
            var codigo = _servicio.CodigoActivacion(id);
            //coger la excepción...

            return codigo;
        }


        //Baja de un usuario
        [HttpDelete("{id}")]
        public IActionResult BajaUsuario(string id)
        {
            var usuario = _servicio.Get(id);
            if (usuario == null)
            {
                return NotFound();
            }
            _servicio.BajaUsuario(id);
            return Ok();
        }

        //Listado de todos los usuarios
        [HttpGet]
        public ActionResult<List<UsuarioResumen>> GetListado(){
            return _servicio.GetAll();
        }

        //USUARIO
        //Operación alta de un usuario
        [HttpPost("altausuario")]
        public ActionResult<Usuario> AltaUsuario(Usuario usuario, [FromQuery] string codigoActivacion)
        {
            _servicio.AltaUsuario(usuario, codigoActivacion);
            return CreatedAtRoute("GetUsuario", new { id = usuario.Id }, usuario);
        }

        //Verificar las credenciales de un usuario.
        [HttpGet("registrado")]
        public ActionResult<Dictionary<String, String>> VerificarCredenciales([FromQuery] string nombre, [FromQuery] string contrasena)
        {
            return _servicio.VerificarCredenciales(nombre, contrasena);
        }

        //Verificar usuario OAuth2
        [HttpGet("oauth")]
        public ActionResult<Dictionary<String, String>> VerificarOAuth([FromQuery] string token)
        {
            return _servicio.VerificarOAuth(token);
        }

    }



}