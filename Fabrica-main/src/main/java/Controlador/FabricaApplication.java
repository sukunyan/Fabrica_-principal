package Controlador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import Vista.Login;

/**
 * clase principal que inicia la aplicacion spring boot
 */
@SpringBootApplication(scanBasePackages = "Vista")
@ComponentScan(basePackages = "Vista")
public class FabricaApplication {

	/**
	 * metodo principal que lanza la aplicacion
	 * 
	 * @param args argumentos de linea de comandos
	 */
	public static void main(String[] args) {
		SpringApplication.run(FabricaApplication.class, args);
		
		Login login = new Login();
		
		login.abrirNavegador("http://localhost:8080/login");
		
	}

}
