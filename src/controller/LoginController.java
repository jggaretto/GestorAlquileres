package controller;

import repository.UsuarioDAO;
import model.Usuario;
import view.panels.LoginPanel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginController {

    private LoginPanel vista;
    private UsuarioDAO dao;
    private Runnable onLoginSuccess;

    public LoginController(LoginPanel vista, UsuarioDAO dao, Runnable onLoginSuccess) {
        this.vista = vista;
        this.dao = dao;
        this.onLoginSuccess = onLoginSuccess;
        inicializarEventos();
    }

    private void inicializarEventos() {
        // Evento al hacer clic en el botón
        vista.getBtnIngresar().addActionListener(e -> intentarAcceso());

        // Eventos para presionar ENTER y limpiar errores al tipear
        KeyAdapter enterYLimpiarListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                vista.limpiarError(); // Limpia el borde rojo si el usuario empieza a escribir
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    intentarAcceso();
                }
            }
        };

        vista.getTxtUsuario().addKeyListener(enterYLimpiarListener);
        vista.getTxtContrasena().addKeyListener(enterYLimpiarListener);
    }

    private void intentarAcceso() {
        String usuario = vista.getUsuario();
        String contrasena = vista.getContrasena();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            vista.mostrarError("Completá ambos campos.");
            return;
        }

        // Consulta a la base de datos
        Usuario userLogueado = dao.validarLogin(usuario, contrasena);

        if (userLogueado != null) {
            vista.limpiarCampos();
            onLoginSuccess.run(); // Le avisa al MainFrame que cambie de pantalla
        } else {
            vista.mostrarError("Usuario o contraseña incorrectos.");
            vista.getTxtContrasena().setText("");
            vista.getTxtContrasena().requestFocus();
        }
    }
}