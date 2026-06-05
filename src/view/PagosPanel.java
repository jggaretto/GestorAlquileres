package view;

import controller.PagoController;
import model.Pago;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PagosPanel extends JPanel {

    // Tema Dark Luxury
    private final Color COLOR_FONDO = new Color(11, 18, 25);
    private final Color COLOR_CARD = new Color(20, 28, 38);

    private final Color COLOR_ACCENTO_ORO = new Color(212, 175, 55);

    private final Color COLOR_SIDEBAR = new Color(20, 25, 30);

    private final Color COLOR_BORDE = new Color(45, 55, 65);

    private final Color COLOR_TEXTO_SECUNDARIO = new Color(170, 180, 190);

    private JTextField txtMes;
    private JTextField txtMonto;
    private JTextField txtEstado;
    private JTextField txtFechaPago;
    private JTextField txtIdContrato;
    private JTextField txtBuscar;

    private JButton btnGuardar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnBuscar;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private PagoController controller = new PagoController();

    public PagosPanel() {

        setLayout(new BorderLayout(0, 20));

        setBackground(COLOR_FONDO);

        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(3, 0, 0, 0, COLOR_ACCENTO_ORO),
                        new EmptyBorder(25, 30, 25, 30)
                )
        );

        add(crearHeader(), BorderLayout.NORTH);
        add(crearContenedorCuerpo(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);

        inicializarEventos();
        cargarTabla();
    }

    private JPanel crearHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel panelTitulos = new JPanel();
        panelTitulos.setOpaque(false);
        panelTitulos.setLayout(new BoxLayout(panelTitulos, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Pagos");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 30));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo =
                new JLabel("Administración de pagos registrados");

        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSubtitulo.setForeground(COLOR_TEXTO_SECUNDARIO);

        panelTitulos.add(lblTitulo);
        panelTitulos.add(Box.createVerticalStrut(5));
        panelTitulos.add(lblSubtitulo);

        header.add(panelTitulos, BorderLayout.WEST);

        JPanel busqueda =
                new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        busqueda.setOpaque(false);

        txtBuscar = crearInputElegante(18);

        btnBuscar =
                crearBoton(
                        "BUSCAR",
                        COLOR_SIDEBAR,
                        Color.WHITE
                );

        busqueda.add(txtBuscar);
        busqueda.add(btnBuscar);

        header.add(busqueda, BorderLayout.EAST);

        return header;
    }

    private JPanel crearContenedorCuerpo() {

        JPanel cuerpo = new JPanel(new GridBagLayout());
        cuerpo.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();

        JPanel formulario =
                new JPanel(new GridLayout(0, 2, 25, 20));

        formulario.setBackground(COLOR_CARD);

        formulario.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BORDE),
                        new EmptyBorder(25, 25, 25, 25)
                )
        );

        txtMes = crearInputElegante(1);
        txtMonto = crearInputElegante(1);
        txtEstado = crearInputElegante(1);
        txtFechaPago = crearInputElegante(1);
        txtIdContrato = crearInputElegante(1);

        formulario.add(
                crearCampo(
                        "MES",
                        txtMes
                )
        );

        formulario.add(
                crearCampo(
                        "MONTO",
                        txtMonto
                )
        );

        formulario.add(
                crearCampo(
                        "ESTADO",
                        txtEstado
                )
        );

        formulario.add(
                crearCampo(
                        "FECHA DE PAGO (AAAA-MM-DD)",
                        txtFechaPago
                )
        );

        formulario.add(
                crearCampo(
                        "ID CONTRATO",
                        txtIdContrato
                )
        );


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 0);

        cuerpo.add(formulario, gbc);

        modeloTabla = new DefaultTableModel(
                new String[]{
                        "ID",
                        "Mes",
                        "Monto",
                        "Estado",
                        "Fecha Pago",
                        "ID Contrato"
                },
                0
        );

        tabla = new JTable(modeloTabla);

        estilizarTabla(tabla);

        JScrollPane scroll = new JScrollPane(tabla);

        scroll.setBorder(
                BorderFactory.createLineBorder(COLOR_BORDE)
        );

        scroll.getViewport().setBackground(COLOR_CARD);

        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);

        cuerpo.add(scroll, gbc);

        return cuerpo;
    }

    private JPanel crearPanelBotones() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        panel.setOpaque(false);

        btnLimpiar =
                crearBoton(
                        "LIMPIAR",
                        new Color(90, 100, 115),
                        Color.WHITE
                );

        btnEliminar =
                crearBoton(
                        "ELIMINAR",
                        new Color(153, 27, 27),
                        Color.WHITE
                );

        btnModificar =
                crearBoton(
                        "MODIFICAR",
                        COLOR_SIDEBAR,
                        Color.WHITE
                );

        btnGuardar =
                crearBoton(
                        "GUARDAR",
                        COLOR_ACCENTO_ORO,
                        Color.BLACK
                );

        panel.add(btnLimpiar);
        panel.add(btnEliminar);
        panel.add(btnModificar);
        panel.add(btnGuardar);

        return panel;
    }

    private JPanel crearCampo(
            String titulo,
            JComponent campo
    ) {

        JPanel panel = new JPanel(new BorderLayout(0, 8));

        panel.setOpaque(false);

        JLabel lbl = new JLabel(titulo);

        lbl.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        lbl.setForeground(COLOR_ACCENTO_ORO);

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(campo, BorderLayout.CENTER);

        return panel;
    }

    private JTextField crearInputElegante(int cols) {

        JTextField campo = new JTextField(cols);

        campo.setBackground(
                new Color(30, 40, 52)
        );

        campo.setForeground(Color.WHITE);

        campo.setCaretColor(COLOR_ACCENTO_ORO);

        campo.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        campo.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                COLOR_BORDE
                        ),
                        new EmptyBorder(
                                12,
                                18,
                                12,
                                18
                        )
                )
        );

        return campo;
    }

    private JButton crearBoton(
            String texto,
            Color fondo,
            Color textoColor
    ) {

        JButton btn = new JButton(texto);

        btn.setBackground(fondo);
        btn.setForeground(textoColor);

        btn.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        btn.setFocusPainted(false);

        btn.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        btn.setBorder(
                new EmptyBorder(
                        12,
                        25,
                        12,
                        25
                )
        );

        return btn;
    }

    private void estilizarTabla(JTable tabla) {

        tabla.setRowHeight(38);

        tabla.setBackground(COLOR_CARD);

        tabla.setForeground(Color.WHITE);

        tabla.setGridColor(COLOR_BORDE);

        tabla.setShowVerticalLines(false);

        tabla.setSelectionBackground(new Color(212,175,55,70));

        tabla.setSelectionForeground(Color.WHITE);

        JTableHeader header =
                tabla.getTableHeader();

        header.setBackground(new Color(15, 20, 25));

        header.setForeground(COLOR_ACCENTO_ORO);

        header.setFont(new Font("SansSerif",Font.BOLD,13));

        header.setPreferredSize(new Dimension(0, 42));
    }

    private void inicializarEventos() {
        btnGuardar.addActionListener(e -> guardarPago());
        btnModificar.addActionListener(e -> modificarPago());
        btnEliminar.addActionListener(e -> eliminarPago());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> cargarTabla(txtBuscar.getText().trim()));

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarFormularioDesdeTabla();
            }
        });
    }

    private void guardarPago() {
        try {
            Pago pago = leerPagoDelFormulario(0);
            if (controller.agregar(pago)) {
                JOptionPane.showMessageDialog(this, "Pago guardado correctamente");
                cargarTabla();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar el pago");
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void modificarPago() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un pago de la tabla");
            return;
        }

        try {
            int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
            Pago pago = leerPagoDelFormulario(id);
            if (controller.actualizar(pago)) {
                JOptionPane.showMessageDialog(this, "Pago modificado correctamente");
                cargarTabla();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo modificar el pago");
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void eliminarPago() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un pago de la tabla");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar el pago seleccionado?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
            if (controller.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Pago eliminado correctamente");
                cargarTabla();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el pago");
            }
        }
    }

    private Pago leerPagoDelFormulario(int id) {
        if (txtMes.getText().trim().isEmpty()
                || txtMonto.getText().trim().isEmpty()
                || txtEstado.getText().trim().isEmpty()
                || txtFechaPago.getText().trim().isEmpty()
                || txtIdContrato.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Completa todos los campos");
        }

        try {
            return new Pago(
                    id,
                    txtMes.getText().trim(),
                    Double.parseDouble(txtMonto.getText().trim()),
                    txtEstado.getText().trim(),
                    LocalDate.parse(txtFechaPago.getText().trim()),
                    Integer.parseInt(txtIdContrato.getText().trim())
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Monto e ID Contrato deben ser numericos");
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("La fecha de pago debe tener formato AAAA-MM-DD");
        }
    }

    private void cargarTabla() {
        cargarTabla("");
    }

    private void cargarTabla(String filtro) {
        modeloTabla.setRowCount(0);
        List<Pago> pagos = controller.listar();
        String textoFiltro = filtro.toLowerCase();

        for (Pago p : pagos) {
            Object[] fila = {
                    p.getId(),
                    p.getMes(),
                    p.getMonto(),
                    p.getEstado(),
                    p.getFechaPago(),
                    p.getIdContrato()
            };

            if (textoFiltro.isEmpty() || coincideFiltro(fila, textoFiltro)) {
                modeloTabla.addRow(fila);
            }
        }
    }

    private boolean coincideFiltro(Object[] fila, String filtro) {
        for (Object valor : fila) {
            if (String.valueOf(valor).toLowerCase().contains(filtro)) {
                return true;
            }
        }
        return false;
    }

    private void cargarFormularioDesdeTabla() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            return;
        }

        txtMes.setText(modeloTabla.getValueAt(fila, 1).toString());
        txtMonto.setText(modeloTabla.getValueAt(fila, 2).toString());
        txtEstado.setText(modeloTabla.getValueAt(fila, 3).toString());
        txtFechaPago.setText(modeloTabla.getValueAt(fila, 4).toString());
        txtIdContrato.setText(modeloTabla.getValueAt(fila, 5).toString());
    }

    private void limpiarFormulario() {
        txtMes.setText("");
        txtMonto.setText("");
        txtEstado.setText("");
        txtFechaPago.setText("");
        txtIdContrato.setText("");
        txtBuscar.setText("");
        tabla.clearSelection();
    }
    // GETTERS

    public JTextField getTxtMes() {
        return txtMes;
    }

    public JTextField getTxtMonto() {
        return txtMonto;
    }

    public JTextField getTxtEstado() {
        return txtEstado;
    }

    public JTextField getTxtFechaPago() {
        return txtFechaPago;
    }

    public JTextField getTxtIdContrato() {
        return txtIdContrato;
    }

    public JTextField getTxtBuscar() {
        return txtBuscar;
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnModificar() {
        return btnModificar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JTable getTabla() {
        return tabla;
    }
}
