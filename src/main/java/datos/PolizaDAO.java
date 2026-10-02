package datos;

import conexion.Conexion;
import modelo.Poliza;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PolizaDAO {

    public List<Poliza> listar(String filtro) {
        List<Poliza> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre_completo AS cliente, a.nombre_comercial AS aseguradora, "
                   + "ts.nombre AS tipo_seguro, u.nombre_completo AS agente "
                   + "FROM polizas p "
                   + "INNER JOIN clientes c ON p.id_cliente = c.id_cliente "
                   + "INNER JOIN aseguradoras a ON p.id_aseguradora = a.id_aseguradora "
                   + "INNER JOIN tipos_seguro ts ON p.id_tipo_seguro = ts.id_tipo_seguro "
                   + "INNER JOIN usuarios u ON p.id_agente = u.id_usuario "
                   + "WHERE (p.num_poliza LIKE ? OR c.nombre_completo LIKE ? OR a.nombre_comercial LIKE ?) "
                   + "ORDER BY p.id_poliza DESC";

        try (Connection cn = Conexion.getInstancia();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            String valor = "%" + filtro + "%";
            ps.setString(1, valor);
            ps.setString(2, valor);
            ps.setString(3, valor);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Poliza p = new Poliza();
                    p.setIdPoliza(rs.getInt("id_poliza"));
                    p.setNumPoliza(rs.getString("num_poliza"));
                    p.setIdCliente(rs.getInt("id_cliente"));
                    p.setIdAseguradora(rs.getInt("id_aseguradora"));
                    p.setIdTipoSeguro(rs.getInt("id_tipo_seguro"));
                    p.setIdAgente(rs.getInt("id_agente"));
                    p.setFechaInicio(rs.getDate("fecha_inicio"));
                    p.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
                    p.setPrimaAnual(rs.getDouble("prima_anual"));
                    p.setEstadoPoliza(rs.getString("estado_poliza"));

                    // Datos legibles para la tabla
                    p.setNombreCliente(rs.getString("cliente"));
                    p.setNombreAseguradora(rs.getString("aseguradora"));
                    p.setNombreTipoSeguro(rs.getString("tipo_seguro"));
                    p.setNombreAgente(rs.getString("agente"));

                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pólizas: " + e.getMessage());
        }
        return lista;
    }

    public boolean registrar(Poliza p) {
        String sql = "INSERT INTO polizas (num_poliza, id_cliente, id_aseguradora, id_tipo_seguro, id_agente, fecha_inicio, fecha_vencimiento, prima_anual, estado_poliza) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection cn = Conexion.getInstancia();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, p.getNumPoliza());
            ps.setInt(2, p.getIdCliente());
            ps.setInt(3, p.getIdAseguradora());
            ps.setInt(4, p.getIdTipoSeguro());
            ps.setInt(5, p.getIdAgente());
            ps.setDate(6, p.getFechaInicio());
            ps.setDate(7, p.getFechaVencimiento());
            ps.setDouble(8, p.getPrimaAnual());
            ps.setString(9, p.getEstadoPoliza());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar póliza: " + e.getMessage());
            return false;
        }
    }

    public boolean modificar(Poliza p) {
        String sql = "UPDATE polizas SET num_poliza=?, id_cliente=?, id_aseguradora=?, id_tipo_seguro=?, id_agente=?, fecha_inicio=?, fecha_vencimiento=?, prima_anual=?, estado_poliza=? WHERE id_poliza=?";
        try (Connection cn = Conexion.getInstancia();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, p.getNumPoliza());
            ps.setInt(2, p.getIdCliente());
            ps.setInt(3, p.getIdAseguradora());
            ps.setInt(4, p.getIdTipoSeguro());
            ps.setInt(5, p.getIdAgente());
            ps.setDate(6, p.getFechaInicio());
            ps.setDate(7, p.getFechaVencimiento());
            ps.setDouble(8, p.getPrimaAnual());
            ps.setString(9, p.getEstadoPoliza());
            ps.setInt(10, p.getIdPoliza());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al modificar póliza: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idPoliza) {
        String sql = "DELETE FROM polizas WHERE id_poliza=?";
        try (Connection cn = Conexion.getInstancia();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idPoliza);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar póliza: " + e.getMessage());
            return false;
        }
    }
}