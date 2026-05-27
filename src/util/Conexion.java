package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {

    private static final String URL  = "jdbc:mysql://localhost:3306/proyecto_abm";
    private static final String USER = "root";
    private static final String PASS = "root123";

    // Devuelve una conexión nueva cada vez que se llama
    public static Connection getConexion() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return null;
        }
    }
}