package Vista;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class Usuario {
	
	@GetMapping("/datos")
	private Map<String, String> obtenerDatosUsuario() {
	    Map<String, String> datos = new HashMap<>();
	    
	    try {
	        Connection conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/fabrica", "root", "");
	        
	        Statement consulta = conexion.createStatement();
	       
	        ResultSet revisar = consulta.executeQuery("select cod, usuario, contrasenia, correo, admin from usuarios;"); 

	        if (revisar.next()) {
	            String usuario = revisar.getString("usuario");
	            datos.put("usuario", usuario);
	        }

	        conexion.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	        datos.put("error", "Error al obtener los datos");
	    }

	    return datos;
	}
	
	@GetMapping
	public ModelAndView mostrarUsuario(HttpSession session) {
	    ModelAndView mav = new ModelAndView("usuario");
	    
	    if (session.getAttribute("Cod") == null) {
	        return new ModelAndView("redirect:/login");
	    } 

	    try {
	        int cod = (int) session.getAttribute("Cod");

	        Connection conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/fabrica", "root", "");
	        Statement consulta = conexion.createStatement();

	        ResultSet revisar = consulta.executeQuery("select usuario from usuarios where cod = " + cod);

	        if (revisar.next()) {
	            String usuario = revisar.getString("usuario");
	            mav.addObject("nombreUsuario", usuario);
	        }

	        conexion.close();
	    } catch (Exception e) {
	        mav.addObject("nombreUsuario", "Usuario");
	    }

	    return mav;
	}
	
	@PostMapping
	@ResponseBody
	private ModelAndView modificarUsuario(@RequestParam(required=false) String User, @RequestParam(required=false) String email,
															@RequestParam(required=false) String password, @RequestParam(required=false) String confirm_password,
															@RequestParam String password_obl, HttpSession session){
		
		ModelAndView men = new ModelAndView();
		
		int cod = (int) session.getAttribute("Cod");
		
		SQL sql = new SQL();
		ArrayList<SQL> Usuario = new ArrayList<>();
		
		String lector = "usuarios/";
	    File carpeta = new File(lector);
	    File[] archivos = carpeta.listFiles();
	    String usuario = "", contrasenia = "", correo = "";
	    int admin = 0;
		
		try {
			
			Connection conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/fabrica", "root", "");
			
			Statement consulta = conexion.createStatement();
			
			ResultSet confirmar = consulta.executeQuery("select cod, usuario, contrasenia, correo, admin from usuarios");
			
			while(confirmar.next()){
					
				System.out.println("==========================================================");
				System.out.println("Usuario: " + confirmar.getString("usuario"));
				System.out.println("Contraseña: " + confirmar.getString("contrasenia"));
				System.out.println("Email: " + confirmar.getString("correo"));
				System.out.println("Admin " + confirmar.getString("admin"));
				System.out.println("==========================================================");
				
				usuario = confirmar.getString("usuario");
				contrasenia = confirmar.getString("contrasenia");
				correo = confirmar.getString("correo");
				admin = confirmar.getInt("admin");
				
				if(User.equals(usuario)){
					men.addObject("mensaje", "Nombre en uso");
					return men;
				}
				if(email.equals(correo)) {
					men.addObject("mensaje", "Correo en uso");
					return men;
				}
				
			}
					
			ResultSet revisar = consulta.executeQuery("select cod, usuario, contrasenia, correo, admin from usuarios where cod= " + cod);
			
			
			while(revisar.next()){
				
				System.out.println("==========================================================");
				System.out.println("Usuario: " + revisar.getString("usuario"));
				System.out.println("Contraseña: " + revisar.getString("contrasenia"));
				System.out.println("Email: " + revisar.getString("correo"));
				System.out.println("Admin " + revisar.getString("admin"));
				System.out.println("==========================================================");
				
				usuario = revisar.getString("usuario");
				contrasenia = revisar.getString("contrasenia");
				correo = revisar.getString("correo");
				admin = revisar.getInt("admin");
				
				
				SQL user = new SQL(cod, usuario, contrasenia, correo, admin);
				
				Usuario.add(user);

				
			}
			
			if(User.equals(usuario)) {
				men.addObject("mensaje", "Nombre de usuario igual al anterior");
				return men;
			}
			if(email.equals(correo)) {
				men.addObject("mensaje", "Correo igual al anterior");
				return men;
			}
			if(password.equals(contrasenia) && confirm_password.equals(contrasenia)) {
				men.addObject("mensaje", "Contraseña usada anteriormente");
				return men;
			}
			
			if(password_obl.equals(contrasenia)) {
			
				if(User.isEmpty()) {
					User = usuario;
					System.out.println("dato del usuario modificado al original: " + User); 
				}
				if(email.isEmpty()) {
					email = correo;
					System.out.println("dato del usuario modificado al original: " + email);
				}
				if(password.isEmpty()) {
					password=contrasenia;
					System.out.println("dato del usuario modificado al original: " + password);
				}
				if(confirm_password.isEmpty()) {
					confirm_password=contrasenia;
					System.out.println("dato del usuario modificado al original: " + confirm_password);
				}
				
				if(password.equals(confirm_password)) {
					System.out.println("update usuarios set usuario='" + User + "', contrasenia='" + confirm_password + "', correo='" + email + "' where cod='" + cod + "'");
						
						// En valor guardamos el resultado del update (1 si todo ha ido bien)
						
						int valor = consulta.executeUpdate("update usuarios set usuario='" + User + "', contrasenia='" + confirm_password + "', correo='" + email + "' where cod=" + cod );
			
						
						if(valor==1) {
							
							System.out.println("Cambios cambiados correctamente");
							
						}else {
							
							System.out.println("Error al modificar usuario datos no coherentes o no posibles de introducir");
							men.addObject("mensaje", "Error al modificar usuario datos no coherentes o no posibles de introducir");
							return men;
						}
				} else {
		            men.addObject("mensaje", "password diferente a la password Confirm");
		            return men;
				}
			
			} else {
	            men.addObject("mensaje", "password equivocada");
	            return men;
			}
				
			conexion.close();
			
            men.addObject("mensaje", "Cambios guardados con exito");
            return men;
			
		} catch (Exception e) {
			
			System.out.println(e);
			men.addObject("mensaje", "Error al aplicar los cambios");
			return men;
		}
		
	}
	
}