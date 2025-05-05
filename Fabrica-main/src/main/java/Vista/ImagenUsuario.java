package Vista;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * clase que gestiona la obtencion de la imagen de perfil del usuario
 */
public class ImagenUsuario {

    /**
     * metodo que obtiene la ruta de la imagen de perfil del usuario a partir de su nombre
     * 
     * @param nombreUsuario nombre del usuario
     * @return ruta de la imagen del usuario, o una imagen por defecto si no se encuentra
     */
    public static String obtenerImagenUsuario(String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.isEmpty()) {
            return null;
        }

        try {
        	Connection conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/fabrica", "root", "");
            PreparedStatement pstmt = conexion.prepareStatement("SELECT imagen FROM usuarios WHERE usuario = ?");
            pstmt.setString(1, nombreUsuario);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String imagen = rs.getString("imagen");
                if (imagen != null && !imagen.isEmpty()) {
                    return "/" + imagen;
                } else {
                    return "/imagenes/default.png";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "/img/default.png";
    }
}
