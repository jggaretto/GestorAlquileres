package controller;

import model.Pago;
import repository.PagoDAO;

import java.util.List;

public class PagoController {

    private PagoDAO repo = new PagoDAO();

    public boolean agregar(Pago p) {
        return repo.agregar(p);
    }

    public List<Pago> listar() {
        return repo.listar();
    }

    public boolean actualizar(Pago p) {
        return repo.actualizar(p);
    }

    public boolean eliminar(int id) {
        return repo.eliminar(id);
    }
}
