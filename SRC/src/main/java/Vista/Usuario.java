package Vista;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/usuario")
public class Usuario {
    
	@GetMapping
    public ModelAndView mostrarUsuario() {
        return new ModelAndView("usuario"); 
    }
	
}