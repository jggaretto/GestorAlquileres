package view.components;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

public class BotonEstilizado extends JButton {

    public BotonEstilizado(String texto, Color fondo, Color textoColor) {
        super(texto);
        setBackground(fondo);
        setForeground(textoColor);
        setFont(new Font("SansSerif", Font.BOLD, 12));
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(new EmptyBorder(12, 25, 12, 25));
    }
}
