package Vista;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import Modelo.SQL;

@Controller
@RequestMapping("/usuario")
public class Usuario {
    
	@GetMapping
    public ModelAndView mostrarUsuario() {
        return new ModelAndView("usuario"); 
    }
	
	@PostMapping
	@ResponseBody
	private ResponseEntity<Map<String, String>> modificarUsuario(@RequestParam String User, @RequestParam String email, @RequestParam String password, @RequestParam String confirm_password){
		
		SQL sql = new SQL();
		ArrayList<SQL> Usuario = new ArrayList<>();
		Map<String, String> response = new HashMap<>();
		
		String lector = "usuarios/";
	    File carpeta = new File(lector);
	    File[] archivos = carpeta.listFiles();
	    int cod = 0;
	    String usuario = "", contrasenia = "", correo = "";
	    int admin = 0;
		
		try {
			
			Connection conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/fabrica", "root", "");
			
			Statement consulta = conexion.createStatement();
			
					
			ResultSet revisar = consulta.executeQuery("select cod, usuario, contrasenia, correo, admin from usuarios;");
			
			
			while(revisar.next()){
				
				System.out.println("==========================================================");
				System.out.println("Cod: " + revisar.getString("cod"));
				System.out.println("Usuario: " + revisar.getString("usuario"));
				System.out.println("Contraseña: " + revisar.getString("contrasenia"));
				System.out.println("Email: " + revisar.getString("correo"));
				System.out.println("Admin " + revisar.getString("admin"));
				System.out.println("==========================================================");
				
				cod = revisar.getInt("cod");
				usuario = revisar.getString("usuario");
				contrasenia = revisar.getString("contrasenia");
				correo = revisar.getString("correo");
				admin = revisar.getInt("admin");
				
				
				SQL user = new SQL(cod, usuario, contrasenia, correo, admin);
				
				Usuario.add(user);

				
			}
			
			if(User==null) {
				User = usuario;
				System.out.println(User);
			} else if(email==null) {
				email = correo;
				System.out.println(email);
			} else if(password==null || confirm_password==null) {
				password = contrasenia;
				confirm_password = contrasenia;
				System.out.println(password + confirm_password);
			}
			
			System.out.println(User+ " " + email + " " + password + " " + confirm_password);
			
			if(password.equals(confirm_password)) {
				
				
					/*
					System.out.println("update usuarios set ");
					
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
					*/
				
				
				conexion.close();
				
				System.out.println(User+ " " + email + " " + password + " " + confirm_password);
				
				response.put("info", "Cambios confirmados");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				
			} else {
            
				response.put("error", "Password diferente a la de confirmar");
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			
		} catch (Exception e) {
			
			System.out.println(e);
			
			response.put("error", "Error al aplicar los cambios");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			
		}
		
		
	}
	
}