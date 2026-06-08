package repository;

import model.Usuario;
import repository.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario validarLogin(String username, String passwordIngresada) {
        Usuario usuarioValidado = null;
        
        // Comparamos password directamente (texto plano)
        String sql = "SELECT id, username, rol FROM usuarios WHERE username = ? AND password = ?";

        try (Connection conn = Conexion.getConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    
                    stmt.setString(1, username);
                    stmt.setString(2, passwordIngresada); // Enviamos el texto tal cual

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            usuarioValidado = new Usuario();
                            usuarioValidado.setId(rs.getInt("id"));
                            usuarioValidado.setUsername(rs.getString("username"));
                            usuarioValidado.setRol(rs.getString("rol"));
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