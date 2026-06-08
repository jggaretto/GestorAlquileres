package repository;

import model.Usuario;
import repository.UsuarioDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioDAOTest {

    private UsuarioDAO dao;

    @BeforeEach
    public void setUp() {
        dao = new UsuarioDAO();
    }

    @Test
    public void testLoginExitoso() {
        // ATENCIÓN: Este test asume que en la BD se insertó 'admin' con clave 'admin123'
        Usuario resultado = dao.validarLogin("admin", "admin123");

        // Afirmamos que el resultado NO es nulo (el login funcionó)
        assertNotNull(resultado, "El usuario debería haber sido encontrado");
        
        // Afirmamos que los datos mapeados son correctos
        assertEquals("admin", resultado.getUsername(), "El username debe coincidir");
        assertEquals("ADMIN", resultado.getRol(), "El rol debe ser ADMIN");
    }

    @Test
    public void testLoginFallidoPorContrasenaIncorrecta() {
        Usuario resultado = dao.validarLogin("admin", "claveEquivocada");

        // Afirmamos que el resultado ES nulo (el login fue rechazado)
        assertNull(resultado, "El usuario no debería ser validado con clave incorrecta");
    }

    @Test
    public void testLoginFallidoPorUsuarioInexistente() {
        Usuario resultado = dao.validarLogin("usuarioFantasma", "admin123");

        assertNull(resultado, "Un usuario que no existe en la BD debe retornar null");
    }
}