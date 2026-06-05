package controller;

import model.Contrato;
import repository.ContratoRepository;

import java.util.List;

public class ContratoController {

    private ContratoRepository repo = new ContratoRepository();

    public boolean agregar(Contrato c) {
        return repo.agregar(c);
    }

    public List<Contrato> listar() {
        return repo.listar();
    }

    public boolean actualizar(Contrato c) {
        return repo.actualizar(c);
    }

    public boolean eliminar(int id) {
        return repo.eliminar(id);
    }
}
