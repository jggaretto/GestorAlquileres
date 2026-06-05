package repository;

import model.Pago;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO {

    public boolean agregar(Pago p) {
        String sql = "INSERT INTO pagos (mes, monto, estado, fecha_pago, id_contrato) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConexion()) {
            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, p.getMes());
                ps.setDouble(2, p.getMonto());
                ps.setString(3, p.getEstado());
                ps.setDate(4, Date.valueOf(p.getFechaPago()));
                ps.setInt(5, p.getIdContrato());
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("Error al agregar pago: " + e.getMessage());
            return false;
        }
    }

    public List<Pago> listar() {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM pagos";
        try (Connection conn = Conexion.getConexion()) {
            if (conn == null) {
                return lista;
            }

            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    lista.add(new Pago(
                        rs.getInt("id"),
                        rs.getString("mes"),
                        rs.getDouble("monto"),
                        rs.getString("estado"),
                        rs.getDate("fecha_pago").toLocalDate(),
                        rs.getInt("id_contrato")
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("Error al listar pagos: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Pago p) {
        String sql = "UPDATE pagos SET mes=?, monto=?, estado=?, fecha_pago=?, id_contrato=? WHERE id=?";
        try (Connection conn = Conexion.getConexion()) {
            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, p.getMes());
                ps.setDouble(2, p.getMonto());
                ps.setString(3, p.getEstado());
                ps.setDate(4, Date.valueOf(p.getFechaPago()));
                ps.setInt(5, p.getIdContrato());
                ps.setInt(6, p.getId());
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar pago: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM pagos WHERE id=?";
        try (Connection conn = Conexion.getConexion()) {
            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("Error al eliminar pago: " + e.getMessage());
            return false;
        }
    }
}
