package controller;

import model.Propietario;
import repository.PropietarioDAO;
import java.util.List;

public class PropietarioController {

    private PropietarioDAO repo = new PropietarioDAO();

    public boolean agregar (Propietario p) {
        return repo.agregar(p);
    }

    public List<Propietario> listar() {
        return repo.listar();
    }

    public boolean actualizar(Propietario p) {
        return repo.actualizar(p);
    }
    public boolean eliminar(int id) {
        return repo.eliminar(id);
    }
    public List<Propietario> buscar(String nombre) {
        return repo.buscar(nombre);
    }
    
}
