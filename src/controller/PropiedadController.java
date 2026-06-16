package controller;

import model.Propiedad;
import repository.PropiedadDAO;
import view.panels.PropiedadesPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PropiedadController {

    private final PropiedadesPanel panel;
    private final PropiedadDAO dao;
    private int idSeleccionado = -1;

    public PropiedadController(PropiedadesPanel panel) {
        this.panel = panel;
        this.dao = new PropiedadDAO();

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
            JOptionPane.showMessageDialog(panel, "Completá todos los campos.");
            return;
        }

        try {
            Propiedad p = new Propiedad(
                    0,
                    panel.getTxtDireccion().getText().trim(),
                    panel.getTxtTipo().getText().trim(),
                    parsearMonto(panel.getTxtPrecioMensual().getText().trim()),
                    true,
                    Integer.parseInt(panel.getTxtIdPropietario().getText().trim())
            );

            if (dao.agregar(p)) {
                JOptionPane.showMessageDialog(panel, "Propiedad guardada correctamente.");
                limpiar();
                listarTodos();
            } else {
                JOptionPane.showMessageDialog(panel, "Error al guardar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panel, "Precio e ID Propietario deben ser números.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(panel, "Seleccioná una propiedad de la tabla.");
            return;
        }

        if (camposVacios()) {
            JOptionPane.showMessageDialog(panel, "Completá todos los campos.");
            return;
        }

        try {
            Propiedad p = new Propiedad(
                    idSeleccionado,
                    panel.getTxtDireccion().getText().trim(),
                    panel.getTxtTipo().getText().trim(),
                    parsearMonto(panel.getTxtPrecioMensual().getText().trim()),
                    true,
                    Integer.parseInt(panel.getTxtIdPropietario().getText().trim())
            );

            if (dao.actualizar(p)) {
                JOptionPane.showMessageDialog(panel, "Propiedad modificada correctamente.");
                limpiar();
                listarTodos();
            } else {
                JOptionPane.showMessageDialog(panel, "Error al modificar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panel, "Precio e ID Propietario deben ser números.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(panel, "Seleccioná una propiedad de la tabla.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                panel,
                "¿Seguro que querés eliminar esta propiedad?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.eliminar(idSeleccionado)) {
                JOptionPane.showMessageDialog(panel, "Propiedad eliminada correctamente.");
                limpiar();
                listarTodos();
            } else {
                JOptionPane.showMessageDialog(panel, "Error al eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void listarTodos() {
        cargarTabla(dao.listar());
    }

    private void buscar() {
        String valor = panel.getTxtBuscar().getText().trim();
        if (valor.isEmpty()) {
            listarTodos();
            return;
        }
        cargarTabla(dao.buscar(valor));
    }

    private void cargarTabla(List<Propiedad> lista) {
        DefaultTableModel modelo = (DefaultTableModel) panel.getTabla().getModel();
        modelo.setRowCount(0);

        for (Propiedad p : lista) {
            modelo.addRow(new Object[]{
                    p.getId(),
                    p.getDireccion(),
                    p.getTipo(),
                    formatearMonto(p.getPrecioMensual()),
                    p.isDisponible() ? "Sí" : "No",
                    p.getIdPropietario()
            });
        }
    }

    private void cargarDesdeTabla() {
        int fila = panel.getTabla().getSelectedRow();
        DefaultTableModel modelo = (DefaultTableModel) panel.getTabla().getModel();

        idSeleccionado = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
        panel.getTxtDireccion().setText(modelo.getValueAt(fila, 1).toString());
        panel.getTxtTipo().setText(modelo.getValueAt(fila, 2).toString());
        panel.getTxtPrecioMensual().setText(modelo.getValueAt(fila, 3).toString());
        panel.getTxtIdPropietario().setText(modelo.getValueAt(fila, 5).toString());
    }

    private void limpiar() {
        panel.getTxtDireccion().setText("");
        panel.getTxtTipo().setText("");
        panel.getTxtPrecioMensual().setText("");
        panel.getTxtIdPropietario().setText("");
        panel.getTxtBuscar().setText("");
        panel.getTabla().clearSelection();
        idSeleccionado = -1;
    }

    private boolean camposVacios() {
        return panel.getTxtDireccion().getText().trim().isEmpty()
                || panel.getTxtTipo().getText().trim().isEmpty()
                || panel.getTxtPrecioMensual().getText().trim().isEmpty()
                || panel.getTxtIdPropietario().getText().trim().isEmpty();
    }

    private String formatearMonto(double monto) {
        NumberFormat formato = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-AR"));
        formato.setMinimumFractionDigits(2);
        formato.setMaximumFractionDigits(2);
        return "$ " + formato.format(monto);
    }

    private double parsearMonto(String texto) {
        String normalizado = texto
                .replace("$", "")
                .replace(" ", "")
                .replace(".", "")
                .replace(",", ".")
                .trim();
        return Double.parseDouble(normalizado);
    }
}
