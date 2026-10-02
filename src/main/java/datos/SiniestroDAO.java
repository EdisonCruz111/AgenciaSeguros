package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Siniestro;

public class SiniestroDAO {

    // Helper con manejo dinámico para evitar errores entre 'conexion' y 'Conexion'
    private Connection obtenerConexion() {
    try {
        Class<?> claseConexion;
        try {
            claseConexion = Class.forName("conexion.Conexion");
        } catch (ClassNotFoundException e) {
            claseConexion = Class.forName("Conexion.Conexion");
        }

        // 1. Intentar invocar getConexion() directamente
        try {
            return (Connection) claseConexion.getMethod("getConexion").invoke(null);
        } catch (NoSuchMethodException e) {
            // 2. Si no existe getConexion(), buscar automáticamente cualquier método estático que devuelva Connection
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

    // 1. LISTAR / BUSCAR
    public List<Siniestro> listar(String filtro) {
        List<Siniestro> lista = new ArrayList<>();
        String sql = "SELECT id_siniestro, num_reclamo, id_poliza, fecha_ocurrencia, fecha_reporte, " +
                     "descripcion_hechos, monto_reclamado, monto_aprobado, estado_reclamo " +
                     "FROM siniestros_reclamos " +
                     "WHERE num_reclamo LIKE ? OR estado_reclamo LIKE ? OR descripcion_hechos LIKE ? " +
                     "ORDER BY id_siniestro DESC";

        Connection cn = obtenerConexion();
        if (cn == null) return lista;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            String p = "%" + filtro + "%";
            ps.setString(1, p);
            ps.setString(2, p);
            ps.setString(3, p);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Siniestro s = new Siniestro();
                s.setIdSiniestro(rs.getInt("id_siniestro"));
                s.setNumReclamo(rs.getString("num_reclamo"));
                s.setIdPoliza(rs.getInt("id_poliza"));
                s.setFechaOcurrencia(rs.getTimestamp("fecha_ocurrencia"));
                s.setFechaReporte(rs.getTimestamp("fecha_reporte"));
                s.setDescripcionHechos(rs.getString("descripcion_hechos"));
                s.setMontoReclamado(rs.getDouble("monto_reclamado"));
                s.setMontoAprobado(rs.getDouble("monto_aprobado"));
                s.setEstadoReclamo(rs.getString("estado_reclamo"));
                lista.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar siniestros: " + e.getMessage());
        }
        return lista;
    }

    // 2. INSERTAR
    public boolean insertar(Siniestro s) {
        String sql = "INSERT INTO siniestros_reclamos (num_reclamo, id_poliza, fecha_ocurrencia, fecha_reporte, " +
                     "descripcion_hechos, monto_reclamado, monto_aprobado, estado_reclamo) " +
                     "VALUES (?, ?, ?, datetime('now', 'localtime'), ?, ?, ?, ?)";
        Connection cn = obtenerConexion();
        if (cn == null) return false;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, s.getNumReclamo());
            ps.setInt(2, s.getIdPoliza());
            ps.setTimestamp(3, s.getFechaOcurrencia());
            ps.setString(4, s.getDescripcionHechos());
            ps.setDouble(5, s.getMontoReclamado());
            ps.setDouble(6, s.getMontoAprobado());
            ps.setString(7, s.getEstadoReclamo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar siniestro: " + e.getMessage());
            return false;
        }
    }

    // 3. ACTUALIZAR / EDITAR
    public boolean actualizar(Siniestro s) {
        String sql = "UPDATE siniestros_reclamos SET num_reclamo = ?, id_poliza = ?, fecha_ocurrencia = ?, " +
                     "descripcion_hechos = ?, monto_reclamado = ?, monto_aprobado = ?, estado_reclamo = ? " +
                     "WHERE id_siniestro = ?";
        Connection cn = obtenerConexion();
        if (cn == null) return false;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, s.getNumReclamo());
            ps.setInt(2, s.getIdPoliza());
            ps.setTimestamp(3, s.getFechaOcurrencia());
            ps.setString(4, s.getDescripcionHechos());
            ps.setDouble(5, s.getMontoReclamado());
            ps.setDouble(6, s.getMontoAprobado());
            ps.setString(7, s.getEstadoReclamo());
            ps.setInt(8, s.getIdSiniestro());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar siniestro: " + e.getMessage());
            return false;
        }
    }

    // 4. ELIMINAR
    public boolean eliminar(int idSiniestro) {
        String sql = "DELETE FROM siniestros_reclamos WHERE id_siniestro = ?";
        Connection cn = obtenerConexion();
        if (cn == null) return false;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idSiniestro);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar siniestro: " + e.getMessage());
            return false;
        }
    }
}