package repository;

import java.sql.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ReportesDAO {

    // ── 1. Resumen general ──────────────────────────────────────────────────
    public Map<String, String> obtenerResumenGeneral() {
        Map<String, String> datos = new LinkedHashMap<>();
        String sql = """
            SELECT
                (SELECT COUNT(*) FROM contratos)                             AS total_contratos,
                (SELECT COUNT(*) FROM pagos)                                 AS total_pagos,
                (SELECT COUNT(*) FROM pagos WHERE estado = 'Pagado')        AS pagos_al_dia,
                (SELECT COUNT(*) FROM pagos WHERE estado = 'Pendiente')     AS pagos_pendientes,
                (SELECT COALESCE(SUM(monto), 0) FROM pagos WHERE estado = 'Pagado') AS total_recaudado,
                (SELECT COALESCE(SUM(monto), 0) FROM pagos WHERE estado = 'Pendiente') AS total_deuda,
                (SELECT COUNT(*) FROM propiedades WHERE disponible = 1)     AS propiedades_libres,
                (SELECT COUNT(*) FROM propiedades WHERE disponible = 0)     AS propiedades_ocupadas
            """;
        try (Connection conn = Conexion.getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                datos.put("Contratos activos",      rs.getString("total_contratos"));
                datos.put("Total de pagos",         rs.getString("total_pagos"));
                datos.put("Pagos al día",           rs.getString("pagos_al_dia"));
                datos.put("Pagos pendientes",       rs.getString("pagos_pendientes"));
                datos.put("Total recaudado ($)",    formatearMonto(rs.getDouble("total_recaudado")));
                datos.put("Deuda pendiente ($)",    formatearMonto(rs.getDouble("total_deuda")));
                datos.put("Propiedades libres",     rs.getString("propiedades_libres"));
                datos.put("Propiedades ocupadas",   rs.getString("propiedades_ocupadas"));
            }
        } catch (Exception e) {
            System.out.println("Error resumen: " + e.getMessage());
        }
        return datos;
    }

    // ── 2. Reporte: Pagos por contrato ──────────────────────────────────────
    // Devuelve filas listas para la tabla principal del reporte
    public List<Object[]> obtenerPagosPorContrato() {
        List<Object[]> filas = new ArrayList<>();
        String sql = """
            SELECT
                c.id                                     AS id_contrato,
                CONCAT(inq.nombre, ' ', inq.apellido)    AS inquilino,
                p.direccion                              AS propiedad,
                COUNT(pg.id)                             AS total_pagos,
                SUM(CASE WHEN pg.estado='Pagado'    THEN 1 ELSE 0 END) AS pagados,
                SUM(CASE WHEN pg.estado='Pendiente' THEN 1 ELSE 0 END) AS pendientes,
                COALESCE(SUM(pg.monto), 0)               AS total_monto,
                c.fecha_inicio,
                c.fecha_fin
            FROM contratos c
            JOIN inquilinos inq ON c.id_inquilino = inq.id
            JOIN propiedades p   ON c.id_propiedad  = p.id
            LEFT JOIN pagos pg   ON pg.id_contrato  = c.id
            GROUP BY c.id, inq.nombre, inq.apellido, p.direccion, c.fecha_inicio, c.fecha_fin
            ORDER BY c.id
            """;
        try (Connection conn = Conexion.getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                filas.add(new Object[]{
                    rs.getInt("id_contrato"),
                    rs.getString("inquilino"),
                    rs.getString("propiedad"),
                    rs.getInt("total_pagos"),
                    rs.getInt("pagados"),
                    rs.getInt("pendientes"),
                    formatearMonto(rs.getDouble("total_monto")),
                    rs.getDate("fecha_inicio"),
                    rs.getDate("fecha_fin")
                });
            }
        } catch (Exception e) {
            System.out.println("Error reporte pagos: " + e.getMessage());
        }
        return filas;
    }

    // ── 3. Detalle: pagos de UN contrato específico ─────────────────────────
    public List<Object[]> obtenerDetallesPagos(int idContrato) {
        List<Object[]> filas = new ArrayList<>();
        String sql = """
            SELECT id, mes, monto, estado, fecha_pago
            FROM pagos
            WHERE id_contrato = ?
            ORDER BY fecha_pago
            """;
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idContrato);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new Object[]{
                        rs.getInt("id"),
                        rs.getString("mes"),
                        formatearMonto(rs.getDouble("monto")),
                        rs.getString("estado"),
                        rs.getDate("fecha_pago")
                    });
                }
            }
        } catch (Exception e) {
            System.out.println("Error detalle pagos: " + e.getMessage());
        }
        return filas;
    }

   // ── 4. Exportar texto plano del reporte ─────────────────────────────────
public String generarTextoReporte(List<Object[]> filas) {
    StringBuilder sb = new StringBuilder();
    
    // Obtener la fecha y hora actual en Argentina
    ZonedDateTime ahora = ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
    DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy, HH:mm:ss", Locale.forLanguageTag("es-AR"));
    String fechaGeneracion = ahora.format(formateador);

    sb.append("═══════════════════════════════════════════════════════════════\n");
    sb.append("          REPORTE DE PAGOS POR CONTRATO — INMOBILIARIA\n");
    sb.append("  Generado: ").append(fechaGeneracion).append("\n");
    sb.append("═══════════════════════════════════════════════════════════════\n\n");

    String fmt = "%-6s %-22s %-26s %-8s %-8s %-10s %-14s\n";
    sb.append(String.format(fmt, "ID", "Inquilino", "Propiedad", "Pagados", "Pend.", "Total", "Monto"));
    sb.append("-".repeat(100)).append("\n");

    for (Object[] f : filas) {
        sb.append(String.format(fmt,
            f[0], truncar(f[1].toString(), 22),
            truncar(f[2].toString(), 26),
            f[4], f[5], f[3], f[6]));
    }
    sb.append("\n").append("═".repeat(100)).append("\n");
    sb.append("  Total de contratos listados: ").append(filas.size()).append("\n");
    return sb.toString();
}

    private String formatearMonto(double monto) {
        NumberFormat formato = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-AR"));
        formato.setMinimumFractionDigits(2);
        formato.setMaximumFractionDigits(2);
        return "$ " + formato.format(monto);
    }

    private String truncar(String s, int max) {
        return s.length() > max ? s.substring(0, max - 1) + "…" : s;
    }
}
