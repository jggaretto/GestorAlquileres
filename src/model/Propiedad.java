package model;

public class Propiedad {
    private int id;
    private String direccion;
    private String tipo;
    private double precioMensual;
    private boolean disponible;
    private int idPropietario;

    public Propiedad() {}

    public Propiedad(int id, String direccion, String tipo, double precioMensual, boolean disponible, int idPropietario) {
        this.id = id;
        this.direccion = direccion;
        this.tipo = tipo;
        this.precioMensual = precioMensual;
        this.disponible = disponible;
        this.idPropietario = idPropietario;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getPrecioMensual() { return precioMensual; }
    public void setPrecioMensual(double precioMensual) { this.precioMensual = precioMensual; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public int getIdPropietario() { return idPropietario; }
    public void setIdPropietario(int idPropietario) { this.idPropietario = idPropietario; }

    @Override
    public String toString() {
        return direccion + " (" + tipo + ")";
    }
}