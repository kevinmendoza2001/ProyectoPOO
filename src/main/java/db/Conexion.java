package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL = "jdbc:postgresql://ep-solitary-salad-avat9bsg-pooler.c-11.us-east-1.aws.neon.tech/neondb?sslmode=require";
    private static final String USUARIO = "neondb_owner";
    private static final String PASSWORD = "npg_mh3WbFXO6DiH";

    private static Conexion instancia;
    private Connection conexion;

    private Conexion() {
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    public static synchronized Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    public static synchronized Connection conectar() {
        try {
            if (getInstancia().conexion == null || getInstancia().conexion.isClosed()) {
                instancia = null;
                instancia = getInstancia();
            }
        } catch (SQLException e) {
            instancia = null;
            getInstancia();
        }
        return getInstancia().conexion;
    }
}