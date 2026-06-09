package controller;

import model.Pago;
import repository.PagoDAO;
import view.PagosPanel;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class PagoController {

    private final PagosPanel panel;
    private final PagoDAO dao;
    private int idSeleccionado = -1;

    public PagoController(PagosPanel panel) {
        this.panel = panel;
        this.dao = new PagoDAO();

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
            Pago pago = leerPagoDelFormulario(0);
            if (dao.agregar(pago)) {
                JOptionPane.showMessageDialog(panel, "Pago guardado correctamente");
                limpiar();
                listarTodos();
            } else {
                JOptionPane.showMessageDialog(panel, "No se pudo guardar el pago");
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(panel, e.getMessage());
        }
    }

    private void modificar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(panel, "Selecciona un pago de la tabla");
            return;
        }

        try {
            Pago pago = leerPagoDelFormulario(idSeleccionado);
            if (dao.actualizar(pago)) {
                JOptionPane.showMessageDialog(panel, "Pago modificado correctamente");
                limpiar();
                listarTodos();
            } else {
                JOptionPane.showMessageDialog(panel, "No se pudo modificar el pago");
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(panel, e.getMessage());
        }
    }

    private void eliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(panel, "Selecciona un pago de la tabla");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                panel,
                "¿Eliminar el pago seleccionado?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (dao.eliminar(idSeleccionado)) {
                JOptionPane.showMessageDialog(panel, "Pago eliminado correctamente");
                limpiar();
                listarTodos();
            } else {
                JOptionPane.showMessageDialog(panel, "No se pudo eliminar el pago");
            }
        }
    }

    private void listarTodos() {
        cargarTabla(dao.listar(), "");
    }

    private void buscar() {
        cargarTabla(dao.listar(), panel.getTxtBuscar().getText().trim());
    }

    private void cargarTabla(List<Pago> pagos, String filtro) {
        DefaultTableModel modelo = (DefaultTableModel) panel.getTabla().getModel();
        modelo.setRowCount(0);
        String textoFiltro = filtro.toLowerCase();

        for (Pago p : pagos) {
            Object[] fila = {
                    p.getId(),
                    p.getMes(),
                    formatearMonto(p.getMonto()),
                    p.getEstado(),
                    p.getFechaPago(),
                    p.getIdContrato()
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

    private Pago leerPagoDelFormulario(int id) {
        if (camposVacios()) {
            throw new IllegalArgumentException("Completa todos los campos");
        }

        try {
            return new Pago(
                    id,
                    panel.getTxtMes().getText().trim(),
                    parsearMonto(panel.getTxtMonto().getText().trim()),
                    panel.getTxtEstado().getText().trim(),
                    LocalDate.parse(panel.getTxtFechaPago().getText().trim()),
                    Integer.parseInt(panel.getTxtIdContrato().getText().trim())
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Monto e ID Contrato deben ser numericos");
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("La fecha de pago debe tener formato AAAA-MM-DD");
        }
    }

    private void cargarDesdeTabla() {
        int fila = panel.getTabla().getSelectedRow();
        DefaultTableModel modelo = (DefaultTableModel) panel.getTabla().getModel();

        idSeleccionado = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
        panel.getTxtMes().setText(modelo.getValueAt(fila, 1).toString());
        panel.getTxtMonto().setText(modelo.getValueAt(fila, 2).toString());
        panel.getTxtEstado().setText(modelo.getValueAt(fila, 3).toString());
        panel.getTxtFechaPago().setText(modelo.getValueAt(fila, 4).toString());
        panel.getTxtIdContrato().setText(modelo.getValueAt(fila, 5).toString());
    }

    private void limpiar() {
        panel.getTxtMes().setText("");
        panel.getTxtMonto().setText("");
        panel.getTxtEstado().setText("");
        panel.getTxtFechaPago().setText("");
        panel.getTxtIdContrato().setText("");
        panel.getTxtBuscar().setText("");
        panel.getTabla().clearSelection();
        idSeleccionado = -1;
    }

    private boolean camposVacios() {
        return panel.getTxtMes().getText().trim().isEmpty()
                || panel.getTxtMonto().getText().trim().isEmpty()
                || panel.getTxtEstado().getText().trim().isEmpty()
                || panel.getTxtFechaPago().getText().trim().isEmpty()
                || panel.getTxtIdContrato().getText().trim().isEmpty();
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
