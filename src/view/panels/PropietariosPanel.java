package view.panels;

import view.components.ModernScrollPane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class PropietariosPanel extends JPanel {

    // Tema Dark Luxury
    private final Color COLOR_FONDO = new Color(11, 18, 25);
    private final Color COLOR_CARD = new Color(20, 28, 38);

    private final Color COLOR_ACCENTO_ORO = new Color(212, 175, 55);

    private final Color COLOR_SIDEBAR = new Color(20, 25, 30);

    private final Color COLOR_BORDE = new Color(45, 55, 65);


    private final Color COLOR_TEXTO_SECUNDARIO = new Color(170, 180, 190);

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtDni;
    private JTextField txtTelefono;
    private JTextField txtEmail;
    private JTextField txtBuscar;

    private JButton btnGuardar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnBuscar;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public PropietariosPanel() {

        setLayout(new BorderLayout(0, 20));

        setBackground(COLOR_FONDO);
        setOpaque(false);
        _cargarFondo();

        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(3,0,0,0,COLOR_ACCENTO_ORO),
                        new EmptyBorder(25,30,25,30)
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

        JLabel lblTitulo = new JLabel("Propietarios");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 30));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Administración de propietarios registrados");

        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSubtitulo.setForeground(COLOR_TEXTO_SECUNDARIO);

        panelTitulos.add(lblTitulo);
        panelTitulos.add(Box.createVerticalStrut(5));
        panelTitulos.add(lblSubtitulo);

        header.add(panelTitulos, BorderLayout.WEST);

        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        busqueda.setOpaque(false);

        txtBuscar = crearInputElegante(18);

        btnBuscar = crearBoton("BUSCAR", COLOR_SIDEBAR, Color.WHITE);

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
                new JPanel(new GridLayout(2, 2, 25, 20));

        formulario.setBackground(COLOR_CARD);

        formulario.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(COLOR_BORDE),
                new EmptyBorder(25,25,25,25)
                ));

        txtNombre = crearInputElegante(1);
        txtApellido = crearInputElegante(1);
        txtDni = crearInputElegante(1);
        txtTelefono = crearInputElegante(1);
        txtEmail = crearInputElegante(1);

        formulario.add(crearCampo("NOMBRE DEL PROPIETARIO",txtNombre));

        formulario.add(
                crearCampo(
                        "APELLIDO",
                        txtApellido
                )
        );

        formulario.add (
                crearCampo(
                        "DNI",
                        txtDni
                )
        );

        formulario.add(
                crearCampo(
                        "TELÉFONO DE CONTACTO",
                        txtTelefono
                )
        );

        formulario.add(
                crearCampo(
                        "CORREO ELECTRÓNICO",
                        txtEmail
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
                        "Nombre",
                        "Apellido",
                        "DNI",
                        "Teléfono",
                        "Email"
                },
                0
        );

        tabla = new JTable(modeloTabla);

        estilizarTabla(tabla);

        JScrollPane scroll = new ModernScrollPane(tabla);

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

        JPanel panel = new JPanel( new FlowLayout( FlowLayout.RIGHT, 10, 0));

        panel.setOpaque(false);

        btnLimpiar = crearBoton("LIMPIAR", new Color(90, 100, 115),Color.WHITE);

        btnEliminar = crearBoton("ELIMINAR", new Color(153, 27, 27),Color.WHITE);

        btnModificar = crearBoton("MODIFICAR", COLOR_SIDEBAR, Color.WHITE);

        btnGuardar = crearBoton("GUARDAR", COLOR_ACCENTO_ORO, Color.BLACK);

        panel.add(btnLimpiar);
        panel.add(btnEliminar);
        panel.add(btnModificar);
        panel.add(btnGuardar);

        return panel;
    }

    private JPanel crearCampo(
            String titulo,
            JTextField campo
    ) {

        JPanel panel = new JPanel(new BorderLayout(0, 8));

        panel.setOpaque(false);

        JLabel lbl = new JLabel(titulo);

        lbl.setFont(
                new Font("SansSerif",Font.BOLD,12)
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

        btn.setBorder(new EmptyBorder(
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

    // GETTERS

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JTextField getTxtApellido() {
        return txtApellido;
    }

    public JTextField getTxtDni() {
        return txtDni;
    }

    public JTextField getTxtTelefono() {
        return txtTelefono;
    }

    public JTextField getTxtEmail() {
        return txtEmail;
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
