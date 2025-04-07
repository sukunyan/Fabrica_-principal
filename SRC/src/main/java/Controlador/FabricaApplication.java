package Controlador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import Vista.Login;

@SpringBootApplication(scanBasePackages = "Vista")
@ComponentScan(basePackages = "Vista")
public class FabricaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FabricaApplication.class, args);
		
		Login login = new Login();
		
		login.abrirNavegador("http://localhost:8080/login");
		
	}

}
