package Vista;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/panel")
public class Panel {
	
	@GetMapping
    public ModelAndView redireccion(HttpSession session) {
        
        String User = (String) session.getAttribute("Usuario");
        if (User.isEmpty()) {
        	return new ModelAndView("/login");
        } else {
  
        	return new ModelAndView("/panel");
        }
    }
	
}
