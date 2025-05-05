package Vista;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;


/**
 * controlador Spring para la gestion de usuarios.
 * Permite visualizar la página del usuario, modificar datos personales,
 * obtener la imagen de perfil del usuario autenticado y cerrar sesión.
 * 
 * Ruta base: /usuario
 */
@Controller
@RequestMapping("/usuario")
public class Usuario {
    
    /**
     * muestra la vista del usuario si el usuario esta logueado entra a usuario.html y no esta va al login.
     *
     * @return una instancia de ModelAndView que apunta a la vista "usuario".
     */
	
	@GetMapping
    public ModelAndView mostrarUsuario(HttpSession session) {
		
		if (session.getAttribute("cod") == null ) {
			return new ModelAndView("/login");
		}else {
			return new ModelAndView("/usuario");
		}
    }
	
    /**
     * Procesa la modificacion de datos del usuario
     * 
     * Permite cambiar el nombre de usuario, correo electrónico, contraseña
     * y la imagen de perfil y tambien requiere validacion de la contraseña actual.
     *
     * @param User            		Nuevo nombre de usuario (opcional).
     * @param email          		Nuevo correo electrónico (opcional).
     * @param password      		Nueva contraseña (opcional).
     * @param confirm_password 		Confirmación de la nueva contraseña (opcional).
     * @param password_obl    		Contraseña actual obligatoria para validar cambios.
     * @param profilePic      		Nueva imagen de perfil (opcional).
     * @param session         		Objeto HttpSession para obtener el usuario autenticado.
     * 
     * @return ResponseEntity con el estado del proceso y mensajes de exito o error.
     */
	
	@PostMapping
	@ResponseBody
	private ResponseEntity<Map<String, String>> modificarUsuario(
	        @RequestParam(required = false) String User,
	        @RequestParam(required = false) String email,
	        @RequestParam(required = false) String password,
	        @RequestParam(required = false) String confirm_password,
	        @RequestParam(required = false) String password_obl,
	        @RequestParam(required = false) MultipartFile profilePic,
	        HttpSession session) {

	    Map<String, String> response = new HashMap<>();

	    // Obtener el usuario actual de la sesión
	    String usuarioActual = (String) session.getAttribute("usuario");
	    
    	if (usuarioActual == null || usuarioActual.isEmpty()) {
	        response.put("error", "Usuario no autenticado");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }
    	
        if (password_obl == null || password_obl.isEmpty()) {
            response.put("error", "Debes confirmar tu contraseña actual");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    	

	    try {
	    	Connection conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/fabrica", "root", "");
	        // 1. obtener contraseña actual de la base de datos
	        String contraseniaBD = null;
	        try (PreparedStatement stmt = conexion.prepareStatement("SELECT contrasenia FROM usuarios WHERE usuario = ?")) {
	            stmt.setString(1, usuarioActual);
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    contraseniaBD = rs.getString("contrasenia");
	                }
	            }
	        }

	        // 2. comparar con la contraseña introducida
	        if (contraseniaBD == null || !contraseniaBD.equals(password_obl)) {
	            response.put("error", "La contraseña actual no es correcta");
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	        }
	    	
	    	boolean cambiosRealizados = false;
	    	StringBuilder sql = new StringBuilder("UPDATE usuarios SET ");
	        ArrayList<Object> parametros = new ArrayList<>();

	        if (User != null && !User.isEmpty()) {
	            sql.append("usuario = ?, ");
	            parametros.add(User);
	            cambiosRealizados = true;
	        }

	        if (email != null && !email.isEmpty()) {
	            sql.append("correo = ?, ");
	            parametros.add(email);
	            cambiosRealizados = true;
	        }

	        if (password != null && !password.isEmpty()) {
	            if (!password.equals(confirm_password)) {
	                response.put("error", "La contraseña y la confirmación no coinciden");
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	            }
	            sql.append("contrasenia = ?, ");
	            parametros.add(password);
	            cambiosRealizados = true;
	        }

	        if (profilePic != null && !profilePic.isEmpty()) {
	        	
	        	// elimina el perfil anterior si existe
	            try (PreparedStatement stmt = conexion.prepareStatement("SELECT imagen FROM usuarios WHERE usuario = ?")) {
	                stmt.setString(1, usuarioActual);
	                try (ResultSet rs = stmt.executeQuery()) {
	                    if (rs.next()) {
	                        String imagenAnterior = rs.getString("imagen");
	                        if (imagenAnterior != null && !imagenAnterior.isEmpty()) {
	                            File archivoAnterior = new File("src/main/resources/static/" + imagenAnterior);
	                            if (archivoAnterior.exists()) {
	                                archivoAnterior.delete();
	                            }
	                        }
	                    }
	                }
	            }
	        	
	            String nombreArchivo = usuarioActual + "_" + profilePic.getOriginalFilename();
	            String rutaDestino = "src/main/resources/static/imagenes/" + nombreArchivo;
	            File destino = new File(rutaDestino);
	            destino.getParentFile().mkdirs();
	            Files.copy(profilePic.getInputStream(), Paths.get(rutaDestino), StandardCopyOption.REPLACE_EXISTING);
	            String imagenRuta = "imagenes/" + nombreArchivo;

	            sql.append("imagen = ?, ");
	            parametros.add(imagenRuta);
	            cambiosRealizados = true;
	        }

	        if (!cambiosRealizados) {
	            response.put("error", "No se ha enviado ningún dato para actualizar");
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	        }

	        // eliminar la ultima coma y espacio
	        sql.setLength(sql.length() - 2);
	        sql.append(" WHERE usuario = ?");
	        parametros.add(usuarioActual);

	        try (PreparedStatement pstmt = conexion.prepareStatement(sql.toString())) {
	            for (int i = 0; i < parametros.size(); i++) {
	                pstmt.setObject(i + 1, parametros.get(i));
	            }

	            int filasAfectadas = pstmt.executeUpdate();

	            if (filasAfectadas > 0) {
	                // Si se cambió el nombre de usuario, actualizarlo también en la sesión
	                if (User != null && !User.isEmpty()) {
	                    session.setAttribute("usuario", User);
	                }

	                response.put("mensaje", "Datos actualizados correctamente");
	                return ResponseEntity.ok(response);
	            } else {
	                response.put("error", "No se encontró al usuario para actualizar");
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("error", "Error al actualizar los datos");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}
	
	
    /**
     * devuelve la URL de la imagen de perfil del usuario autenticado.
     *
     * @param session Objeto HttpSession para identificar al usuario actual.
     * @return ResponseEntity con la URL de la imagen o mensaje de error si no hay sesion.
     */
	@GetMapping("/imagenes")
	@ResponseBody
	public ResponseEntity<Map<String, String>> obtenerImagenPerfil(HttpSession session) {
	    Map<String, String> response = new HashMap<>();

	    String usuarioActual = (String) session.getAttribute("usuario");

	    if (usuarioActual == null || usuarioActual.isEmpty()) {
	        response.put("error", "Usuario no autenticado");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }

	    String imagen = ImagenUsuario.obtenerImagenUsuario(usuarioActual);
	    response.put("url", imagen);
	    return ResponseEntity.ok(response);
	}
	
	
    /**
     * metodo que cierra la sesion del usuario y redirige a la pagina de login
     * 
     * @param session sesion actual del usuario
     * @return redireccion a la pagina de login
     */
	@GetMapping("/logout")
	public String cerrarSesion(HttpSession session) {
	    session.invalidate(); // cierra la sesion
	    return "redirect:/login"; 
	}

}