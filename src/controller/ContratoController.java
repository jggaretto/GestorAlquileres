package controller;

import model.Contrato;
import repository.ContratoDAO;

import java.util.List;

public class ContratoController {

    private ContratoDAO repo = new ContratoDAO();

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
