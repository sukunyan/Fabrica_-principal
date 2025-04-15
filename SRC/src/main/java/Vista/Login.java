package Vista;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Modelo.SQL;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


@Controller
@RequestMapping("/login")
public class Login {
	
	SQL sql = new SQL();
	ArrayList<SQL> User = new ArrayList<>();
	
	@GetMapping
	private ModelAndView Principal() {
		return new ModelAndView("login");
	}
	
	public void abrirNavegador(String url) {
		
		 try {
		        String os = System.getProperty("os.name").toLowerCase();
		        ProcessBuilder processBuilder;

		        if (os.contains("win")) {
		            // En Windows
		            processBuilder = new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", url);
		        } else if (os.contains("mac")) {
		            // En MacOS
		            processBuilder = new ProcessBuilder("open", url);
		        } else if (os.contains("nix") || os.contains("nux")) {
		            // En Linux
		            processBuilder = new ProcessBuilder("xdg-open", url);
		        } else {
		            throw new UnsupportedOperationException("Unsupported OS");
		        }

		        processBuilder.start();  // Inicia el proceso para abrir el navegador
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		 
    }
	
	@PostMapping
	@ResponseBody
	public ResponseEntity<Map<String, String>> iniciarSesion(@RequestParam String Usuario, @RequestParam String Contrasenia, HttpSession session) {
	    String lector = "usuarios/";
	    File carpeta = new File(lector);
	    File[] archivos = carpeta.listFiles();
	    int cod = 0;
	    String usuario = "", contrasenia = "", correo = "";
	    int admin = 0;

	    Map<String, String> response = new HashMap<>();

	    if (archivos == null) {
	        response.put("error", "No se encontraron archivos de usuarios");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	    
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
				
				User.add(user);
				
				if(Usuario.equals(usuario) && Contrasenia.equals(contrasenia)) {
					
					session.setAttribute("Cod", cod);
					session.setAttribute("Usuario", usuario);
					
					response.put("mensaje", "Inicio de sesion exitoso");
					
					return ResponseEntity.ok(response);
				}
				
			}
					
			
			
				
			conexion.close();

	 
        } catch (Exception e) {
            response.put("error", "Error al leer el archivo");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
	        response.put("error", "Usuario o contraseña incorrectos. Intenta de nuevo.");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	
}
