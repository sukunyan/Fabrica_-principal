package Vista;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/panel")
public class Panel {
    
	@GetMapping
    public ModelAndView mostrarPanel() {
        return new ModelAndView("panel"); 
    }
	
}
