package Modelo;

/**
 * clase que representa un coche con sus atributos principales
 * como codigo, marca, modelo, fecha, matricula y numero de chasis
 */
public class Coches {
	
	private int cod;
	private String marca;
	private String modelo;
	private String fecha;
	private String matricula;
	private String numChasis;
	
	/**
	 * constructor por defecto que inicializa los atributos con valores vacios o cero
	 */
	public Coches(){
		this.cod = 0;
		this.marca = "";
		this.modelo = "";
		this.fecha = "";
		this.matricula = "";
		this.numChasis = "";
	}

	/**
	 * constructor que inicializa los atributos del coche con los valores dados
	 *
	 * @param cod codigo del coche
	 * @param marca marca del coche
	 * @param modelo modelo del coche
	 * @param fecha fecha del coche (anio de fabricacion)
	 * @param matricula matricula del coche
	 * @param numChasis numero de chasis del coche
	 */
	public Coches(int cod, String marca, String modelo, String fecha, String matricula, String numChasis) {
		this.cod = cod;
		this.marca = marca;
		this.modelo = modelo;
		this.fecha = fecha;
		this.matricula = matricula;
		this.numChasis = numChasis;
	}


	/**
	 * obtiene la marca del coche
	 * 
	 * @return marca del coche
	 */
	public String getMarca() {
		return marca;
	}

	/**
	 * establece la marca del coche
	 * 
	 * @param marca nueva marca del coche
	 */
	public void setMarca(String marca) {
		this.marca = marca;
	}

	/**
	 * obtiene el modelo del coche
	 * 
	 * @return modelo del coche
	 */
	public String getModelo() {
		return modelo;
	}

	/**
	 * establece el modelo del coche
	 * 
	 * @param modelo nuevo modelo del coche
	 */
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	/**
	 * obtiene la fecha (anio) del coche
	 * 
	 * @return fecha del coche
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * establece la fecha (anio) del coche
	 * 
	 * @param fecha nueva fecha del coche
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * obtiene la matricula del coche
	 * 
	 * @return matricula del coche
	 */
	public String getMatricula() {
		return matricula;
	}

	/**
	 * establece la matricula del coche
	 * 
	 * @param matricula nueva matricula del coche
	 */
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	/**
	 * obtiene el numero de chasis del coche
	 * 
	 * @return numero de chasis del coche
	 */
	public String getNumChasis() {
		return numChasis;
	}

	/**
	 * establece el numero de chasis del coche
	 * 
	 * @param numChasis nuevo numero de chasis del coche
	 */
	public void setNumChasis(String numChasis) {
		this.numChasis = numChasis;
	}

	/**
	 * obtiene el codigo del coche
	 * 
	 * @return codigo del coche
	 */
	public int getCod() {
		return cod;
	}

	/**
	 * establece el codigo del coche
	 * 
	 * @param cod nuevo codigo del coche
	 */
	public void setCod(int cod) {
		this.cod = cod;
	}
}
