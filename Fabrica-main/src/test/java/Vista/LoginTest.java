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

import org.junit.jupiter.api.Test;

import Modelo.SQL;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 * Ccontrolador encargado de manejar las operaciones de inicio de sesion
 * 
 * Ruta base: /login
 */
@Controller
@RequestMapping("/login")
public class LoginTest {
	
	SQL sql = new SQL();
	ArrayList<SQL> User = new ArrayList<>();
	
    /**
     * metodo que responde a solicitudes get en la ruta "/login"
     * 
     * @return una instancia de ModelAndView que apunta a la vista "login"
     */
	@GetMapping
	@Test
	private ModelAndView Principal() {
		return new ModelAndView("login");
	}
	
    /**
     * metodo utilitario para abrir una url en el navegador predeterminado del sistema
     *
     * @param url la url que se desea abrir
     */
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
	
    /**
	 * metodo que recibe los datos del login
	 * verifica si el usuario y la contrasenia son correctos
	 * y guarda los datos en la sesion si todo esta bien
     *
     * @param Usuario nombre de usuario ingresado
     * @param Contrasenia contrasenia ingresada
     * @param session objeto httpsession para almacenar los atributos del usuario autenticado
     * @return un responseentity con un mensaje de exito o error
     */
    @PostMapping
    @ResponseBody
    @Test
    public ResponseEntity<Map<String, String>> iniciarSesion(
            @RequestParam String Usuario,
            @RequestParam String Contrasenia,
            HttpSession session) {

        Map<String, String> response = new HashMap<>();

        try {
        	Connection conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/fabrica", "root", "");
            String sql = "SELECT cod, usuario, contrasenia, correo, admin FROM usuarios WHERE usuario = ? AND contrasenia = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, Usuario);
                pstmt.setString(2, Contrasenia);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        // usuario valido: guardar en sesion
                        session.setAttribute("usuario", rs.getString("usuario"));
                        session.setAttribute("correo", rs.getString("correo"));
                        session.setAttribute("admin", rs.getInt("admin"));
                        session.setAttribute("cod", rs.getInt("cod"));

                        response.put("mensaje", "Inicio de sesión exitoso");
                        return ResponseEntity.ok(response);
                    } else {
                        response.put("error", "Usuario o contraseña incorrectos. Intenta de nuevo.");
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Error al conectar con la base de datos");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
