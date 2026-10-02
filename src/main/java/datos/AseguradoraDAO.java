package datos;

import conexion.Conexion;
import modelo.Aseguradora;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AseguradoraDAO {

    public List<Aseguradora> listar(String filtro) {
        List<Aseguradora> lista = new ArrayList<>();
        String sql = "SELECT * FROM aseguradoras WHERE (rtn LIKE ? OR nombre_comercial LIKE ? OR contacto_principal LIKE ?) ORDER BY id_aseguradora DESC";

        try (Connection cn = Conexion.getInstancia();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            String valor = "%" + filtro + "%";
            ps.setString(1, valor);
            ps.setString(2, valor);
            ps.setString(3, valor);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Aseguradora a = new Aseguradora();
                    a.setIdAseguradora(rs.getInt("id_aseguradora"));
                    a.setRtn(rs.getString("rtn"));
                    a.setNombreComercial(rs.getString("nombre_comercial"));
                    a.setContactoPrincipal(rs.getString("contacto_principal"));
                    a.setTelefono(rs.getString("telefono"));
                    a.setCorreo(rs.getString("correo"));
                    lista.add(a);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar aseguradoras: " + e.getMessage());
        }
        return lista;
    }

    public boolean registrar(Aseguradora a) {
        String sql = "INSERT INTO aseguradoras (rtn, nombre_comercial, contacto_principal, telefono, correo) VALUES (?, ?, ?, ?, ?)";
        try (Connection cn = Conexion.getInstancia();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, a.getRtn());
            ps.setString(2, a.getNombreComercial());
            ps.setString(3, a.getContactoPrincipal());
            ps.setString(4, a.getTelefono());
            ps.setString(5, a.getCorreo());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar aseguradora: " + e.getMessage());
            return false;
        }
    }

    public boolean modificar(Aseguradora a) {
        String sql = "UPDATE aseguradoras SET rtn=?, nombre_comercial=?, contacto_principal=?, telefono=?, correo=? WHERE id_aseguradora=?";
        try (Connection cn = Conexion.getInstancia();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, a.getRtn());
            ps.setString(2, a.getNombreComercial());
            ps.setString(3, a.getContactoPrincipal());
            ps.setString(4, a.getTelefono());
            ps.setString(5, a.getCorreo());
            ps.setInt(6, a.getIdAseguradora());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al modificar aseguradora: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idAseguradora) {
        String sql = "DELETE FROM aseguradoras WHERE id_aseguradora=?";
        try (Connection cn = Conexion.getInstancia();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idAseguradora);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar aseguradora: " + e.getMessage());
            return false;
        }
    }
}