package Modelo;

import java.time.LocalDate;

public class Coches {
	
	private int cod;
	private String marca;
	private String modelo;
	private String fecha;
	private String matricula;
	private String numChasis;
	
	public Coches(){
		this.cod = 0;
		this.marca = "";
		this.modelo = "";
		this.fecha = "";
		this.matricula = "";
		this.numChasis = "";
	}


	public Coches(int cod, String marca, String modelo, String fecha, String matricula, String numChasis) {
		this.cod = cod;
		this.marca = marca;
		this.modelo = modelo;
		this.fecha = fecha;
		this.matricula = matricula;
		this.numChasis = numChasis;
	}


	/**
	 * @return the marca
	 */
	public String getMarca() {
		return marca;
	}


	/**
	 * @param marca the marca to set
	 */
	public void setMarca(String marca) {
		this.marca = marca;
	}


	/**
	 * @return the modelo
	 */
	public String getModelo() {
		return modelo;
	}


	/**
	 * @param modelo the modelo to set
	 */
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}


	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}


	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}


	/**
	 * @return the matricula
	 */
	public String getMatricula() {
		return matricula;
	}


	/**
	 * @param matricula the matricula to set
	 */
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}


	/**
	 * @return the numChasis
	 */
	public String getNumChasis() {
		return numChasis;
	}


	/**
	 * @param numChasis the numChasis to set
	 */
	public void setNumChasis(String numChasis) {
		this.numChasis = numChasis;
	}


	/**
	 * @return the cod
	 */
	public int getCod() {
		return cod;
	}


	/**
	 * @param cod the cod to set
	 */
	public void setCod(int cod) {
		this.cod = cod;
	}
	
	
	
}
