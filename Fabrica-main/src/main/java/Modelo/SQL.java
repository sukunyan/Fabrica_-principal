package Modelo;


/**
 * clase que representa un usuario del sistema
 */
public class SQL {
	
	private int Cod;
	private String Usuario;
	private String Correo;
	private String Contrasenia;
	private int Admin;
	
	/**
	 * constructor que inicializa un usuario con todos sus campos
	 * 
	 * @param cod codigo del usuario
	 * @param usuario nombre de usuario
	 * @param correo correo electronico del usuario
	 * @param contrasenia contrasenia del usuario
	 * @param admin indica si el usuario es administrador (1) o no (0)
	 */
	public SQL(int cod, String usuario, String correo, String contrasenia, int admin) {
		this.Cod = cod;
		this.Usuario = usuario;
		this.Correo = correo;
		this.Contrasenia = contrasenia;
		this.Admin = admin;
	}
	
	/**
	 * constructor por defecto que inicializa todos los campos vacios o en 0
	 */
	public SQL() {
		this.Cod = 0;
		this.Usuario = "";
		this.Correo = "";
		this.Contrasenia = "";
		this.Admin = 0;
	}

	/**
	 * @return nombre de usuario
	 */
	public String getUsuario() {
		return Usuario;
	}

	/**
	 * @param usuario nombre de usuario a establecer
	 */
	public void setUsuario(String usuario) {
		Usuario = usuario;
	}

	/**
	 * @return correo electronico del usuario
	 */
	public String getCorreo() {
		return Correo;
	}

	/**
	 * @param correo correo electronico a establecer
	 */
	public void setCorreo(String correo) {
		Correo = correo;
	}

	/**
	 * @return contrasenia del usuario
	 */
	public String getContrasenia() {
		return Contrasenia;
	}

	/**
	 * @param contrasenia contrasenia a establecer
	 */
	public void setContrasenia(String contrasenia) {
		Contrasenia = contrasenia;
	}

	/**
	 * @return 1 si es administrador, 0 si no lo es
	 */
	public int getAdmin() {
		return Admin;
	}

	/**
	 * @param admin valor que indica si el usuario es administrador
	 */
	public void setAdmin(int admin) {
		Admin = admin;
	}

	/**
	 * @return codigo del usuario
	 */
	public int getCod() {
		return Cod;
	}

	/**
	 * @param cod codigo del usuario a establecer
	 */
	public void setCod(int cod) {
		Cod = cod;
	}
	
}
