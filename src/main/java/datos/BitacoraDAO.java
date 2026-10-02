package datos;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class BitacoraDAO {

    public void buscarBitacora(javax.swing.JTable tabla, String filtro) {
    String[] columnas = {"ID", "Usuario", "Fecha y Hora", "IP Origen", "Acción Realizada"};
    DefaultTableModel modelo = new DefaultTableModel(null, columnas);
    
    String sql = "SELECT b.id_log, u.username, b.fecha_ingreso, b.ip_origen, b.accion " +
                 "FROM bitacora_accesos b " +
                 "JOIN usuarios u ON b.id_usuario = u.id_usuario " +
                 "WHERE u.username LIKE ? OR b.accion LIKE ? " +
                 "ORDER BY b.fecha_ingreso DESC";
    
    try (Connection con = Conexion.getInstancia();
     PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, "%" + filtro + "%");
        ps.setString(2, "%" + filtro + "%");
        
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getInt("id_log");
                fila[1] = rs.getString("username");
                fila[2] = rs.getString("fecha_ingreso");
                fila[3] = rs.getString("ip_origen");
                fila[4] = rs.getString("accion");
                modelo.addRow(fila);
            }
            tabla.setModel(modelo);
            tabla.setDefaultEditor(Object.class, null);
        }
        
    } catch (SQLException e) {
        System.err.println("Error al buscar en la bitácora: " + e.getMessage());
    }
}
    // Método para poblar directamente un DefaultTableModel para la JTable
    public void cargarTablaBitacora(javax.swing.JTable tabla) {
        String[] columnas = {"ID", "Usuario", "Fecha y Hora", "IP Origen", "Acción Realizada"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas);
        
        String sql = "SELECT b.id_log, u.username, b.fecha_ingreso, b.ip_origen, b.accion " +
                     "FROM bitacora_accesos b " +
                     "JOIN usuarios u ON b.id_usuario = u.id_usuario " +
                     "ORDER BY b.fecha_ingreso DESC";
        
        try (Connection cn = Conexion.getInstancia();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getInt("id_log");
                fila[1] = rs.getString("username");
                fila[2] = rs.getString("fecha_ingreso");
                fila[3] = rs.getString("ip_origen");
                fila[4] = rs.getString("accion");
                modelo.addRow(fila);
            }
            
            tabla.setModel(modelo);
            
        } catch (SQLException e) {
            System.err.println("Error al cargar la bitácora: " + e.getMessage());
        }
    }
}