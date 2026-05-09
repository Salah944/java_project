import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import database.ConnectionDb2;

    public class Test {

        public static void main(String[] args) {

            try {

                Connection cnx = ConnectionDb2.getConnection();

                System.out.println("Connexion réussie !");

                ConnectionDb2.closecnx(cnx);

            } catch (SQLException e) {

                System.out.println("Erreur : " + e.getMessage());
            }
        }
    }

