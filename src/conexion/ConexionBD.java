package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Configuración para XAMPP (puerto 3306, usuario root, sin contraseña)
    private static final String URL = "jdbc:mysql://localhost:3306/speedfast_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    public static Connection conectar() {
        Connection conexion = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            System.out.println("¡Conexión exitosa a speedfast_db!");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver JDBC no encontrado en las librerías.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
        return conexion;
    }

    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada exitosamente.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Connection con = null;
        try {
            con = ConexionBD.conectar();
            if (con != null && !con.isClosed()) {
                System.out.println(">>> Paso 2 completado exitosamente <<<");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexionBD.cerrarConexion(con);
        }
    }
}