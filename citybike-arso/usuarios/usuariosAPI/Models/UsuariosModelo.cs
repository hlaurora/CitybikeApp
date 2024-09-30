using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
namespace Usuarios.Modelo {

    public class Usuario{

        //El resto de información proporcionada en el proceso de alta se deja a criterio de cada 
        //grupo (nombre completo, correo electrónico, teléfono, dirección postal, etc.).
        [BsonId]
        [BsonRepresentation(BsonType.String)]
        public string Id{get;set;}
        public string NombreUsuario{get;set;}
        public string Contrasena{get;set;}
        public string Rol{get;set;}
        public string IdOAuth{get;set;}

        // Nombre Apellido1 Apellido2
        public string NombreCompleto{get;set;}
        public string Correo{get;set;}
        public string Direccion{get;set;}

        

    }



}