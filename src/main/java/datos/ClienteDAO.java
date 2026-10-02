package datos;

import conexion.Conexion;
import modelo.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // Listar clientes usando los campos reales de MySQL
    public List<Cliente> listar(String filtro) {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE (identificacion LIKE ? OR nombre_completo LIKE ?) ORDER BY id_cliente DESC";

        try (Connection cn = Conexion.getInstancia();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            String valor = "%" + filtro + "%";
            ps.setString(1, valor);
            ps.setString(2, valor);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cliente c = new Cliente();
                    c.setIdCliente(rs.getInt("id_cliente"));
                    c.setDni(rs.getString("identificacion")); // Lee 'identificacion'
                    
                    // Separar nombre_completo para rellenar tus campos de modelo si es necesario
                    String completo = rs.getString("nombre_completo");
                    c.setNombre(completo); 
                    c.setApellido(""); 
                    
                    c.setTelefono(rs.getString("telefono"));
                    c.setCorreo(rs.getString("correo"));
                    c.setDireccion(rs.getString("direccion"));
                    c.setEstado(rs.getBoolean("estado"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }
        return lista;
    }

    // Registrar cliente concatenando Nombre y Apellido hacia 'nombre_completo'
    public boolean registrar(Cliente c) {
        String sql = "INSERT INTO clientes (identificacion, nombre_completo, telefono, correo, direccion, estado) VALUES (?, ?, ?, ?, ?, TRUE)";
        try (Connection cn = Conexion.getInstancia();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            String nombreCompleto = (c.getNombre() + " " + c.getApellido()).trim();

            ps.setString(1, c.getDni());
            ps.setString(2, nombreCompleto);
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getCorreo());
            ps.setString(5, c.getDireccion());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar cliente: " + e.getMessage());
            return false;
        }
    }

    // Modificar cliente usando las columnas correctas
    public boolean modificar(Cliente c) {
        String sql = "UPDATE clientes SET identificacion=?, nombre_completo=?, telefono=?, correo=?, direccion=?, estado=? WHERE id_cliente=?";
        try (Connection cn = Conexion.getInstancia();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            String nombreCompleto = (c.getNombre() + " " + c.getApellido()).trim();

            ps.setString(1, c.getDni());
            ps.setString(2, nombreCompleto);
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getCorreo());
            ps.setString(5, c.getDireccion());
            ps.setBoolean(6, c.isEstado());
            ps.setInt(7, c.getIdCliente());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al modificar cliente: " + e.getMessage());
            return false;
        }
    }

    // Eliminación lógica / Desactivación
    public boolean cambiarEstado(int idCliente, boolean estado) {
        String sql = "UPDATE clientes SET estado=? WHERE id_cliente=?";
        try (Connection cn = Conexion.getInstancia();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setBoolean(1, estado);
            ps.setInt(2, idCliente);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado: " + e.getMessage());
            return false;
        }
    }
}