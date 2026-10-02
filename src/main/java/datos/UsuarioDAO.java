package datos;

import conexion.Conexion;
import modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    
    public java.util.List<Usuario> listar(String filtro) {
    java.util.List<Usuario> lista = new java.util.ArrayList<>();
    String sql = "SELECT u.id_usuario, u.id_rol, u.username, u.nombre_completo, u.correo, u.estado, r.nombre AS nombre_rol " +
                 "FROM usuarios u " +
                 "INNER JOIN roles r ON u.id_rol = r.id_rol " +
                 "WHERE u.username LIKE ? OR u.nombre_completo LIKE ? " +
                 "ORDER BY u.id_usuario DESC";

    try (Connection cn = Conexion.getInstancia();
         PreparedStatement ps = cn.prepareStatement(sql)) {

        ps.setString(1, "%" + filtro + "%");
        ps.setString(2, "%" + filtro + "%");

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setIdRol(rs.getInt("id_rol"));
                u.setUsername(rs.getString("username"));
                u.setNombreCompleto(rs.getString("nombre_completo"));
                u.setEmail(rs.getString("correo"));
                u.setActivo(rs.getBoolean("estado"));
                u.setNombreRol(rs.getString("nombre_rol"));
                lista.add(u);
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al listar usuarios: " + e.getMessage());
    }
    return lista;
}

public boolean registrar(Usuario u) {
    String sql = "INSERT INTO usuarios (id_rol, username, password, nombre_completo, correo, estado) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection cn = Conexion.getInstancia();
         PreparedStatement ps = cn.prepareStatement(sql)) {

        ps.setInt(1, u.getIdRol());
        ps.setString(2, u.getUsername());
        ps.setString(3, u.getPassword());
        ps.setString(4, u.getNombreCompleto());
        ps.setString(5, u.getEmail());
        ps.setBoolean(6, u.isActivo());

        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error al registrar usuario: " + e.getMessage());
        return false;
    }
}

public boolean modificar(Usuario u) {
    String sql = "UPDATE usuarios SET id_rol = ?, username = ?, nombre_completo = ?, correo = ?, estado = ? WHERE id_usuario = ?";
    try (Connection cn = Conexion.getInstancia();
         PreparedStatement ps = cn.prepareStatement(sql)) {

        ps.setInt(1, u.getIdRol());
        ps.setString(2, u.getUsername());
        ps.setString(3, u.getNombreCompleto());
        ps.setString(4, u.getEmail());
        ps.setBoolean(5, u.isActivo());
        ps.setInt(6, u.getIdUsuario());

        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error al modificar usuario: " + e.getMessage());
        return false;
    }
}

public boolean cambiarEstado(int idUsuario, boolean nuevoEstado) {
    String sql = "UPDATE usuarios SET estado = ? WHERE id_usuario = ?";
    try (Connection cn = Conexion.getInstancia();
         PreparedStatement ps = cn.prepareStatement(sql)) {

        ps.setBoolean(1, nuevoEstado);
        ps.setInt(2, idUsuario);

        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error al cambiar estado: " + e.getMessage());
        return false;
    }
}

    public Usuario validarLogin(String user, String pass) {
        Usuario usuario = null;
        String sql = "SELECT u.id_usuario, u.id_rol, u.username, u.nombre_completo, u.correo, u.estado, r.nombre AS nombre_rol " +
                     "FROM usuarios u " +
                     "INNER JOIN roles r ON u.id_rol = r.id_rol " +
                     "WHERE u.username = ? AND u.password = ? AND u.estado = TRUE";

        try (Connection cn = Conexion.getInstancia();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, user);
            ps.setString(2, pass);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setIdRol(rs.getInt("id_rol"));
                    usuario.setUsername(rs.getString("username"));
                    usuario.setNombreCompleto(rs.getString("nombre_completo"));
                    usuario.setEmail(rs.getString("correo"));
                    usuario.setActivo(rs.getBoolean("estado"));
                    usuario.setNombreRol(rs.getString("nombre_rol"));

                    registrarBitacora(usuario.getIdUsuario(), "Inicio de sesión exitoso (" + usuario.getNombreRol() + ")");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al validar login: " + e.getMessage());
        }
        return usuario;
    }

    private void registrarBitacora(int idUsuario, String accion) {
        String sql = "INSERT INTO bitacora_accesos (id_usuario, ip_origen, accion) VALUES (?, '127.0.0.1', ?)";
        try (Connection cn = Conexion.getInstancia();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setString(2, accion);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al registrar en bitácora: " + e.getMessage());
        }
    }
}