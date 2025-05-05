package Vista;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

/**
 * clase de configuracion que permite registrar los manejadores de recursos estaticos
 * como hojas de estilo css, imagenes y archivos javascript.
 */
@Configuration
@EnableWebMvc
public class Configuraciones implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        
        registry.addResourceHandler("/images/**")
        		.addResourceLocations("classpath:/static/images/");
        
        registry.addResourceHandler("/js/**")
		.addResourceLocations("classpath:/static/js/");
        
        registry.addResourceHandler("/imagenes/**")
		.addResourceLocations("classpath:/static/imagenes/");
    }
}

