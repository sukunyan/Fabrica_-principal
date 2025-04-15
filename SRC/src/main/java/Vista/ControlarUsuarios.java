package Vista;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/logout")
public class ControlarUsuarios {
	@PostMapping
    public String cerrarSesion(HttpSession session) {
        session.invalidate(); // Esto elimina la sesión del usuario
        return "redirect:/login"; // Redirige a la página de login
    }
}
