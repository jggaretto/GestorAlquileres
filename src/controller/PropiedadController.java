package controller;

import model.Propiedad;
import repository.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropiedadController {

    public boolean agregar(Propiedad p) {
        String sql = "INSERT INTO propiedades (direccion, tipo, precio_mensual, disponible, id_propietario) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getDireccion());
            ps.setString(2, p.getTipo());
            ps.setDouble(3, p.getPrecioMensual());
            ps.setBoolean(4, p.isDisponible());
            ps.setInt(5, p.getIdPropietario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al agregar propiedad: " + e.getMessage());
            return false;
        }
    }

    public List<Propiedad> listar() {
        List<Propiedad> lista = new ArrayList<>();
        String sql = "SELECT * FROM propiedades";
        try (Connection conn = Conexion.getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Propiedad(
                    rs.getInt("id"),
                    rs.getString("direccion"),
                    rs.getString("tipo"),
                    rs.getDouble("precio_mensual"),
                    rs.getBoolean("disponible"),
                    rs.getInt("id_propietario")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar propiedades: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Propiedad p) {
        String sql = "UPDATE propiedades SET direccion=?, tipo=?, precio_mensual=?, disponible=?, id_propietario=? WHERE id=?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getDireccion());
            ps.setString(2, p.getTipo());
            ps.setDouble(3, p.getPrecioMensual());
            ps.setBoolean(4, p.isDisponible());
            ps.setInt(5, p.getIdPropietario());
            ps.setInt(6, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar propiedad: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM propiedades WHERE id=?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar propiedad: " + e.getMessage());
            return false;
        }
    }
}