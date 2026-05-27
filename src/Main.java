import java.sql.Connection;
import java.sql.DriverManager;

public class Main {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/proyecto_abm";
        String user = "root";
        String password = "root123";

        try {

            Connection conn =
                DriverManager.getConnection(
                    url,
                    user,
                    password
                );

            System.out.println("Conexión exitosa");

            conn.close();

        } catch (Exception e) {

            System.out.println("Error de conexión");
            e.printStackTrace();
        }
    }
}