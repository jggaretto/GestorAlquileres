import javax.swing.SwingUtilities;
import view.MenuFrame;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new MenuFrame().setVisible(true);
        });

    }
}