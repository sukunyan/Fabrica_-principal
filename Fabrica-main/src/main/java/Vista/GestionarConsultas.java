package Vista;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import Modelo.Coches;
import jakarta.servlet.http.HttpSession;

/**
 * clase controlador que gestiona las operaciones sobre los coches desde la vista /gestionar.
 * incluye funcionalidades para consultar, modificar y eliminar coches,
 * asi como cerrar la sesion del usuario.
 * 
 * Ruta base: /gestionar
 */
@Controller
@RequestMapping("/gestionar")
public class GestionarConsultas {

    /**
     * lista de coches que se utiliza para almacenar los datos obtenidos desde la base de datos.
     */
	ArrayList<Coches> coches = new ArrayList<>();
	
    /**
     * metodo que devuelve la vista principal de gestionar si el session esta inicializado.
     *
     * @return una instancia de ModelAndView que apunta a la vista "gestionar".
     */
	@GetMapping
	public ModelAndView Gestionador(HttpSession session) {
		if (session.getAttribute("cod") == null ) {
			return new ModelAndView("/login");
		}else {
			return new ModelAndView("/gestionar");
		}
		
	}
	
    /**
     * metodo que obtiene los datos de todos los coches desde la base de datos y los devuelve en formato json.
     * tambien imprime en consola la informacion de cada coche.
     *
     * @param session sesion http actual del usuario
     * @return json con la lista de coches o mensaje de error
     */
	@GetMapping("/datos")
	@ResponseBody 
	public String Consultas(HttpSession session) {
		
		String usuarioActual = (String) session.getAttribute("usuario");
    	ImagenUsuario.obtenerImagenUsuario(usuarioActual);

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
	
    /**
     * metodo que modifica los datos de un coche en la base de datos.
     *
     * @param coche objeto coche recibido desde el cliente
     * @return json indicando si la modificacion fue exitosa o no
     */
	@PostMapping("/actualizar")
    @ResponseBody
    private String modificar(@RequestBody Coches coche, HttpSession session) {
		
		if(session.getAttribute("cod") != null) {	
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
		} else {
			return "{\"fail\":\"false\"}";
		}
		
	}
	
    /**
     * metodo que elimina un coche de la base de datos y lo guarda en la tabla de eliminados.
     *
     * @param coche objeto coche recibido desde el cliente
     * @return json indicando si la eliminacion fue exitosa o no
     */
	@PostMapping("/eliminar")
	@ResponseBody
	private String eliminar(@RequestBody Coches coche, HttpSession session) {
		
		 Coches Coche = coche;
		
		 if(session.getAttribute("cod") != null) {
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
		} else {
			return "{\"fail\":\"false\"}";
		}
		
	}
	
    /**
     * metodo que cierra la sesion del usuario y redirige a la pagina de login
     * 
     * @param session sesion actual del usuario
     * @return redireccion a la pagina de login
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
	
}
