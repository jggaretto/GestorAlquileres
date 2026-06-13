package controller;

import model.Inquilino;
import repository.InquilinoDAO;
import view.panels.InquilinosPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class InquilinoController {

    private final InquilinosPanel panel;
    private final InquilinoDAO dao;
    private int idSeleccionado = -1;

    public InquilinoController(InquilinosPanel panel) {
        this.panel = panel;
        this.dao = new InquilinoDAO();

        iniciarEventos();
        listarTodos();
    }

    // --- EVENTOS ---
    private void iniciarEventos() {

        // Botón Guardar
        panel.getBtnGuardar().addActionListener(e -> guardar());

        // Botón Modificar
        panel.getBtnModificar().addActionListener(e -> modificar());

        // Botón Eliminar
        panel.getBtnEliminar().addActionListener(e -> eliminar());

        // Botón Limpiar
        panel.getBtnLimpiar().addActionListener(e -> limpiar());

        // Botón Buscar
        panel.getBtnBuscar().addActionListener(e -> buscar());

        // Clic en fila de la tabla → carga datos en el formulario
        panel.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && panel.getTabla().getSelectedRow() >= 0) {
                cargarDesdeTabla();
            }
        });
    }

    // --- GUARDAR ---
    private void guardar() {
        if (dao.existeDni(panel.getTxtDni().getText().trim())) {
            JOptionPane.showMessageDialog(panel, "Ya existe un inquilino con ese DNI.",
              "DNI duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (camposVacios()) {
            JOptionPane.showMessageDialog(panel, "Completá todos los campos.");
            return;
        }

        Inquilino i = new Inquilino(
            panel.getTxtNombre().getText().trim(),
            panel.getTxtApellido().getText().trim(),
            panel.getTxtDni().getText().trim(),
            panel.getTxtTelefono().getText().trim());

        if (dao.insertar(i)) {
            JOptionPane.showMessageDialog(panel, "Inquilino guardado correctamente.");
            limpiar();
            listarTodos();
        } else {
            JOptionPane.showMessageDialog(panel, "Error al guardar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- MODIFICAR ---
    private void modificar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(panel, "Seleccioná un inquilino de la tabla.");
            return;
        }
        if (camposVacios()) {
            JOptionPane.showMessageDialog(panel, "Completá todos los campos.");
            return;
        }

        Inquilino i = new Inquilino(
            idSeleccionado,
            panel.getTxtNombre().getText().trim(),
            panel.getTxtApellido().getText().trim(),
            panel.getTxtDni().getText().trim(),
            panel.getTxtTelefono().getText().trim());

        if (dao.modificar(i)) {
            JOptionPane.showMessageDialog(panel, "Inquilino modificado correctamente.");
            limpiar();
            listarTodos();
        } else {
            JOptionPane.showMessageDialog(panel, "Error al modificar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- ELIMINAR ---
    private void eliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(panel, "Seleccioná un inquilino de la tabla.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            panel,"¿Seguro que querés eliminar este inquilino?","Confirmar",JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.eliminar(idSeleccionado)) {
                JOptionPane.showMessageDialog(panel, "Inquilino eliminado correctamente.");
                limpiar();
                listarTodos();
            } else {
                JOptionPane.showMessageDialog(panel, "Error al eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // --- LISTAR TODOS ---
    private void listarTodos() {
        cargarTabla(dao.listarTodos());
    }

    // --- BUSCAR ---
    private void buscar() {
        String texto = panel.getTxtBuscar().getText().trim();
        if (texto.isEmpty()) {
            listarTodos();
            return;
        }
        cargarTabla(dao.buscar(texto));
    }

    // --- CARGAR TABLA ---
    private void cargarTabla(List<Inquilino> lista) {
        DefaultTableModel modelo = (DefaultTableModel) panel.getTabla().getModel();
        modelo.setRowCount(0);
        for (Inquilino i : lista) {
            modelo.addRow(new Object[] {
                i.getId(),
                i.getNombre(),
                i.getApellido(),
                i.getDni(),
                i.getTelefono()
            });
        }
    }

    // --- CARGAR DESDE TABLA ---
    private void cargarDesdeTabla() {
        int fila = panel.getTabla().getSelectedRow();
        DefaultTableModel modelo = (DefaultTableModel) panel.getTabla().getModel();

        idSeleccionado = (int) modelo.getValueAt(fila, 0);
        panel.getTxtNombre().setText(modelo.getValueAt(fila, 1).toString());
        panel.getTxtApellido().setText(modelo.getValueAt(fila, 2).toString());
        panel.getTxtDni().setText(modelo.getValueAt(fila, 3).toString());
        panel.getTxtTelefono().setText(modelo.getValueAt(fila, 4).toString());
    }

    // --- LIMPIAR ---
    private void limpiar() {
        panel.getTxtNombre().setText("");
        panel.getTxtApellido().setText("");
        panel.getTxtDni().setText("");
        panel.getTxtTelefono().setText("");
        panel.getTxtBuscar().setText("");
        panel.getTabla().clearSelection();
        idSeleccionado = -1;
    }

    // --- HELPERS ---
    private boolean camposVacios() {
        return panel.getTxtNombre().getText().trim().isEmpty()
        || panel.getTxtApellido().getText().trim().isEmpty()
        || panel.getTxtDni().getText().trim().isEmpty()
        || panel.getTxtTelefono().getText().trim().isEmpty();
    }
}
