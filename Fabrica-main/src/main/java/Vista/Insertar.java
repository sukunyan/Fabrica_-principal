package Vista;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;
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

import Modelo.Coches;
import jakarta.servlet.http.HttpSession;

/**
 * clase controlador que gestiona la insercion de nuevos coches en la base de datos.
 * 
 * Ruta base: /insertar
 */
@Controller
@RequestMapping("/insertar")
public class Insertar {
    /**
     * objeto coche utilizado para almacenar temporalmente los datos del coche a insertar.
     */
    Coches coches = new Coches();

    /**
     * metodo que abre la vista del formulario de insertar si tiene el session inicializada
     *
     * @return una instancia de ModelAndView que apunta a la vista "insertar".
     */
    @GetMapping
    public ModelAndView AbrirInsertar(HttpSession session) {
		if (session.getAttribute("cod") == null ) {
			return new ModelAndView("/login");
		}else {
			return new ModelAndView("/insertar");
		}
        
    }

    /**
     * metodo que recibe los datos de un nuevo coche desde un formulario web y lo guarda en la base de datos.
     * valida si el usuario esta autenticado antes de realizar la insercion.
     *
     * @param marca marca del coche
     * @param modelo modelo del coche
     * @param anio anio del coche como localdate
     * @param matricula matricula del coche
     * @param numChasis numero de chasis del coche
     * @param session sesion actual del usuario
     * @return responseentity con mensaje de exito o error
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<Map<String, String>> InsertarCoche(
            @RequestParam String marca,
            @RequestParam String modelo,
            @RequestParam LocalDate anio,
            @RequestParam String matricula,
            @RequestParam String numChasis,
            HttpSession session) {
    	
    	String usuarioActual = (String) session.getAttribute("usuario");
    	ImagenUsuario.obtenerImagenUsuario(usuarioActual);
    	
        Map<String, String> response = new HashMap<>();
    	
        if (usuarioActual == null) {
            response.put("error", "Usuario no autenticado. Por favor, inicie sesión.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
    
        coches.setMarca(marca);
        coches.setModelo(modelo);
        coches.setFecha(anio.toString());
        coches.setMatricula(matricula);
        coches.setNumChasis(numChasis);

        try {
        	Connection conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/fabrica", "root", "");
            Statement consulta = conexion.createStatement();
            String sql = "insert into coches(marca, modelo, anio, matricula, numChasis) values ('" +
                    coches.getMarca() + "', '" +
                    coches.getModelo() + "', '" +
                    coches.getFecha() + "', '" +
                    coches.getMatricula() + "', '" +
                    coches.getNumChasis() + "')";

            System.out.println("insert into coches(marca, modelo, anio, matricula, numChasis)" + 
            " values('" + coches.getMarca() + "', '" + 
            coches.getModelo() + "', '" + 
            coches.getFecha() + "', " + "'" + 
            coches.getMatricula() + "', '" + coches.getNumChasis() + "');");
            consulta.executeUpdate(sql);

            response.put("mensaje", "Coche guardado correctamente");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Error al guardar el coche: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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
