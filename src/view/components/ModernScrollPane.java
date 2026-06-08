package view.components;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

public class ModernScrollPane extends JScrollPane {

    private static final Color COLOR_CARD = new Color(20, 28, 38);
    private static final Color COLOR_BORDE = new Color(45, 55, 65);
    private static final Color COLOR_TRACK = new Color(13, 20, 28);
    private static final Color COLOR_THUMB = new Color(212, 175, 55);
    private static final Color COLOR_THUMB_HOVER = new Color(232, 197, 82);

    public ModernScrollPane(JComponent view) {
        super(view);
        setBorder(javax.swing.BorderFactory.createLineBorder(COLOR_BORDE));
        setBackground(COLOR_CARD);
        getViewport().setBackground(COLOR_CARD);
        setCorner(UPPER_RIGHT_CORNER, crearEsquina());
        setCorner(LOWER_RIGHT_CORNER, crearEsquina());
        setCorner(UPPER_LEFT_CORNER, crearEsquina());
        setCorner(LOWER_LEFT_CORNER, crearEsquina());
        personalizarBarra(getVerticalScrollBar());
        personalizarBarra(getHorizontalScrollBar());
    }

    private JComponent crearEsquina() {
        JComponent esquina = new JComponent() {};
        esquina.setOpaque(true);
        esquina.setBackground(COLOR_TRACK);
        return esquina;
    }

    private void personalizarBarra(JScrollBar barra) {
        barra.setOpaque(true);
        barra.setBackground(COLOR_TRACK);
        barra.setPreferredSize(new Dimension(12, 12));
        barra.setUnitIncrement(16);
        barra.setBlockIncrement(80);
        barra.setUI(new ModernScrollBarUI());
    }

    private static class ModernScrollBarUI extends BasicScrollBarUI {

        @Override
        protected void configureScrollBarColors() {
            thumbColor = COLOR_THUMB;
            thumbHighlightColor = COLOR_THUMB_HOVER;
            thumbDarkShadowColor = COLOR_THUMB;
            thumbLightShadowColor = COLOR_THUMB;
            trackColor = COLOR_TRACK;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return crearBotonVacio();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return crearBotonVacio();
        }

        private JButton crearBotonVacio() {
            JButton boton = new JButton();
            boton.setPreferredSize(new Dimension(0, 0));
            boton.setMinimumSize(new Dimension(0, 0));
            boton.setMaximumSize(new Dimension(0, 0));
            boton.setBorder(null);
            boton.setFocusable(false);
            boton.setOpaque(false);
            return boton;
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(COLOR_TRACK);
            g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            g2.dispose();
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isDragging || isThumbRollover() ? COLOR_THUMB_HOVER : COLOR_THUMB);

            int padding = 3;
            int x = thumbBounds.x + padding;
            int y = thumbBounds.y + padding;
            int width = Math.max(6, thumbBounds.width - padding * 2);
            int height = Math.max(6, thumbBounds.height - padding * 2);
            g2.fillRoundRect(x, y, width, height, 8, 8);
            g2.dispose();
        }
    }
}
