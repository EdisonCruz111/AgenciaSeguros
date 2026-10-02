package conexion;

import com.formdev.flatlaf.FlatLightLaf;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            System.err.println("Error al cargar FlatLaf: " + e.getMessage());
        }

        try {
            Connection cn = Conexion.getInstancia();
            if (cn != null && !cn.isClosed()) {
                JOptionPane.showMessageDialog(null, 
                    "¡Conexión exitosa a MySQL!\nTema visual FlatLaf activado correctamente.", 
                    "Sistema de Seguros", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Error de conexión: " + e.getMessage(), 
                "Error de Base de Datos", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}