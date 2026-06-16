package view.components;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public final class EstilizadorTabla {

    private EstilizadorTabla() {}

    public static void apply(JTable tabla) {
        tabla.setRowHeight(38);
        tabla.setBackground(TemaApp.CARD);
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(TemaApp.BORDE);
        tabla.setShowVerticalLines(false);
        tabla.setSelectionBackground(new Color(212, 175, 55, 70));
        tabla.setSelectionForeground(Color.WHITE);

        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(15, 20, 25));
        header.setForeground(TemaApp.ACENTO);
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 42));
    }
}
