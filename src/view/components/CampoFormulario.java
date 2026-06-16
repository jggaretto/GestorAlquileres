package view.components;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Font;

public class CampoFormulario extends JPanel {

    public CampoFormulario(String titulo, JComponent campo) {
        super(new BorderLayout(0, 8));
        setOpaque(false);

        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(TemaApp.ACENTO);

        add(lbl, BorderLayout.NORTH);
        add(campo, BorderLayout.CENTER);
    }
}
