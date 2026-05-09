import model.User;

import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;

public class ConnectionDb2 {
    private String URL = "jdbc:sqlserver://localhost:1433;databaseName=FermeAgricole;encrypt=true;trustServerCertificate=true" ;
    private String USER = "sa";
    private String PSWD ="sa";
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL, USER, PSWD);
    }
    public static void closecnx(Connection cnx) throws SQLException{
        cnx.close();
    }

}