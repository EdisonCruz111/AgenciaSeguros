package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PagosDAO {

    // Helper con manejo dinámico idéntico para evitar errores de conexión
    private Connection obtenerConexion() {
        try {
            Class<?> claseConexion;
            try {
                claseConexion = Class.forName("conexion.Conexion");
            } catch (ClassNotFoundException e) {
                claseConexion = Class.forName("Conexion.Conexion");
            }

            try {
                return (Connection) claseConexion.getMethod("getConexion").invoke(null);
            } catch (NoSuchMethodException e) {
                for (java.lang.reflect.Method m : claseConexion.getMethods()) {
                    if (java.lang.reflect.Modifier.isStatic(m.getModifiers()) 
                            && Connection.class.isAssignableFrom(m.getReturnType()) 
                            && m.getParameterCount() == 0) {
                        return (Connection) m.invoke(null);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener conexion: " + e.getMessage());
        }
        return null;
    }

    public void cargarTablaPagos(JTable tabla) {
        // 1. Quitamos "Usuario" de las cabeceras de la tabla
    javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(
        new Object[]{"ID Pago", "N° Recibo", "Póliza", "Método de Pago", "Monto", "Fecha"}, 0
    );
    
    // 2. Quitamos el JOIN con usuarios y el campo de la consulta SELECT
    // (Asegúrate de que 'num_poliza' coincida con el nombre real de tu columna en la tabla polizas)
    String sql = "SELECT p.id_pago, p.num_recibo, po.num_poliza, m.nombre AS metodo, p.monto_pagado, p.fecha_pago " +
                 "FROM pagos_prima p " +
                 "JOIN polizas po ON p.id_poliza = po.id_poliza " +
                 "JOIN metodos_pago m ON p.id_metodo_pago = m.id_metodo";
                 
    try (Connection cn = conexion.Conexion.getInstancia();
         PreparedStatement ps = cn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        
        while (rs.next()) {
            Object[] fila = {
                rs.getInt("id_pago"),
                rs.getString("num_recibo"),
                rs.getString("num_poliza"),
                rs.getString("metodo"),
                rs.getDouble("monto_pagado"),
                rs.getTimestamp("fecha_pago")
            };
            modelo.addRow(fila);
        }
        tabla.setModel(modelo);
        
    } catch (SQLException e) {
        System.err.println("Error al cargar los pagos de prima: " + e.getMessage());
    }
}
    
    public boolean registrarPago(String numRecibo, int idPoliza, int idMetodoPago, double montoPagado, int idUsuarioCobro) {
        String sql = "INSERT INTO pagos_prima (num_recibo, id_poliza, id_metodo_pago, monto_pagado, fecha_pago, id_usuario_cobro) " +
                     "VALUES (?, ?, ?, ?, NOW(), ?)";
        
        Connection cn = obtenerConexion();
        if (cn == null) return false;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, numRecibo);
            ps.setInt(2, idPoliza);
            ps.setInt(3, idMetodoPago);
            ps.setDouble(4, montoPagado);
            ps.setInt(5, idUsuarioCobro);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al registrar el pago: " + e.getMessage());
            return false;
        }
    }
}