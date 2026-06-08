package model;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

public class Pago {
    private int id;
    private String mes;
    private double monto;
    private String estado;
    private LocalDate fechaPago;
    private int idContrato;

    public Pago() {}

    public Pago(int id, String mes, double monto, String estado, LocalDate fechaPago, int idContrato) {
        this.id = id;
        this.mes = mes;
        this.monto = monto;
        this.estado = estado;
        this.fechaPago = fechaPago;
        this.idContrato = idContrato;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMes() { return mes; }
    public void setMes(String mes) { this.mes = mes; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }

    public int getIdContrato() { return idContrato; }
    public void setIdContrato(int idContrato) { this.idContrato = idContrato; }

    @Override
    public String toString() {
        return "Pago #" + id + " - " + mes + " (" + formatearMonto(monto) + ")";
    }

    private String formatearMonto(double monto) {
        NumberFormat formato = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-AR"));
        formato.setMinimumFractionDigits(2);
        formato.setMaximumFractionDigits(2);
        return "$ " + formato.format(monto);
    }
}
