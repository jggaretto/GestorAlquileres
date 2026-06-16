package view.panels;

import view.dialogs.ReporteDetalleDialog;
import view.components.ModernScrollPane;
import repository.ReportesDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Panel de Reportes — estilo Dark Luxury idéntico al resto de la app.
 *
 * Secciones:
 *  • KPIs / tarjetas de resumen en la parte superior
 *  • Tabla: "Reporte de Pagos por Contrato"
 *  • Doble clic en una fila → ReporteDetalleDialog (detalle de pagos)
 *  • Botón "EXPORTAR" → guarda un .txt en el escritorio
 */
public class ReportesPanel extends JPanel {

    // ── Paleta ───────────────────────────────────────────────────────────────
    private final Color COLOR_FONDO     = new Color(11, 18, 25);
    private final Color COLOR_CARD      = new Color(20, 28, 38);
    private final Color COLOR_ACCENTO   = new Color(212, 175, 55);
    private final Color COLOR_SIDEBAR   = new Color(20, 25, 30);
    private final Color COLOR_BORDE     = new Color(45, 55, 65);
    private final Color COLOR_TEXTO_SEC = new Color(170, 180, 190);

    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private List<Object[]> filasActuales;

    private final ReportesDAO dao = new ReportesDAO();

    public ReportesPanel() {
        setLayout(new BorderLayout(0, 20));
        setBackground(COLOR_FONDO);
        setOpaque(false);
        _cargarFondo();
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(3, 0, 0, 0, COLOR_ACCENTO),
            new EmptyBorder(25, 30, 25, 30)
        ));

        add(crearHeader(),  BorderLayout.NORTH);
        add(crearCuerpo(),  BorderLayout.CENTER);
        add(crearFooter(),  BorderLayout.SOUTH);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HEADER: título + KPIs
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel crearHeader() {
    JPanel header = new JPanel(new BorderLayout(0, 18));
    header.setOpaque(false);

    // Títulos
    JPanel panelTitulos = new JPanel();
    panelTitulos.setOpaque(false);
    panelTitulos.setLayout(new BoxLayout(panelTitulos, BoxLayout.Y_AXIS));

    JLabel lblTitulo = new JLabel("Reportes");
    lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 30));
    lblTitulo.setForeground(Color.WHITE);

    JLabel lblSub = new JLabel("Resumen financiero y detalle de contratos");
    lblSub.setFont(new Font("SansSerif", Font.PLAIN, 14));
    lblSub.setForeground(COLOR_TEXTO_SEC);

    panelTitulos.add(lblTitulo);
    panelTitulos.add(Box.createVerticalStrut(5));
    panelTitulos.add(lblSub);
    header.add(panelTitulos, BorderLayout.NORTH);

    // KPIs en 2 filas de 4
    Map<String, String> resumen = dao.obtenerResumenGeneral();
    JPanel kpiWrapper = new JPanel(new GridLayout(2, 4, 10, 10));
    kpiWrapper.setOpaque(false);

    for (Map.Entry<String, String> e : resumen.entrySet()) {
        kpiWrapper.add(crearKpiCard(e.getKey(), e.getValue(), elegirColorKpi(e.getKey())));
    }
    header.add(kpiWrapper, BorderLayout.CENTER);

    return header;
}

    private Color elegirColorKpi(String clave) {
        if (clave.contains("recaudado"))  return new Color(34, 197, 94);
        if (clave.contains("pendient") || clave.contains("Deuda")) return new Color(239, 68, 68);
        if (clave.contains("libres"))     return new Color(96, 165, 250);
        if (clave.contains("ocupadas"))   return new Color(251, 146, 60);
        return COLOR_ACCENTO;
    }

  private JPanel crearKpiCard(String label, String valor, Color colorValor) {
    JPanel card = new JPanel(new BorderLayout(0, 6));
    card.setBackground(COLOR_CARD);
    card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(COLOR_BORDE),
        new EmptyBorder(12, 14, 12, 14)
    ));
    // Tooltip para ver el texto completo al pasar el mouse
    card.setToolTipText(label + ": " + valor);

    JLabel lblValor = new JLabel(valor);
    lblValor.setFont(new Font("SansSerif", Font.BOLD, 20));
    lblValor.setForeground(colorValor);

    // Texto completo, sin cortar
    JLabel lblLabel = new JLabel("<html><body style='width:120px'>"
        + label.toUpperCase() + "</body></html>");
    lblLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
    lblLabel.setForeground(COLOR_TEXTO_SEC);

    card.add(lblValor, BorderLayout.NORTH);
    card.add(lblLabel, BorderLayout.CENTER);
    return card;
}

    // ─────────────────────────────────────────────────────────────────────────
    // CUERPO: tabla de reporte
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel crearCuerpo() {
        JPanel cuerpo = new JPanel(new BorderLayout(0, 10));
        cuerpo.setOpaque(false);

        // Sub-header de la sección tabla
        JPanel subHeader = new JPanel(new BorderLayout());
        subHeader.setOpaque(false);

        JLabel lblSeccion = new JLabel("Pagos por Contrato");
        lblSeccion.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblSeccion.setForeground(COLOR_ACCENTO);
        subHeader.add(lblSeccion, BorderLayout.WEST);

        JLabel lblHint = new JLabel("Doble clic en una fila para ver el detalle");
        lblHint.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblHint.setForeground(COLOR_TEXTO_SEC);
        subHeader.add(lblHint, BorderLayout.EAST);

        cuerpo.add(subHeader, BorderLayout.NORTH);

        // Tabla
        modeloTabla = new DefaultTableModel(
            new String[]{"ID", "Inquilino", "Propiedad",
                         "Total Pagos", "Pagados", "Pendientes", "Monto Total",
                         "Fecha Inicio", "Fecha Fin"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(38);
        tabla.setBackground(COLOR_CARD);
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(COLOR_BORDE);
        tabla.setShowVerticalLines(false);
        tabla.setSelectionBackground(new Color(212, 175, 55, 70));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));

        // Renderer para columna "Pendientes"
        tabla.getColumnModel().getColumn(5).setCellRenderer(
            new PendientesRenderer()
        );

        JTableHeader th = tabla.getTableHeader();
        th.setBackground(new Color(15, 20, 25));
        th.setForeground(COLOR_ACCENTO);
        th.setFont(new Font("SansSerif", Font.BOLD, 13));
        th.setPreferredSize(new Dimension(0, 42));

        // Doble clic → detalle
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    abrirDetalle();
                }
            }
        });

        JScrollPane scroll = new ModernScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDE));
        scroll.getViewport().setBackground(COLOR_CARD);
        cuerpo.add(scroll, BorderLayout.CENTER);

        cargarTabla();
        return cuerpo;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // FOOTER: botones
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel crearFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footer.setOpaque(false);

        JButton btnActualizar = crearBoton("ACTUALIZAR", COLOR_SIDEBAR, Color.WHITE);
        JButton btnDetalle    = crearBoton("VER DETALLE", new Color(45, 55, 70), Color.WHITE);
        JButton btnExportar   = crearBoton("EXPORTAR .TXT", COLOR_ACCENTO, Color.BLACK);

        btnActualizar.addActionListener(e -> cargarTabla());

        btnDetalle.addActionListener(e -> abrirDetalle());

        btnExportar.addActionListener(e -> exportarReporte());

        footer.add(btnActualizar);
        footer.add(btnDetalle);
        footer.add(btnExportar);
        return footer;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LÓGICA
    // ─────────────────────────────────────────────────────────────────────────
    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        filasActuales = dao.obtenerPagosPorContrato();
        for (Object[] f : filasActuales) {
            modeloTabla.addRow(f);
        }
    }

    private void abrirDetalle() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleccioná un contrato de la tabla primero.",
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idContrato    = (int) modeloTabla.getValueAt(fila, 0);
        String inquilino  = modeloTabla.getValueAt(fila, 1).toString();
        String propiedad  = modeloTabla.getValueAt(fila, 2).toString();

        Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);
        ReporteDetalleDialog dialog =
            new ReporteDetalleDialog(owner, idContrato, inquilino, propiedad);
        dialog.setVisible(true);
    }

    private void exportarReporte() {
    if (filasActuales == null || filasActuales.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay datos para exportar.");
        return;
    }

    String nombreSugerido = "reporte_inmobiliaria_"
        + new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date())
        + ".txt";

    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Guardar reporte");
    chooser.setSelectedFile(new java.io.File(nombreSugerido));
    chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
        "Archivos de texto (*.txt)", "txt"));

    int resultado = chooser.showSaveDialog(this);
    if (resultado != JFileChooser.APPROVE_OPTION) return;

    java.io.File archivo = chooser.getSelectedFile();
    // Agregar extensión si el usuario la borró
    if (!archivo.getName().endsWith(".txt")) {
        archivo = new java.io.File(archivo.getAbsolutePath() + ".txt");
    }

    try (java.io.FileWriter fw = new java.io.FileWriter(archivo)) {
        fw.write(dao.generarTextoReporte(filasActuales));
        JOptionPane.showMessageDialog(this,
            "Reporte guardado en:\n" + archivo.getAbsolutePath(),
            "Exportacion exitosa", JOptionPane.INFORMATION_MESSAGE);
    } catch (java.io.IOException ex) {
        JOptionPane.showMessageDialog(this,
            "No se pudo guardar el archivo:\n" + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    // ─────────────────────────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────────────────────────
    private JButton crearBoton(String texto, Color fondo, Color textoColor) {
        JButton btn = new JButton(texto);
        btn.setBackground(fondo);
        btn.setForeground(textoColor);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(12, 25, 12, 25));
        return btn;
    }

    // Renderer: pinta en rojo los pendientes > 0
    private static class PendientesRenderer extends javax.swing.table.DefaultTableCellRenderer {
        private final Color COLOR_CARD = new Color(20, 28, 38);

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v,
                boolean sel, boolean foc, int row, int col) {
            super.getTableCellRendererComponent(t, v, sel, foc, row, col);
            int val = v instanceof Integer ? (int) v : 0;
            setForeground(val > 0 ? new Color(239, 68, 68) : new Color(34, 197, 94));
            setBackground(sel ? new Color(212, 175, 55, 60) : COLOR_CARD);
            setHorizontalAlignment(CENTER);
            setFont(new Font("SansSerif", Font.BOLD, 13));
            setOpaque(true);
            return this;
        }
    }

    // ── Fondo: edificio-menu.jpg con overlay oscuro ───────────────────────────
    private java.awt.Image _imagenFondo;
    private void _cargarFondo() {
        java.net.URL url = getClass().getResource("/assets/edificio-menu.jpg");
        if (url != null) _imagenFondo = new javax.swing.ImageIcon(url).getImage();
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
        if (_imagenFondo != null) {
            g2.drawImage(_imagenFondo, 0, 0, getWidth(), getHeight(), this);
            g2.setColor(new java.awt.Color(5, 10, 18, 215));
            g2.fillRect(0, 0, getWidth(), getHeight());
        } else {
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
        g2.dispose();
        super.paintComponent(g);
    }

}