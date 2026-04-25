package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDb {

    private static  String URL="jdbc:sqlserver://localhost:1433;databaseName=java_project;Encrypt=True;TrustServerCertificate=True;";
    private static String user="sa";
    private static String password="sa";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, user,password);

    }

    public static void closeCnx(Connection cnx) throws SQLException {
        cnx.close();
    }
}
