package repository;

import model.Propietario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropietarioDAO {

    // --- INSERT ---
    public boolean agregar(Propietario p) {
        String sql = "INSERT INTO propietarios (nombre, apellido, dni, telefono, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getApellido());
            ps.setString(3, p.getDni());
            ps.setString(4, p.getTelefono());
            ps.setString(5, p.getEmail());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al agregar propietario: " + e.getMessage());
            return false;
        }
    }

    // --- SELECT ALL ---
    public List<Propietario> listar() {
        List<Propietario> lista = new ArrayList<>();
        String sql = "SELECT * FROM propietarios";
        try (Connection conn = Conexion.getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Propietario(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("dni"),
                    rs.getString("telefono"),
                    rs.getString("email")
                ));
            }

        } catch (Exception e) {
            System.out.println("Error al listar propietarios: " + e.getMessage());
        }
        return lista;
    }

    // --- UPDATE ---
    public boolean actualizar(Propietario p) {
        String sql = "UPDATE propietarios SET nombre=?, apellido=?, dni=?, telefono=?, email=? WHERE id=?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getApellido());
            ps.setString(3, p.getDni());
            ps.setString(4, p.getTelefono());
            ps.setString(5, p.getEmail());
            ps.setInt(6, p.getId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al actualizar propietario: " + e.getMessage());
            return false;
        }
    }

    // --- DELETE ---
    public boolean eliminar(int id) {
        String sql = "DELETE FROM propietarios WHERE id=?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al eliminar propietario: " + e.getMessage());
            return false;
        }
    }

    // --- BUSCAR POR NOMBRE/APELLIDO O ID ---
    public List<Propietario> buscar(String texto) {
    List<Propietario> lista = new ArrayList<>();
    String sql;

    if (texto.matches("\\d+")) {
        sql = "SELECT * FROM propietarios WHERE id = ? OR dni = ?";
    } else {
        sql = "SELECT * FROM propietarios WHERE nombre LIKE ? OR apellido LIKE ?";
    }

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        if (texto.matches("\\d+")) {
            ps.setInt(1, Integer.parseInt(texto));
            ps.setString(2, texto);
        } else {
            String patron = "%" + texto + "%";
            ps.setString(1, patron);
            ps.setString(2, patron);
        }

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lista.add(new Propietario(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("dni"),
                rs.getString("telefono"),
                rs.getString("email")
            ));
        }

    } catch (Exception e) {
        System.out.println("Error al buscar propietario: " + e.getMessage());
    }
    return lista;
    }
}