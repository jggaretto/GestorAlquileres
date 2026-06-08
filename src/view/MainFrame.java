package view;

import view.panels.LoginPanel;
import view.panels.MenuPanel;
import javax.swing.*;

import controller.LoginController;
import repository.UsuarioDAO;

import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel contenedorPrincipal;

    public MainFrame() {
        // Look & Feel global aca
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Gestión Inmobiliaria — Sistema Central");
        setSize(1150, 750); // El tamaño del menú (el más grande)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true); // El menú general suele ser redimensionable

        cardLayout = new CardLayout();
        contenedorPrincipal = new JPanel(cardLayout);

        // Instanciamos los paneles. El LoginPanel es el primero que se muestra, por eso lo instanciamos antes y le pasamos el controlador.
        LoginPanel loginPanel = new LoginPanel();
        UsuarioDAO dao = new UsuarioDAO();
        // Creamos el controlador, le pasamos la vista, el DAO y la acción de éxito
        new LoginController(loginPanel, dao, () -> cardLayout.show(contenedorPrincipal, "MENU"));
        contenedorPrincipal.add(loginPanel, "LOGIN");
        
        MenuPanel menuPanel = new MenuPanel();

        contenedorPrincipal.add(loginPanel, "LOGIN");
        contenedorPrincipal.add(menuPanel, "MENU");

        add(contenedorPrincipal);
    }
}