package model;

import java.time.LocalDate;

public class Contrato {
    private int id;
    private int idPropiedad;
    private int idInquilino;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double monto;

    public Contrato() {}

    public Contrato(int id, int idPropiedad, int idInquilino, LocalDate fechaInicio, LocalDate fechaFin, double monto) {
        this.id = id;
        this.idPropiedad = idPropiedad;
        this.idInquilino = idInquilino;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.monto = monto;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdPropiedad() { return idPropiedad; }
    public void setIdPropiedad(int idPropiedad) { this.idPropiedad = idPropiedad; }

    public int getIdInquilino() { return idInquilino; }
    public void setIdInquilino(int idInquilino) { this.idInquilino = idInquilino; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    @Override
    public String toString() {
        return "Contrato #" + id + " - Propiedad " + idPropiedad + " / Inquilino " + idInquilino;
    }
}
