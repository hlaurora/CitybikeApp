using MongoDB.Driver;
using Repositorio;
using Usuarios.Modelo;

namespace Usuarios.Repositorio{

    public class RepositorioUsuariosMongoDB : Repositorio<Usuario, string>
    {
        private readonly IMongoCollection<Usuario> usuarios;

        public RepositorioUsuariosMongoDB()
        {
            // var client = new MongoClient("mongodb+srv://4auroralo:atlasCluster0@citybike.vumktwx.mongodb.net/citybike?retryWrites=true&w=majority");   //ruta de MongoDB
            var client = new MongoClient("mongodb://root:example@mongo:27017/usuarios?authSource=admin");
            var database = client.GetDatabase("citybike"); //nombre de la bd
            usuarios = database.GetCollection<Usuario>("usuarios");
        }


        public string Add(Usuario entity)
        {
            usuarios.InsertOne(entity);
            return entity.Id;
        }

        public void Delete(Usuario entity)
        {
            usuarios.DeleteOne(usuario => usuario.Id == entity.Id);
        }

        public List<Usuario> GetAll()
        {
            return usuarios.Find(_ => true).ToList(); //Es como no poner condición (1=1)
        }

        public Usuario GetById(string id)
        {
            return usuarios.Find(usuario => usuario.Id == id).FirstOrDefault(); //el primero que la cumple
        }

        /*public Usuario GetByNombreUsuario(string nombreUsuario)
        {
            return usuarios.Find(usuario => usuario.NombreUsuario == nombreUsuario).FirstOrDefault(); 
        }*/

        public List<string> GetIds()
        {
            var todas = usuarios.Find(_ => true).ToList();

            return todas.Select(p => p.Id).ToList();
        }

        public void Update(Usuario entity)
        {
            usuarios.ReplaceOne(usuario => usuario.Id == entity.Id, entity);
        }
    }





}