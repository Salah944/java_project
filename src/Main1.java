import database.ConnectionDb;

import java.sql.Connection;
import java.sql.SQLException;

public class Main1 {
    public static void main(String[] args) {
        try (Connection connection = ConnectionDb.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Connexion a la base de donnees reussie.");
            } else {
                System.out.println("Connexion a la base de donnees echouee.");
            }
        } catch (SQLException e) {
            System.out.println("Connexion a la base de donnees echouee.");
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }
}
