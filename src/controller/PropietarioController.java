package controller;

import model.Propietario;
import repository.PropietarioDAO;
import view.PropietariosPanel;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class PropietarioController {

    private final PropietariosPanel panel;
    private final PropietarioDAO dao;
    private int idSeleccionado = -1;

    public PropietarioController(PropietariosPanel panel) {
        this.panel = panel;
        this.dao = new PropietarioDAO();

        iniciarEventos();
        listarTodos();
    }

    private void iniciarEventos() {
        panel.getBtnGuardar().addActionListener(e -> guardar());
        panel.getBtnModificar().addActionListener(e -> modificar());
        panel.getBtnEliminar().addActionListener(e -> eliminar());
        panel.getBtnLimpiar().addActionListener(e -> limpiar());
        panel.getBtnBuscar().addActionListener(e -> buscar());

        panel.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && panel.getTabla().getSelectedRow() >= 0) {
                cargarDesdeTabla();
            }
        });
    }

    private void guardar() {
        if (camposVacios()) {
            JOptionPane.showMessageDialog(panel, "Completa todos los campos");
            return;
        }

        Propietario p = leerPropietarioDelFormulario(0);
        if (dao.agregar(p)) {
            JOptionPane.showMessageDialog(panel, "Propietario guardado correctamente");
            limpiar();
            listarTodos();
        } else {
            JOptionPane.showMessageDialog(panel, "No se pudo guardar el propietario");
        }
    }

    private void modificar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(panel, "Selecciona un propietario de la tabla");
            return;
        }

        if (camposVacios()) {
            JOptionPane.showMessageDialog(panel, "Completa todos los campos");
            return;
        }

        Propietario p = leerPropietarioDelFormulario(idSeleccionado);
        if (dao.actualizar(p)) {
            JOptionPane.showMessageDialog(panel, "Propietario modificado correctamente");
            limpiar();
            listarTodos();
        } else {
            JOptionPane.showMessageDialog(panel, "No se pudo modificar el propietario");
        }
    }

    private void eliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(panel, "Selecciona un propietario de la tabla");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                panel,
                "¿Eliminar el propietario seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (dao.eliminar(idSeleccionado)) {
                JOptionPane.showMessageDialog(panel, "Propietario eliminado correctamente");
                limpiar();
                listarTodos();
            } else {
                JOptionPane.showMessageDialog(panel, "No se pudo eliminar el propietario");
            }
        }
    }

    private void listarTodos() {
        cargarTabla(dao.listar(), "");
    }

    private void buscar() {
        String texto = panel.getTxtBuscar().getText().trim();
        if (texto.isEmpty()) {
            listarTodos();
        } else {
            cargarTabla(dao.buscar(texto), "");
        }
    }

    private void cargarTabla(List<Propietario> propietarios, String filtro) {
        DefaultTableModel modelo = (DefaultTableModel) panel.getTabla().getModel();
        modelo.setRowCount(0);
        String textoFiltro = filtro.toLowerCase();

        for (Propietario p : propietarios) {
            Object[] fila = {
                    p.getId(),
                    p.getNombre(),
                    p.getApellido(),
                    p.getTelefono(),
                    p.getEmail()
            };

            if (textoFiltro.isEmpty() || coincideFiltro(fila, textoFiltro)) {
                modelo.addRow(fila);
            }
        }
    }

    private boolean coincideFiltro(Object[] fila, String filtro) {
        for (Object valor : fila) {
            if (String.valueOf(valor).toLowerCase().contains(filtro)) {
                return true;
            }
        }
        return false;
    }

    private Propietario leerPropietarioDelFormulario(int id) {
        return new Propietario(
                id,
                panel.getTxtNombre().getText().trim(),
                panel.getTxtApellido().getText().trim(),
                panel.getTxtTelefono().getText().trim(),
                panel.getTxtEmail().getText().trim()
        );
    }

    private void cargarDesdeTabla() {
        int fila = panel.getTabla().getSelectedRow();
        DefaultTableModel modelo = (DefaultTableModel) panel.getTabla().getModel();

        idSeleccionado = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
        panel.getTxtNombre().setText(modelo.getValueAt(fila, 1).toString());
        panel.getTxtApellido().setText(modelo.getValueAt(fila, 2).toString());
        panel.getTxtTelefono().setText(modelo.getValueAt(fila, 3).toString());
        panel.getTxtEmail().setText(modelo.getValueAt(fila, 4).toString());
    }

    private void limpiar() {
        panel.getTxtNombre().setText("");
        panel.getTxtApellido().setText("");
        panel.getTxtTelefono().setText("");
        panel.getTxtEmail().setText("");
        panel.getTxtBuscar().setText("");
        panel.getTabla().clearSelection();
        idSeleccionado = -1;
    }

    private boolean camposVacios() {
        return panel.getTxtNombre().getText().trim().isEmpty()
                || panel.getTxtApellido().getText().trim().isEmpty()
                || panel.getTxtTelefono().getText().trim().isEmpty()
                || panel.getTxtEmail().getText().trim().isEmpty();
    }
}