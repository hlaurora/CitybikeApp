package pasarela.dto;

public class ResponseAuthDTO {

	private String token;
	private String id;
	private String usuario;
	private String rol;
	public ResponseAuthDTO(String token, String id, String usuario, String rol) {
		super();
		this.token = token;
		this.id = id;
		this.usuario = usuario;
		this.rol = rol;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	
	
	
}
