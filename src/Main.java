import database.ConnectionDb;

import java.sql.Connection;
import java.sql.SQLException;

void main() {
 try{
     Connection cnx = ConnectionDb.getConnection();
     System.out.println("Connexion réussie !");
 } catch (SQLException e) {
     throw new RuntimeException(e);

 }


}
