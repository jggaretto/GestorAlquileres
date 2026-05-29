import java.sql.Connection;

import repository.Conexion;

public class Main {
    public static void main(String[] args) {
        Connection conn = Conexion.getConexion();
        if (conn != null) {
            System.out.println("Conexion.java funciona correctamente");
        } else {
            System.out.println("Algo falló en Conexion.java");
        }
    }
}