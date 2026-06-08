package repository;

import model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDAO {

    public Usuario validarLogin(String username, String passwordIngresada) {
        Usuario usuarioValidado = null;

        String sql = "SELECT id, username, password, rol FROM usuarios WHERE username = ?";

        try (Connection conn = Conexion.getConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setString(1, username);

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            String passwordGuardada = rs.getString("password");

                            if (BCrypt.checkpw(passwordIngresada, passwordGuardada)) {
                                usuarioValidado = new Usuario();
                                usuarioValidado.setId(rs.getInt("id"));
                                usuarioValidado.setUsername(rs.getString("username"));
                                usuarioValidado.setRol(rs.getString("rol"));
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en la consulta: " + e.getMessage());
        }

        return usuarioValidado;
    }
}