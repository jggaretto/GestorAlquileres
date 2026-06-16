package view.components;

import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Font;

public class CampoTextoEstilizado extends JTextField {

    public CampoTextoEstilizado(int columns) {
        super(columns);
        setBackground(new Color(30, 40, 52));
        setForeground(Color.WHITE);
        setCaretColor(TemaApp.ACENTO);
        setFont(new Font("SansSerif", Font.PLAIN, 14));
        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(TemaApp.BORDE),
                        new EmptyBorder(12, 18, 12, 18)
                )
        );
    }
}
