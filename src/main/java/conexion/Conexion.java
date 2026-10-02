package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL = "jdbc:mysql://localhost:3306/agencia_seguros?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "edisoncruz";
    private static Connection cn = null;

    private Conexion() {}

    public static Connection getInstancia() throws SQLException {
        if (cn == null || cn.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                cn = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Error al cargar el driver JDBC: " + e.getMessage());
            }
        }
        return cn;
    }

    public static void cerrarConexion() {
        if (cn != null) {
            try {
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}