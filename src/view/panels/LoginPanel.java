package view.panels;

import view.components.BotonEstilizado;
import view.components.CampoPasswordEstilizado;
import view.components.CampoTextoEstilizado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginPanel extends JPanel {

    private final Color COLOR_FONDO   = new Color(11, 18, 25);
    private final Color COLOR_CARD    = new Color(20, 28, 38);
    private final Color COLOR_ACCENTO = new Color(212, 175, 55);
    private final Color COLOR_BORDE   = new Color(45, 55, 65);
    private final Color COLOR_TEXTO_SEC = new Color(170, 180, 190);

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;
    private JLabel lblError;

    public LoginPanel() {
        setLayout(new GridBagLayout());
        setBackground(COLOR_FONDO);

        JPanel contenedorLogin = new JPanel(new BorderLayout());
        contenedorLogin.setBackground(COLOR_FONDO);
        contenedorLogin.setPreferredSize(new Dimension(480, 550));
        contenedorLogin.setMinimumSize(new Dimension(480, 550));

        contenedorLogin.add(crearPanelLogo(),        BorderLayout.NORTH);
        contenedorLogin.add(crearPanelFormulario(), BorderLayout.CENTER);
        contenedorLogin.add(crearFooter(),          BorderLayout.SOUTH);

        add(contenedorLogin);
    }

    private JPanel crearPanelLogo() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(30, 0, 30, 0));

        JLabel lblIcono = new JLabel("⌂");
        lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 56));
        lblIcono.setForeground(COLOR_ACCENTO);
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNombre = new JLabel("I N M O B I L I A R I A");
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSlogan = new JLabel("Gestor de Alquileres");
        lblSlogan.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSlogan.setForeground(COLOR_TEXTO_SEC);
        lblSlogan.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel linea = new JPanel();
        linea.setBackground(COLOR_ACCENTO);
        linea.setPreferredSize(new Dimension(60, 2));
        linea.setMaximumSize(new Dimension(60, 2));
        linea.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblIcono);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblNombre);
        panel.add(Box.createVerticalStrut(6));
        panel.add(lblSlogan);
        panel.add(Box.createVerticalStrut(16));
        panel.add(linea);

        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE),
            new EmptyBorder(15, 30, 15, 30)
        ));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(COLOR_FONDO);
        wrapper.setBorder(new EmptyBorder(0, 40, 0, 40));
        wrapper.add(card, BorderLayout.CENTER);

        card.add(crearCampoCompleto("USUARIO", false));
        card.add(Box.createVerticalStrut(20));

        card.add(crearCampoCompleto("CONTRASEÑA", true));
        card.add(Box.createVerticalStrut(10));

        lblError = new JLabel(" ");
        lblError.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblError.setForeground(new Color(239, 68, 68));
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblError.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        card.add(lblError);

        card.add(Box.createVerticalStrut(20));

        btnIngresar = new BotonEstilizado("INGRESAR", COLOR_ACCENTO, Color.BLACK);
        btnIngresar.setBorder(new EmptyBorder(13, 0, 13, 0));
        btnIngresar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btnIngresar.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(btnIngresar);

        return wrapper;
    }

    private JPanel crearCampoCompleto(String labelTexto, boolean esPassword) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel lbl = new JLabel(labelTexto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(COLOR_ACCENTO);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField campo = esPassword ? new CampoPasswordEstilizado() : new CampoTextoEstilizado(1);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        campo.setPreferredSize(new Dimension(0, 48));

        if (esPassword) {
            txtContrasena = (JPasswordField) campo;
        } else {
            txtUsuario = campo;
        }

        panel.add(lbl);
        panel.add(Box.createVerticalStrut(8));
        panel.add(campo);

        return panel;
    }

    private JPanel crearFooter() {
        JPanel footer = new JPanel();
        footer.setBackground(COLOR_FONDO);
        footer.setBorder(new EmptyBorder(16, 0, 20, 0));

        JLabel lbl = new JLabel("Sistema de Gestión Inmobiliaria — UNViMe");
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lbl.setForeground(new Color(80, 90, 100));
        footer.add(lbl);

        return footer;
    }

    // ── Métodos expuestos para el Controlador ────────────────────────────────
    public String getUsuario() { return txtUsuario.getText().trim(); }
    public String getContrasena() { return new String(txtContrasena.getPassword()).trim(); }
    public JButton getBtnIngresar() { return btnIngresar; }
    public JTextField getTxtUsuario() { return txtUsuario; }
    public JPasswordField getTxtContrasena() { return txtContrasena; }

    public void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(239, 68, 68)),
            new EmptyBorder(10, 15, 10, 15)
        ));
    }

    public void limpiarError() {
        lblError.setText(" ");
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE),
            new EmptyBorder(10, 15, 10, 15)
        ));
    }
    
    public void limpiarCampos() {
        txtUsuario.setText("");
        txtContrasena.setText("");
        limpiarError();
    }
}
