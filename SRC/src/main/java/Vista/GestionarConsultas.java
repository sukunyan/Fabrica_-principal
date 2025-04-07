package Vista;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import Modelo.Coches;

@Controller
@RequestMapping("/gestionar")
public class GestionarConsultas {

	ArrayList<Coches> coches = new ArrayList<>();
	@GetMapping
	public ModelAndView Gestionador() {
		return new ModelAndView("/gestionar");
	}
	
	@GetMapping("/datos")
	@ResponseBody 
	public String Consultas() {
		
		System.out.println();
		
		coches.clear();  
		
		try {
			Connection conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/fabrica", "root", "");
			
			Statement consulta = conexion.createStatement();
			
			ResultSet revisar = consulta.executeQuery("select cod, marca, modelo, anio, matricula, numChasis from coches;");
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			
			while(revisar.next()){
				
				System.out.println("==========================================================");
				System.out.println("Cod: " + revisar.getString("cod"));
				System.out.println("Marca: " + revisar.getString("marca"));
				System.out.println("Modelo: " + revisar.getString("modelo"));
				System.out.println("Anio: " + revisar.getString("anio"));
				System.out.println("Matricula: " + revisar.getString("matricula"));
				System.out.println("NumChasis: " + revisar.getString("numChasis"));
				System.out.println("==========================================================");
				
				int cod = revisar.getInt("cod");
				String marca = revisar.getString("marca");
				String modelo = revisar.getString("modelo");
				String anio = revisar.getString("anio");
				String matricula = revisar.getString("matricula");
				String numChasis = revisar.getString("numChasis");
				
				
				Coches c = new Coches(cod, marca, modelo, anio, matricula, numChasis);
				
				coches.add(c);
				
				
			}

			
			String json = gson.toJson(coches);
			System.out.println(json);
			return json;
			
			
		}catch (Exception e) {
				
			System.out.println(e);
			return "{\"error\":\"" + e.getMessage() + "\"}";
				
		}
		
		
	}
	
	
	
	@PostMapping("/actualizar")
    @ResponseBody
    private String modificar(@RequestBody Coches coche) {
		
		try {
			
            Coches cocheModificado = coche;
            
			Connection conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/fabrica", "root", "");
			
			Statement consulta = conexion.createStatement();
			
			System.out.println("update coches set marca="
					+ "'" + cocheModificado.getMarca() + "', modelo='" + cocheModificado.getModelo() + 
					"', anio='" + cocheModificado.getFecha() + "', matricula="
							+ "'" + cocheModificado.getMatricula() + "', numChasis=" + "'" + cocheModificado.getNumChasis() + "'" +
					" where cod= '" + cocheModificado.getCod() + "');");
			
			// En valor guardamos el resultado del update (1 si todo ha ido bien)
			
			int valor = consulta.executeUpdate("update coches set marca='" 
				    + cocheModificado.getMarca() + "', modelo='" + cocheModificado.getModelo() + 
				    "', anio='" + cocheModificado.getFecha() + "', matricula='" 
				    + cocheModificado.getMatricula() + "', numChasis='" + cocheModificado.getNumChasis() + "'" + 
				    " where cod='" + cocheModificado.getCod() + "'");

			
			if(valor==1) {
				
				System.out.println("Coche modificado correctamente");
				
			}else {
				
				System.out.println("No existe un coche con ese id");
				
			}
			
			conexion.close();
			
			return "{\"success\":\"true\"}";
			
		} catch (Exception e) {
			
			System.out.println(e);
			return "{\"fail\":\"false\"}";
			
		}	
		
	}
	
	@PostMapping("/eliminar")
	@ResponseBody
	private String eliminar(@RequestBody Coches coche) {
		
		 Coches Coche = coche;
		
		
		try {
			
			Connection conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/fabrica", "root", "");
			
			Statement consulta = conexion.createStatement();
			
			consulta.executeUpdate("insert into eliminados(marca, modelo, anio, matricula, numChasis)" + " values('" + Coche.getMarca() + "', '" + Coche.getModelo() + "', '" + Coche.getFecha() + "', " + "'" + Coche.getMatricula() + "', '" + Coche.getNumChasis() + "');");
			
			System.out.println("insert into eliminados(marca, modelo, anio, matricula, numChasis)" + " values('" + Coche.getMarca() + "', '" + Coche.getModelo() + "', '" + Coche.getFecha() + "', " + "'" + Coche.getMatricula() + "', '" + Coche.getNumChasis() + "');");
			
			int valor = consulta.executeUpdate("delete from coches where cod=" + Coche.getCod());
			
			if(valor==1) {
				System.out.println("Coche borrado correctamente");
				consulta.executeUpdate("ALTER TABLE coches AUTO_INCREMENT = 0");
			}else {
				System.out.println("Ha habido un problema en el borrado del Coche");
			}
			
			conexion.close();
			
			return "{\"success\":\"true\"}";
			
		} catch (Exception e) {
			
			System.out.println(e);
			return "{\"fail\":\"false\"}";
			
		}
	}
	
}
