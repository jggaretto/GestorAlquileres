package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import controller.InquilinoController;

public class MenuFrame extends JFrame {
    private JPanel panelContenido; 
    private JLabel lblTituloSeccion;

    private JButton botonSeleccionado = null;

    private final Color COLOR_SIDEBAR = new Color(20, 25, 30);      
    private final Color COLOR_HOVER = new Color(35, 45, 55);        
    private final Color COLOR_ACCENTO = new Color(212, 175, 55);    
    private final Color COLOR_FONDO = new Color(245, 247, 250);     
    private final Color COLOR_TEXTO = new Color(220, 225, 230);     

    public MenuFrame() {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } 
        catch (Exception e) { e.printStackTrace(); }

        setTitle("Gestión Inmobiliaria");
        setSize(1150, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        // 1. SIDEBAR (Menú Lateral Izquierdo)
        JPanel sidebar = new JPanel();
        sidebar.setBackground(COLOR_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(260, 0)); 
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Contenedor del Logo para mejor control
        JPanel brandPanel = new JPanel();
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBackground(COLOR_SIDEBAR);
        brandPanel.setMaximumSize(new Dimension(260, 120));
        brandPanel.setBorder(new EmptyBorder(30, 0, 20, 0));

        JLabel lblLogo = new JLabel("⌂"); 
        lblLogo.setFont(new Font("SansSerif", Font.PLAIN, 45));
        lblLogo.setForeground(COLOR_ACCENTO);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblBrand = new JLabel("I N M O B I L I A R I A");
        lblBrand.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblBrand.setForeground(Color.WHITE);
        lblBrand.setAlignmentX(Component.CENTER_ALIGNMENT);

        brandPanel.add(lblLogo);
        brandPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Espacio entre logo y texto
        brandPanel.add(lblBrand);
        sidebar.add(brandPanel);

        // Separador envuelto en un panel para que no se estire raro
        JPanel separatorContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        separatorContainer.setBackground(COLOR_SIDEBAR);
        separatorContainer.setMaximumSize(new Dimension(260, 2));
        
        JSeparator separador = new JSeparator();
        separador.setForeground(new Color(60, 70, 80)); 
        separador.setBackground(COLOR_SIDEBAR); 
        separador.setPreferredSize(new Dimension(210, 1)); 
        separatorContainer.add(separador);
        
        sidebar.add(separatorContainer);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20))); 

        // Botones del sidebar con texto e ícono
        sidebar.add(crearBotonSidebar("Inicio", "home.png"));
        sidebar.add(crearBotonSidebar("Propietarios", "propietario.png"));
        sidebar.add(crearBotonSidebar("Inquilinos", "inquilino.png"));
        sidebar.add(crearBotonSidebar("Propiedades", "propiedades.png"));
        sidebar.add(crearBotonSidebar("Contratos", "contrato.png"));
        sidebar.add(crearBotonSidebar("Pagos", "pagos.png"));

        add(sidebar, BorderLayout.WEST);

       
  
  
        // 2. ÁREA CENTRAL 
  
        JPanel panelDerecho = new JPanel(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_SIDEBAR); 
        header.setPreferredSize(new Dimension(0, 75)); 
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, COLOR_ACCENTO)); 
        
        lblTituloSeccion = new JLabel("Inicio");
        lblTituloSeccion.setFont(new Font("SansSerif", Font.BOLD, 22)); 
        lblTituloSeccion.setForeground(Color.WHITE); 
        lblTituloSeccion.setBorder(new EmptyBorder(0, 30, 0, 0));
        header.add(lblTituloSeccion, BorderLayout.WEST);

        JLabel lblUsuario = new JLabel("●  Administrador   ");
        lblUsuario.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblUsuario.setForeground(COLOR_TEXTO); 
        lblUsuario.setBorder(new EmptyBorder(0, 0, 0, 30));
        header.add(lblUsuario, BorderLayout.EAST);

        panelDerecho.add(header, BorderLayout.NORTH);

        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(COLOR_FONDO);
        
        mostrarPantalla(crearPanelBienvenida(), "Inicio");

        panelDerecho.add(panelContenido, BorderLayout.CENTER);
        add(panelDerecho, BorderLayout.CENTER);
    }



    
    // MÉTODOS PARA BIENVENIDA
    
    private JPanel crearPanelBienvenida() {
        // Panel con imagen de fondo y texto superpuesto
        JPanel panelHero = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    java.net.URL imgUrl = getClass().getResource("/assets/edificio-menu.jpg");
                    if (imgUrl != null) {
                        Image img = new ImageIcon(imgUrl).getImage();
                        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                        g.setColor(new Color(0, 0, 0, 130)); 
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                } catch (Exception e) {
                    // Si falla la imagen, queda el color de fondo normal
                }
            }
        };
        
        // Configuración del panel de bienvenida
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.CENTER;

        //Saludo principal
        JLabel saludo = new JLabel("INMOBILIARIA");
        saludo.setFont(new Font("SansSerif", Font.BOLD, 48)); // Más grande e imponente
        saludo.setForeground(Color.WHITE); 
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        panelHero.add(saludo, gbc);

        //Slogan o bienvenida secundaria
        JLabel slogan = new JLabel("Bienvenido al Gestor de Alquileres");
        slogan.setFont(new Font("SansSerif", Font.PLAIN, 20));
        slogan.setForeground(new Color(200, 200, 200)); // Blanco grisáceo
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        panelHero.add(slogan, gbc);

        // Fecha actual dinámica
        String fechaActual = new java.text.SimpleDateFormat("EEEE dd 'de' MMMM 'de' yyyy").format(new java.util.Date());
        JLabel fecha = new JLabel(fechaActual);
        fecha.setFont(new Font("SansSerif", Font.PLAIN, 16));
        fecha.setForeground(COLOR_ACCENTO);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 40, 0);
        panelHero.add(fecha, gbc);

        // Línea decorativa dorada
        JPanel linea = new JPanel();
        linea.setPreferredSize(new Dimension(120, 3));
        linea.setBackground(COLOR_ACCENTO);
        gbc.gridy = 3;
        panelHero.add(linea, gbc);

        return panelHero;
    }
    


    // MÉTODOS DE RENDERIZADO Y NAVEGACIÓN
    
    private void mostrarPantalla(JPanel nuevoPanel, String titulo) {
        panelContenido.removeAll();
        panelContenido.add(nuevoPanel, BorderLayout.CENTER);
        
        String tituloLimpio = titulo.replaceAll("[^a-zA-ZáéíóúÁÉÍÓÚñÑ ]", "").trim();
        if(tituloLimpio.equals("Inicio")) {
            lblTituloSeccion.setText("Inicio");
        } else {
            lblTituloSeccion.setText("Gestión de " + tituloLimpio);
        }
        
        panelContenido.revalidate();
        panelContenido.repaint();
    }

   // pasamos el texto y el nombre del archivo de imagen
    private JButton crearBotonSidebar(String texto, String rutaIcono) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 15));
        btn.setForeground(COLOR_TEXTO);
        btn.setBackground(COLOR_SIDEBAR);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false); 
        btn.setOpaque(true);
        
        try {
            java.net.URL imgUrl = getClass().getResource("/assets/" + rutaIcono);
            if (imgUrl != null) {
                ImageIcon iconoOriginal = new ImageIcon(imgUrl);
                // Si la imagen es muy grande, se achicamos a 20x20 píxeles
                Image imgEscalada = iconoOriginal.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(imgEscalada));
                
                // Separación entre el icono y el texto
                btn.setIconTextGap(15); 
            }
        } catch (Exception e) {
            System.err.println("No se encontró el icono: " + rutaIcono);
        }

        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(260, 50)); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Como el icono ya ocupa espacio, se reduce el padding izquierdo a 25
        javax.swing.border.Border padding = new EmptyBorder(10, 25, 10, 15);
        
        javax.swing.border.Border bordeNormal = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, COLOR_SIDEBAR), 
            padding
        );
        
        javax.swing.border.Border bordeHover = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, COLOR_ACCENTO), 
            padding
        );

        javax.swing.border.Border bordeSeleccionado = BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 4, 0, 0, COLOR_ACCENTO),
        padding
        );

        btn.setBorder(bordeNormal);

            btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn != botonSeleccionado) {
                    btn.setBackground(COLOR_HOVER);
                    btn.setForeground(Color.WHITE);
                    btn.setBorder(bordeHover);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (btn != botonSeleccionado) {
                    btn.setBackground(COLOR_SIDEBAR);
                    btn.setForeground(COLOR_TEXTO);
                    btn.setBorder(bordeNormal);
                }
            }
        });
        
            btn.addActionListener(e -> {
            if (botonSeleccionado != null) {
                botonSeleccionado.setBackground(COLOR_SIDEBAR);
                botonSeleccionado.setForeground(COLOR_TEXTO);
                botonSeleccionado.setBorder(bordeNormal);
            }

            botonSeleccionado = btn;
            btn.setBackground(COLOR_HOVER);
            btn.setForeground(COLOR_ACCENTO);      
            btn.setBorder(bordeSeleccionado);

            if (texto.contains("Inicio")) {
                mostrarPantalla(crearPanelBienvenida(), "Inicio");
            } else if (texto.contains("Propietarios")) {
                mostrarPantalla(new PropietariosPanel(), "Propietarios");
            } else if (texto.contains("Inquilinos")) {
                InquilinosPanel panelInquilinos = new InquilinosPanel();
                new InquilinoController(panelInquilinos);
                mostrarPantalla(panelInquilinos, "Inquilinos");
            } else if (texto.contains("Propiedades")) {
                mostrarPantalla(new PropiedadesPanel(), "Propiedades");
            } else if (texto.contains("Contratos")) {
                mostrarPantalla(new ContratosPanel(), "Contratos");
            } else if (texto.contains("Pagos")) {
                mostrarPantalla(new PagosPanel(), "Pagos");
            } else {
                JPanel panelVacio = new JPanel();
                panelVacio.setBackground(COLOR_FONDO);
                mostrarPantalla(panelVacio, texto);
            }
        });
       
        return btn;
    }   
}