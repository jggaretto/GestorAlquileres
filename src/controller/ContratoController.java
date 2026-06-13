package controller;

import model.Contrato;
import repository.ContratoDAO;
import view.panels.ContratosPanel;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class ContratoController {

    private final ContratosPanel panel;
    private final ContratoDAO dao;
    private int idSeleccionado = -1;

    public ContratoController(ContratosPanel panel) {
        this.panel = panel;
        this.dao = new ContratoDAO();

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
        try {
            Contrato contrato = leerContratoDelFormulario(0);
            if (dao.agregar(contrato)) {
                JOptionPane.showMessageDialog(panel, "Contrato guardado correctamente");
                limpiar();
                listarTodos();
            } else {
                JOptionPane.showMessageDialog(panel, "No se pudo guardar el contrato");
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(panel, e.getMessage());
        }
    }

    private void modificar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(panel, "Selecciona un contrato de la tabla");
            return;
        }

        try {
            Contrato contrato = leerContratoDelFormulario(idSeleccionado);
            if (dao.actualizar(contrato)) {
                JOptionPane.showMessageDialog(panel, "Contrato modificado correctamente");
                limpiar();
                listarTodos();
            } else {
                JOptionPane.showMessageDialog(panel, "No se pudo modificar el contrato");
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(panel, e.getMessage());
        }
    }

    private void eliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(panel, "Selecciona un contrato de la tabla");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                panel,
                "¿Eliminar el contrato seleccionado?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (dao.eliminar(idSeleccionado)) {
                JOptionPane.showMessageDialog(panel, "Contrato eliminado correctamente");
                limpiar();
                listarTodos();
            } else {
                JOptionPane.showMessageDialog(panel, "No se pudo eliminar el contrato");
            }
        }
    }

    private void listarTodos() {
        cargarTabla(dao.listar(), "");
    }

    private void buscar() {
        cargarTabla(dao.listar(), panel.getTxtBuscar().getText().trim());
    }

    private void cargarTabla(List<Contrato> contratos, String filtro) {
        DefaultTableModel modelo = (DefaultTableModel) panel.getTabla().getModel();
        modelo.setRowCount(0);
        String textoFiltro = filtro.toLowerCase();

        for (Contrato c : contratos) {
            Object[] fila = {
                    c.getId(),
                    c.getFechaInicio(),
                    c.getFechaFin(),
                    formatearMonto(c.getMonto()),
                    c.getIdInquilino(),
                    c.getIdPropiedad()
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

    private Contrato leerContratoDelFormulario(int id) {
        if (camposVacios()) {
            throw new IllegalArgumentException("Completa todos los campos");
        }

        try {
            return new Contrato(
                    id,
                    Integer.parseInt(panel.getTxtIdPropiedad().getText().trim()),
                    Integer.parseInt(panel.getTxtIdInquilino().getText().trim()),
                    LocalDate.parse(panel.getTxtFechaInicio().getText().trim()),
                    LocalDate.parse(panel.getTxtFechaFin().getText().trim()),
                    parsearMonto(panel.getTxtMonto().getText().trim())
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Monto, ID Inquilino e ID Propiedad deben ser numericos");
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Las fechas deben tener formato AAAA-MM-DD");
        }
    }

    private void cargarDesdeTabla() {
        int fila = panel.getTabla().getSelectedRow();
        DefaultTableModel modelo = (DefaultTableModel) panel.getTabla().getModel();

        idSeleccionado = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
        panel.getTxtFechaInicio().setText(modelo.getValueAt(fila, 1).toString());
        panel.getTxtFechaFin().setText(modelo.getValueAt(fila, 2).toString());
        panel.getTxtMonto().setText(modelo.getValueAt(fila, 3).toString());
        panel.getTxtIdInquilino().setText(modelo.getValueAt(fila, 4).toString());
        panel.getTxtIdPropiedad().setText(modelo.getValueAt(fila, 5).toString());
    }

    private void limpiar() {
        panel.getTxtFechaInicio().setText("");
        panel.getTxtFechaFin().setText("");
        panel.getTxtMonto().setText("");
        panel.getTxtIdInquilino().setText("");
        panel.getTxtIdPropiedad().setText("");
        panel.getTxtBuscar().setText("");
        panel.getTabla().clearSelection();
        idSeleccionado = -1;
    }

    private boolean camposVacios() {
        return panel.getTxtFechaInicio().getText().trim().isEmpty()
                || panel.getTxtFechaFin().getText().trim().isEmpty()
                || panel.getTxtMonto().getText().trim().isEmpty()
                || panel.getTxtIdInquilino().getText().trim().isEmpty()
                || panel.getTxtIdPropiedad().getText().trim().isEmpty();
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
