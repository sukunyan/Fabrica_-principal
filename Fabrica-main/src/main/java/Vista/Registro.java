package Vista;

import Modelo.SQL;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * controlador encargado de manejar el registro de nuevos usuarios
 * 
 * ruta base: /registrar
 */
@Controller
@RequestMapping("/registrar")
public class Registro {

    SQL user = new SQL();
    ArrayList<SQL> User = new ArrayList<>();

    /**
     * metodo que muestra el formulario para registrarse
     * 
     * @return vista con el formulario de registro
     */
    @GetMapping
    public ModelAndView mostrarFormularioRegistro() {
        return new ModelAndView("registrar");
    }

    /**
     * metodo que maneja el registro de un nuevo usuario
     * verifica si el correo o nombre de usuario ya están en uso
     * y si no es así, los guarda en la base de datos
     *
     * @param Usuario nombre de usuario ingresado
     * @param Correo correo electrónico ingresado
     * @param Contrasenia contrasenia ingresada
     * @return un responseentity con el mensaje de exito o error
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<Object> registrarse(@RequestParam String Usuario, @RequestParam String Correo, @RequestParam String Contrasenia) {
        int cod = 0;
        String usuario = "";
        String contrasenia = "";
        String correo = "";
        int administrador = 0;

        
        Map<String, String> response = new HashMap<>();
        
        
        try {
			
			Connection conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/fabrica", "root", "");
			
			Statement consulta = conexion.createStatement();
			
					
			ResultSet revisar = consulta.executeQuery("select cod, usuario, contrasenia, correo, admin from usuarios;");
			
			
			while(revisar.next()){
				
				System.out.println("==========================================================");
				System.out.println("Cod: " + revisar.getString("cod"));
				System.out.println("Marca: " + revisar.getString("usuario"));
				System.out.println("Modelo: " + revisar.getString("contrasenia"));
				System.out.println("Anio: " + revisar.getString("correo"));
				System.out.println("Matricula: " + revisar.getString("admin"));
				System.out.println("==========================================================");
				
				cod = revisar.getInt("cod");
				usuario = revisar.getString("usuario");
				contrasenia = revisar.getString("contrasenia");
				correo = revisar.getString("correo");
				int admin = revisar.getInt("admin");
				
				user.setCod(cod);
				user.setUsuario(usuario);
				user.setContrasenia(contrasenia);
				user.setCorreo(correo);
				user.setAdmin(admin);
				
				User.add(user);
	
			}
					
			
			for(SQL user : User) {
				if(user.getCorreo().equals(Correo) || user.getUsuario().equals(Usuario)) {
                    // Si el correo ya está en uso, devuelve un error
                	response.put("error", "El correo de usuario o nombre ya está en uso, por favor use otro o inicie sesión.");
        	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                }

			}
			
			consulta.executeUpdate("insert into usuarios(usuario, contrasenia, correo, admin)" + " values('" + Usuario + "', '" + Contrasenia + "', '" + Correo + "'," + administrador + ");");
			
			System.out.println("insert into usuarios(usuario, contrasenia, correo, admin)" + " values('" + Usuario + "', '" + Contrasenia + "', '" + Correo + "'," + administrador + ");");
			
			
			conexion.close();

	 
        } catch (Exception e) {
            response.put("error", "Error al leer o escribir Usuarios");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        // si todo sale bien, responde con exito pero no esta en uso este mesaje por que el javascript se redirige a login directamente si fue exitoso
        response.put("success", "Registro exitoso, ya puede iniciar sesión.");
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}
