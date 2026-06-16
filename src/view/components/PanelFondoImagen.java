package view.components;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;

public class PanelFondoImagen extends JPanel {

    private static final String DEFAULT_IMAGE = "/assets/edificio-menu.jpg";

    private final Image image;
    private final Color overlay;

    public PanelFondoImagen(LayoutManager layout, Color overlay) {
        super(layout);
        this.overlay = overlay;
        java.net.URL url = getClass().getResource(DEFAULT_IMAGE);
        this.image = url != null ? new ImageIcon(url).getImage() : null;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        if (image != null) {
            g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            g2.setColor(overlay);
            g2.fillRect(0, 0, getWidth(), getHeight());
        } else {
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
