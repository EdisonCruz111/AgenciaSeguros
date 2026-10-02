package datos;

import conexion.Conexion;
import modelo.TipoSeguro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoSeguroDAO {

    public List<TipoSeguro> listar(String filtro) {
        List<TipoSeguro> lista = new ArrayList<>();
        String sql = "SELECT * FROM tipos_seguro WHERE (nombre LIKE ? OR descripcion LIKE ?) ORDER BY id_tipo_seguro DESC";

        try (Connection cn = Conexion.getInstancia();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            String valor = "%" + filtro + "%";
            ps.setString(1, valor);
            ps.setString(2, valor);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TipoSeguro ts = new TipoSeguro();
                    ts.setIdTipoSeguro(rs.getInt("id_tipo_seguro"));
                    ts.setNombre(rs.getString("nombre"));
                    ts.setDescripcion(rs.getString("descripcion"));
                    lista.add(ts);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar tipos de seguro: " + e.getMessage());
        }
        return lista;
    }

    public boolean registrar(TipoSeguro ts) {
        String sql = "INSERT INTO tipos_seguro (nombre, descripcion) VALUES (?, ?)";
        try (Connection cn = Conexion.getInstancia();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, ts.getNombre());
            ps.setString(2, ts.getDescripcion());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar tipo de seguro: " + e.getMessage());
            return false;
        }
    }

    public boolean modificar(TipoSeguro ts) {
        String sql = "UPDATE tipos_seguro SET nombre=?, descripcion=? WHERE id_tipo_seguro=?";
        try (Connection cn = Conexion.getInstancia();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, ts.getNombre());
            ps.setString(2, ts.getDescripcion());
            ps.setInt(3, ts.getIdTipoSeguro());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al modificar tipo de seguro: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idTipoSeguro) {
        String sql = "DELETE FROM tipos_seguro WHERE id_tipo_seguro=?";
        try (Connection cn = Conexion.getInstancia();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idTipoSeguro);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar tipo de seguro: " + e.getMessage());
            return false;
        }
    }
}