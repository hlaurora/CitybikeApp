using Usuarios.Modelo;
using Repositorio;
using System;
using Microsoft.AspNetCore.Authorization.Infrastructure;

namespace Usuarios.Servicio
{

    public class UsuarioResumen
    {
        public string Id { get; set; }
        public string Nombre { get; set; }
        public string rol { get; set; }
    }

    public class CodigoActivacion
    {
        public string IdUsuario { get; set; }
        public DateTime Validez { get; set; }
        public string Codigo { get; set; }
    }

    public interface IServicioUsuarios
    {
        string Create(Usuario usuario);

        Usuario Get(String id);

        void Update(Usuario usuario);
        void Remove(string id);

        //Añadir funciones del servicio

        //Gestor
        string CodigoActivacion(String id); //Genera un código de activación para un usuario
        void BajaUsuario(String id); //Baja de un usuario

        List<UsuarioResumen> GetAll();  //Listado de todos los usuarios

        //Usuario
        void AltaUsuario(Usuario usuario, string codigoActivacion); //Alta de un usuario (arreglar)

        //En estas dos operaciones se retorna un mapa con los claims
        Dictionary<String,String> VerificarCredenciales(String nombre, String contrasena); //Verifica las credenciales de un usuario
        Dictionary<String,String> VerificarOAuth(String token);
        

    }


    public class ServicioUsuarios : IServicioUsuarios
    {

        private Repositorio<Usuario, string> repositorio;

        public ServicioUsuarios(Repositorio<Usuario, string> repositorio)
        {
            this.repositorio = repositorio;
        }

        //Operación alta de un usuario. En la información del alta se proporciona un código de activación 
        //que se utiliza para comprobar que el usuario está autorizado a realizar el alta. 
        //Esta operación solo da de alta usuarios de la aplicación, ya que los gestores 
        //se dan de alta directamente en la base de datos. En relación a la información 
        //relativa al proceso de autenticación, se ofrecen dos alternativas: con usuario/contraseña y mediante OAuth2. 
        //Si se establece una constraseña, se opta por la primera opción. En cambio, si se elige OAuth2, se añadirá a 
        //la petición un campo con el identificador de usuario OAuth2 de GitHub. Los gestores tendrán autenticación 
        //con usuario/contraseña. 

        public void AltaUsuario(Usuario usuario, string codigoActivacion)
        {
            //throw new NotImplementedException();
            // Gestión codigo autorizacion
            if (VerificarCodigo(codigoActivacion, usuario.Id))
            {
                this.Create(usuario);
            }
            else
            {
                throw new ArgumentException();
            }

        }

        public void BajaUsuario(string id)
        {
            //throw new NotImplementedException();
            this.Remove(id);
        }

        //Solicitud código de activación (para el gestor): tiene como parámetro el identificador de un usuario. 
        //La operación genera un código asociado al usuario con un plazo de validez. 
        //Este código será requerido en el proceso de alta. Nota: existirá un proceso administrativo, ajeno a la aplicación, que permite al usuario solicitar el alta en la aplicación. 
        //Si cumple los requisitos, el gestor activa un código para que se proceda con el alta.
        public string CodigoActivacion(string id)
        {
            string codigo= "";
            // Generar 5 números aleatorios
            for (int i = 0; i < 5; i++)
            {
                int digito = new Random().Next(0, 10);
                codigo += digito.ToString();
            }
            codigo += id;
            codigo += DateTime.Now.AddDays(7).ToString();
            return codigo;
        }

        private Boolean VerificarCodigo(string codigo, string id) {
            return id == codigo.Substring(5, id.Length) && DateTime.Now < DateTime.Parse(codigo.Substring(5+id.Length));
        }

        public string Create(Usuario usuario)
        {
            return repositorio.Add(usuario);
        }

        public Usuario Get(string id)
        {
            return repositorio.GetById(id);
        }

        public List<UsuarioResumen> GetAll()
        {
            return repositorio.GetAll().Select(u => new UsuarioResumen
            {
                Id = u.Id,
                Nombre = u.NombreUsuario,
                rol = u.Rol
            }).ToList();
        }

        public void Remove(string id)
        {
            Usuario usuario = repositorio.GetById(id);
            repositorio.Delete(usuario);
        }

        public void Update(Usuario usuario)
        {
            throw new NotImplementedException();
        }

        /*
        Verificar las credenciales de un usuario. En el proceso de login con usuario/contraseña este operación 
        comprueba que exista el usuario y que coincida la contraseña.
        Retornar un mapa con los claims (identificador de usuario, nombre completo, rol) 
        que se utilizarán para emitir un token JWT.
        */

        //retorna un mapa con los claims (id de usuario, nombre de usuario, rol)
        public Dictionary<String,String> VerificarCredenciales(string nombreUsuario, string contrasena)
        {
            //throw new NotImplementedException();
            Usuario usuario = repositorio.GetAll().Where(u => u.NombreUsuario == nombreUsuario).FirstOrDefault();
            if(usuario.Id != null && usuario.Contrasena == contrasena){
            Dictionary<String,String> claims = new Dictionary<String, String>
            {
                { "Id", usuario.Id },
                { "Nombre", usuario.NombreUsuario },
                { "Rol", usuario.Rol },
                { "sub", usuario.Id}
            };
            return claims;}
            throw new ArgumentException();
        }

        /*
        Verificar usuario OAuth2. En el proceso de autenticación OAuth2 esta operación comprueba 
        si el identificador OAuth2 de GitHub corresponde con alguno de los usuarios registrados 
        en la aplicación, es decir, de un usuario registrado con autenticación OAuth2.
        Retornar un mapa con los claims (identificador de usuario, nombre completo, rol) 
        que se utilizarán para emitir un token JWT.
        */
        public Dictionary<String,String> VerificarOAuth(string token)
        {
            var usuario = repositorio.GetAll().Where(u => u.IdOAuth != null && u.IdOAuth == token).FirstOrDefault(); 
            if(usuario != null){
                return new Dictionary<string, string>
                {
                    { "Id", usuario.Id },
                    { "Nombre", usuario.NombreUsuario },
                    { "Rol", usuario.Rol },
                    { "sub", usuario.Id}
                };
            }
            throw new KeyNotFoundException();
            
        }
    }




}