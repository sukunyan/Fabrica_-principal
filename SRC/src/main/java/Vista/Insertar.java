package Vista;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import Modelo.Coches;

@Controller
@RequestMapping("/insertar")
public class Insertar {
	
	Coches coches = new Coches();
	
	@GetMapping
    public ModelAndView AbrirInsertar() {
        return new ModelAndView("insertar");  
    }
	
	@PostMapping
	public ModelAndView InsertarCoche(@RequestParam String marca, @RequestParam String modelo, @RequestParam LocalDate anio, 
			@RequestParam String matricula, @RequestParam String numChasis) {
		
		coches.setMarca(marca);
		coches.setModelo(modelo);
		String fecha = anio.toString();
		coches.setFecha(fecha);
		coches.setMatricula(matricula);
		coches.setNumChasis(numChasis);
		
		
		
		try {
			
			Connection conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/fabrica", "root", "");
			
			Statement consulta = conexion.createStatement();
			
			consulta.executeUpdate("insert into coches(marca, modelo, anio, matricula, numChasis)" + " values('" + coches.getMarca() + "', '" + coches.getModelo() + "', '" + coches.getFecha() + "', " + "'" + coches.getMatricula() + "', '" + coches.getNumChasis() + "');");
			
			System.out.println("insert into coches(marca, modelo, anio, matricula, numChasis)" + " values('" + coches.getMarca() + "', '" + coches.getModelo() + "', '" + coches.getFecha() + "', " + "'" + coches.getMatricula() + "', '" + coches.getNumChasis() + "');");
			
			conexion.close();
			
		} catch (Exception e) {
			
			System.out.println(e);
			
		}
		
		return new ModelAndView("/panel");
	}

	
}
