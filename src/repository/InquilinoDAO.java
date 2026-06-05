package repository;

import model.Inquilino;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InquilinoDAO {

    // --- INSERT ---
    public boolean insertar(Inquilino i) {
        String sql = "INSERT INTO inquilinos (nombre, apellido, dni, telefono) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, i.getNombre());
            ps.setString(2, i.getApellido());
            ps.setString(3, i.getDni());
            ps.setString(4, i.getTelefono());
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error al insertar inquilino: " + e.getMessage());
            return false;
        }
    }

    // --- UPDATE ---
    public boolean modificar(Inquilino i) {
        String sql = "UPDATE inquilinos SET nombre=?, apellido=?, dni=?, telefono=? WHERE id=?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, i.getNombre());
            ps.setString(2, i.getApellido());
            ps.setString(3, i.getDni());
            ps.setString(4, i.getTelefono());
            ps.setInt(5, i.getId());
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error al modificar inquilino: " + e.getMessage());
            return false;
        }
    }

    // --- DELETE ---
    public boolean eliminar(int id) {
        String sql = "DELETE FROM inquilinos WHERE id=?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error al eliminar inquilino: " + e.getMessage());
            return false;
        }
    }

    // --- SELECT ALL ---
    public List<Inquilino> listarTodos() {
        List<Inquilino> lista = new ArrayList<>();
        String sql = "SELECT * FROM inquilinos";
        try (Connection conn = Conexion.getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Inquilino(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("dni"),
                    rs.getString("telefono")
                ));
            }

        } catch (Exception e) {
            System.out.println("Error al listar inquilinos: " + e.getMessage());
        }
        return lista;
    }

    // --- SELECT BY ID ---
    public Inquilino buscarPorId(int id) {
        String sql = "SELECT * FROM inquilinos WHERE id=?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Inquilino(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("dni"),
                    rs.getString("telefono")
                );
            }

        } catch (Exception e) {
            System.out.println("Error al buscar inquilino: " + e.getMessage());
        }
        return null;
    }

    // --- BUSCAR POR NOMBRE/APELLIDO O ID ---
    public List<Inquilino> buscar(String texto) {
        List<Inquilino> lista = new ArrayList<>();
        String sql;

        if (texto.matches("\\d+")) {
            sql = "SELECT * FROM inquilinos WHERE id = ?";
        } else {
            sql = "SELECT * FROM inquilinos WHERE nombre LIKE ? OR apellido LIKE ?";
        }

        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (texto.matches("\\d+")) {
                ps.setInt(1, Integer.parseInt(texto));
            } else {
                String patron = "%" + texto + "%";
                ps.setString(1, patron);
                ps.setString(2, patron);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Inquilino(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("dni"),
                    rs.getString("telefono")
                ));
            }

        } catch (Exception e) {
            System.out.println("Error al buscar: " + e.getMessage());
        }
        return lista;
    }

    // -- VERIFICAR DNI DUPLICADO ---
    public boolean existeDni(String dni){
        String sql = "SELECT COUNT(*) FROM inquilinos WHERE dni = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            System.out.println("Error al verificar DNI: " + e.getMessage());
        }
        return false;
    } 

}