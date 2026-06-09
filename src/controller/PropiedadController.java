package controller;

import model.Propiedad;
import repository.PropiedadDAO;

import java.util.List;

public class PropiedadController {

    private PropiedadDAO repo = new PropiedadDAO();

    public boolean agregar(Propiedad p) {
        return repo.agregar(p);
    }

    public List<Propiedad> listar() {
        return repo.listar();
    }

    public boolean actualizar(Propiedad p) {
        return repo.actualizar(p);
    }

    public boolean eliminar(int id) {
        return repo.eliminar(id);
    }
}