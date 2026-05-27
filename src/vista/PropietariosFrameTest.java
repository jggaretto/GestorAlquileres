package vista;

import util.Conexion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * VERSIÓN DE PRUEBA — habla directo con la BD sin modelo ni controlador.
 * Solo para testear que la base de datos funciona correctamente.
 * NO es la versión final del proyecto.
 */
public class PropietariosFrameTest extends JFrame {

    // Campos del formulario
    private JTextField txtNombre, txtApellido, txtTelefono, txtEmail, txtBuscar;

    // Tabla
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    // ID de la fila seleccionada en la tabla
    private int idSeleccionado = -1;

    // ─────────────────────────────────────────────────────────────────────────
    public PropietariosFrameTest() {
        setTitle("Propietarios — Prueba directa BD");
        setSize(800, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(crearFormulario(), BorderLayout.NORTH);
        add(crearTabla(),      BorderLayout.CENTER);
        add(crearBotones(),    BorderLayout.SOUTH);

        listarTodos(); // carga los datos al abrir
    }

    // ── FORMULARIO ────────────────────────────────────────────────────────────
    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del propietario"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        txtNombre   = new JTextField(14);
        txtApellido = new JTextField(14);
        txtTelefono = new JTextField(12);
        txtEmail    = new JTextField(18);
        txtBuscar   = new JTextField(16);

        gbc.gridx=0; gbc.gridy=0; panel.add(new JLabel("Nombre:"),   gbc);
        gbc.gridx=1;              panel.add(txtNombre,                gbc);
        gbc.gridx=2;              panel.add(new JLabel("Apellido:"),  gbc);
        gbc.gridx=3;              panel.add(txtApellido,              gbc);

        gbc.gridx=0; gbc.gridy=1; panel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx=1;              panel.add(txtTelefono,              gbc);
        gbc.gridx=2;              panel.add(new JLabel("Email:"),     gbc);
        gbc.gridx=3;              panel.add(txtEmail,                 gbc);

        gbc.gridx=0; gbc.gridy=2; panel.add(new JLabel("Buscar:"),   gbc);
        gbc.gridx=1;              panel.add(txtBuscar,                gbc);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscar());
        gbc.gridx=2; panel.add(btnBuscar, gbc);

        JButton btnVerTodos = new JButton("Ver todos");
        btnVerTodos.addActionListener(e -> {
            txtBuscar.setText("");
            listarTodos();
        });
        gbc.gridx=3; panel.add(btnVerTodos, gbc);

        return panel;
    }

    // ── TABLA ─────────────────────────────────────────────────────────────────
    private JScrollPane crearTabla() {
        modeloTabla = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Apellido", "Teléfono", "Email"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(24);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);

        // Al hacer clic en una fila → carga los datos en el formulario
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                int fila = tabla.getSelectedRow();
                idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
                txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                txtApellido.setText(modeloTabla.getValueAt(fila, 2).toString());
                txtTelefono.setText(modeloTabla.getValueAt(fila, 3).toString());
                txtEmail.setText(modeloTabla.getValueAt(fila, 4).toString());
            }
        });

        return new JScrollPane(tabla);
    }

    // ── BOTONES ───────────────────────────────────────────────────────────────
    private JPanel crearBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));

        JButton btnGuardar   = new JButton("Guardar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar  = new JButton("Eliminar");
        JButton btnLimpiar   = new JButton("Limpiar");

        btnGuardar.addActionListener(e   -> guardar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e  -> eliminar());
        btnLimpiar.addActionListener(e   -> limpiar());

        panel.add(btnGuardar);
        panel.add(btnModificar);
        panel.add(btnEliminar);
        panel.add(btnLimpiar);

        return panel;
    }

    // ── GUARDAR → INSERT directo a la BD ──────────────────────────────────────
    private void guardar() {
        if (camposVacios()) {
            JOptionPane.showMessageDialog(this, "Completá todos los campos.");
            return;
        }
        String sql = "INSERT INTO propietarios (nombre, apellido, telefono, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, txtNombre.getText().trim());
            ps.setString(2, txtApellido.getText().trim());
            ps.setString(3, txtTelefono.getText().trim());
            ps.setString(4, txtEmail.getText().trim());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Propietario guardado.");
            limpiar();
            listarTodos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── MODIFICAR → UPDATE directo a la BD ───────────────────────────────────
    private void modificar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccioná un propietario de la tabla.");
            return;
        }
        if (camposVacios()) {
            JOptionPane.showMessageDialog(this, "Completá todos los campos.");
            return;
        }
        String sql = "UPDATE propietarios SET nombre=?, apellido=?, telefono=?, email=? WHERE id=?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, txtNombre.getText().trim());
            ps.setString(2, txtApellido.getText().trim());
            ps.setString(3, txtTelefono.getText().trim());
            ps.setString(4, txtEmail.getText().trim());
            ps.setInt(5, idSeleccionado);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Propietario modificado.");
            limpiar();
            listarTodos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── ELIMINAR → DELETE directo a la BD ────────────────────────────────────
    private void eliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccioná un propietario de la tabla.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Seguro que querés eliminar este propietario?",
            "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM propietarios WHERE id=?";
            try (Connection conn = Conexion.getConexion();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, idSeleccionado);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Propietario eliminado.");
                limpiar();
                listarTodos();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ── LISTAR TODOS → SELECT directo a la BD ────────────────────────────────
    private void listarTodos() {
        modeloTabla.setRowCount(0);
        String sql = "SELECT * FROM propietarios";
        try (Connection conn = Conexion.getConexion();
             Statement st   = conn.createStatement();
             ResultSet rs   = st.executeQuery(sql)) {

            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("telefono"),
                    rs.getString("email")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── BUSCAR → SELECT con WHERE directo a la BD ─────────────────────────────
  private void buscar() {
    String texto = txtBuscar.getText().trim();
    if (texto.isEmpty()) { listarTodos(); return; }

    modeloTabla.setRowCount(0);
    String sql;

    // Si es número busca por ID exacto, si no busca por nombre/apellido
    if (texto.matches("\\d+")) {
        sql = "SELECT * FROM propietarios WHERE id = ?";
    } else {
        sql = "SELECT * FROM propietarios WHERE nombre LIKE ? OR apellido LIKE ?";
    }

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        if (texto.matches("\\d+")) {
            ps.setInt(1, Integer.parseInt(texto));
        } else {
            String patron = "%" + texto + "%";
            ps.setString(1, patron);
            ps.setString(2, patron);
        }

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            modeloTabla.addRow(new Object[]{
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("telefono"),
                rs.getString("email")
            });
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al buscar: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    // ── HELPERS ───────────────────────────────────────────────────────────────
    private void limpiar() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtBuscar.setText("");
        tabla.clearSelection();
        idSeleccionado = -1;
    }

    private boolean camposVacios() {
        return txtNombre.getText().trim().isEmpty()
            || txtApellido.getText().trim().isEmpty()
            || txtTelefono.getText().trim().isEmpty()
            || txtEmail.getText().trim().isEmpty();
    }

    // ── MAIN ──────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PropietariosFrameTest().setVisible(true));
    }
}