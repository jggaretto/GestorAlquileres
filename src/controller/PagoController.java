package controller;

import model.Pago;
import repository.PagoRepository;

import java.util.List;

public class PagoController {

    private PagoRepository repo = new PagoRepository();

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
