package Vista;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

/**
 * controlador encargado de manejar el panel de usuario y la sesion
 * 
 * ruta base: /panel
 */
@Controller
@RequestMapping("/panel")
public class Panel {
    
    /**
     * metodo que muestra el panel de usuario si el session esta inicializada
     * obtiene la imagen de perfil del usuario actual
     * 
     * @param session sesion actual del usuario
     * @return vista del panel principal de la pagina web
     */
	@GetMapping
    public ModelAndView mostrarPanel(HttpSession session) {
		
		String usuarioActual = (String) session.getAttribute("usuario");
    	ImagenUsuario.obtenerImagenUsuario(usuarioActual);
    	
    	if (session.getAttribute("cod") == null ) {
			return new ModelAndView("/login");
		}else {
			return new ModelAndView("/panel");
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
