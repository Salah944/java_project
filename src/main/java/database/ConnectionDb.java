package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionDb {

    private static final String URL = getConfig(
            "JAVA_PROJECT_DB_URL",
            "jdbc:sqlserver://localhost:1433;databaseName=FermeAgricole;Encrypt=True;TrustServerCertificate=True;"
    );
    private static final String USER = getConfig("JAVA_PROJECT_DB_USER", "sa");
    private static final String PASSWORD = getConfig("JAVA_PROJECT_DB_PASSWORD", "sa");

    private ConnectionDb() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public static void closecnx(Connection cnx) throws SQLException{
        cnx.close();
    }

    private static String getConfig(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
