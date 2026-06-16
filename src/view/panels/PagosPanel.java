package view.panels;

import view.components.ScrollPaneModerno;
import view.components.CampoFormulario;
import view.components.BotonEstilizado;
import view.components.CampoTextoEstilizado;
import view.components.EstilizadorTabla;
import view.components.PanelFondoImagen;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class PagosPanel extends PanelFondoImagen {

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

    public PagosPanel() {

        super(new BorderLayout(0, 20), new Color(5, 10, 18, 215));

        setBackground(COLOR_FONDO);
        setOpaque(false);

        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(3, 0, 0, 0, COLOR_ACCENTO_ORO),
                        new EmptyBorder(25, 30, 25, 30)
                )
        );

        add(crearHeader(), BorderLayout.NORTH);
        add(crearContenedorCuerpo(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
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

        JScrollPane scroll = new ScrollPaneModerno(tabla);

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
        return new CampoFormulario(titulo, campo);
    }

    private JTextField crearInputElegante(int cols) {
        return new CampoTextoEstilizado(cols);
    }

    private JButton crearBoton(
            String texto,
            Color fondo,
            Color textoColor
    ) {
        return new BotonEstilizado(texto, fondo, textoColor);
    }

    private void estilizarTabla(JTable tabla) {
        EstilizadorTabla.apply(tabla);
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
