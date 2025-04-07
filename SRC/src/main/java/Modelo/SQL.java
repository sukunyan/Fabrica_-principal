package Modelo;

public class SQL {
	
	private int Cod;
	private String Usuario;
	private String Correo;
	private String Contrasenia;
	private int Admin;
	
	/**
	 * @param usuario
	 * @param correo
	 * @param contraseña
	 */
	public SQL(int cod, String usuario, String correo, String contrasenia, int admin) {
		this.Cod = cod;
		this.Usuario = usuario;
		this.Correo = correo;
		this.Contrasenia = contrasenia;
		this.Admin = admin;
	}
	
	public SQL() {
		this.Cod = 0;
		this.Usuario = "";
		this.Correo = "";
		this.Contrasenia = "";
		this.Admin = 0;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return Usuario;
	}
	
	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		Usuario = usuario;
	}
	
	/**
	 * @return the correo
	 */
	public String getCorreo() {
		return Correo;
	}
	
	/**
	 * @param correo the correo to set
	 */
	public void setCorreo(String correo) {
		Correo = correo;
	}
	
	/**
	 * @return the contraseña
	 */
	public String getContrasenia() {
		return Contrasenia;
	}
	
	/**
	 * @param contraseña the contraseña to set
	 */
	public void setContrasenia(String contrasenia) {
		Contrasenia = contrasenia;
	}

	/**
	 * @return the admin
	 */
	public int getAdmin() {
		return Admin;
	}

	/**
	 * @param admin the admin to set
	 */
	public void setAdmin(int admin) {
		Admin = admin;
	}

	/**
	 * @return the cod
	 */
	public int getCod() {
		return Cod;
	}

	/**
	 * @param cod the cod to set
	 */
	public void setCod(int cod) {
		Cod = cod;
	}

	
	
}
