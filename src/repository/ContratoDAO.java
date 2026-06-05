package repository;

import model.Contrato;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContratoDAO {

    public boolean agregar(Contrato c) {
        String sql = "INSERT INTO contratos (fecha_inicio, fecha_fin, monto, id_inquilino, id_propiedad) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConexion()) {
            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(c.getFechaInicio()));
                ps.setDate(2, Date.valueOf(c.getFechaFin()));
                ps.setDouble(3, c.getMonto());
                ps.setInt(4, c.getIdInquilino());
                ps.setInt(5, c.getIdPropiedad());
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("Error al agregar contrato: " + e.getMessage());
            return false;
        }
    }

    public List<Contrato> listar() {
        List<Contrato> lista = new ArrayList<>();
        String sql = "SELECT * FROM contratos";
        try (Connection conn = Conexion.getConexion()) {
            if (conn == null) {
                return lista;
            }

            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    lista.add(new Contrato(
                        rs.getInt("id"),
                        rs.getInt("id_propiedad"),
                        rs.getInt("id_inquilino"),
                        rs.getDate("fecha_inicio").toLocalDate(),
                        rs.getDate("fecha_fin").toLocalDate(),
                        rs.getDouble("monto")
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("Error al listar contratos: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Contrato c) {
        String sql = "UPDATE contratos SET fecha_inicio=?, fecha_fin=?, monto=?, id_inquilino=?, id_propiedad=? WHERE id=?";
        try (Connection conn = Conexion.getConexion()) {
            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(c.getFechaInicio()));
                ps.setDate(2, Date.valueOf(c.getFechaFin()));
                ps.setDouble(3, c.getMonto());
                ps.setInt(4, c.getIdInquilino());
                ps.setInt(5, c.getIdPropiedad());
                ps.setInt(6, c.getId());
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar contrato: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM contratos WHERE id=?";
        try (Connection conn = Conexion.getConexion()) {
            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("Error al eliminar contrato: " + e.getMessage());
            return false;
        }
    }
}
