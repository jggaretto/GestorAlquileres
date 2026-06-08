package view.dialogs;

import repository.ReportesDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * Ventana modal que muestra el detalle de pagos de un contrato específico.
 * Se abre al hacer doble clic (o al pulsar "Ver Detalle") en ReportesPanel.
 */
public class ReporteDetalleDialog extends JDialog {

    private final Color COLOR_FONDO     = new Color(11, 18, 25);
    private final Color COLOR_CARD      = new Color(20, 28, 38);
    private final Color COLOR_ACCENTO   = new Color(212, 175, 55);
    private final Color COLOR_BORDE     = new Color(45, 55, 65);
    private final Color COLOR_TEXTO_SEC = new Color(170, 180, 190);
    private final Color COLOR_SIDEBAR   = new Color(20, 25, 30);

    private final ReportesDAO dao = new ReportesDAO();

    public ReporteDetalleDialog(Frame owner, int idContrato,
                                String inquilino, String propiedad) {
        super(owner, "Detalle del Contrato #" + idContrato, true);
        setSize(700, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO);

        // ── Header ──────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_SIDEBAR);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 0, COLOR_ACCENTO),
            new EmptyBorder(18, 25, 18, 25)
        ));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Contrato #" + idContrato);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel(inquilino + "  ·  " + propiedad);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSub.setForeground(COLOR_TEXTO_SEC);

        textos.add(lblTitulo);
        textos.add(Box.createVerticalStrut(4));
        textos.add(lblSub);
        header.add(textos, BorderLayout.WEST);

        // Badge: total pagado
        List<Object[]> filas = dao.obtenerDetallesPagos(idContrato);
        double totalPagado = calcularTotal(filas, "Pagado");
        double totalPend   = calcularTotal(filas, "Pendiente");

        JPanel badges = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        badges.setOpaque(false);
        badges.add(crearBadge("Pagado", String.format("$ %.2f", totalPagado), new Color(34, 197, 94)));
        badges.add(crearBadge("Pendiente", String.format("$ %.2f", totalPend), new Color(239, 68, 68)));
        header.add(badges, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ── Tabla ────────────────────────────────────────────────────────────
        DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID Pago", "Mes", "Monto", "Estado", "Fecha Pago"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Object[] f : filas) {
            modelo.addRow(f);
        }

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(36);
        tabla.setBackground(COLOR_CARD);
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(COLOR_BORDE);
        tabla.setShowVerticalLines(false);
        tabla.setSelectionBackground(new Color(212, 175, 55, 60));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));

        // Colorear la columna Estado
        tabla.getColumnModel().getColumn(3).setCellRenderer(
            new EstadoRenderer()
        );

        JTableHeader th = tabla.getTableHeader();
        th.setBackground(new Color(15, 20, 25));
        th.setForeground(COLOR_ACCENTO);
        th.setFont(new Font("SansSerif", Font.BOLD, 13));
        th.setPreferredSize(new Dimension(0, 40));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(20, 20, 10, 20),
            BorderFactory.createLineBorder(COLOR_BORDE)
        ));
        scroll.getViewport().setBackground(COLOR_CARD);
        scroll.setBackground(COLOR_FONDO);
        add(scroll, BorderLayout.CENTER);

        // ── Footer: botón cerrar ─────────────────────────────────────────────
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 12));
        footer.setBackground(COLOR_FONDO);

        JButton btnCerrar = new JButton("CERRAR");
        btnCerrar.setBackground(COLOR_SIDEBAR);
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.setBorder(new EmptyBorder(10, 22, 10, 22));
        btnCerrar.addActionListener(e -> dispose());
        footer.add(btnCerrar);
        add(footer, BorderLayout.SOUTH);
    }

    private double calcularTotal(List<Object[]> filas, String estado) {
    double total = 0;
    for (Object[] f : filas) {
        if (estado.equals(f[3])) {
            // f[2] tiene formato "$ 310000,00" — limpiar símbolo y separadores
            String raw = f[2].toString()
                .replace("$", "")
                .replace(".", "")   // separador de miles
                .replace(",", ".")  // coma decimal → punto
                .trim();
            try { total += Double.parseDouble(raw); } catch (Exception ignored) {}
        }
    }
    return total;
}

    private JPanel crearBadge(String label, String valor, Color color) {
        JPanel p = new JPanel(new BorderLayout(4, 2));
        p.setOpaque(false);

        JLabel lbl = new JLabel(label.toUpperCase());
        lbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        lbl.setForeground(COLOR_TEXTO_SEC);

        JLabel val = new JLabel(valor);
        val.setFont(new Font("SansSerif", Font.BOLD, 15));
        val.setForeground(color);

        p.add(lbl, BorderLayout.NORTH);
        p.add(val, BorderLayout.CENTER);
        return p;
    }

    // Renderer para colorear la celda de Estado
    private static class EstadoRenderer extends javax.swing.table.DefaultTableCellRenderer {
        private final Color COLOR_CARD = new Color(20, 28, 38);

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v,
                boolean sel, boolean foc, int row, int col) {
            super.getTableCellRendererComponent(t, v, sel, foc, row, col);
            setForeground(
                "Pagado".equals(v)    ? new Color(34, 197, 94) :
                "Pendiente".equals(v) ? new Color(239, 68, 68) :
                Color.WHITE
            );
            setBackground(sel ? new Color(212, 175, 55, 60) : COLOR_CARD);
            setFont(new Font("SansSerif", Font.BOLD, 13));
            setHorizontalAlignment(CENTER);
            setBorder(new EmptyBorder(0, 8, 0, 8));
            setOpaque(true);
            return this;
        }
    }
}