package view.components;

import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Font;

public class CampoPasswordEstilizado extends JPasswordField {

    public CampoPasswordEstilizado() {
        setBackground(new Color(30, 40, 52));
        setForeground(Color.WHITE);
        setCaretColor(TemaApp.ACENTO);
        setFont(new Font("SansSerif", Font.PLAIN, 14));
        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(TemaApp.BORDE),
                        new EmptyBorder(10, 15, 10, 15)
                )
        );
    }
}
