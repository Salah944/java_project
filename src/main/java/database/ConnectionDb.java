package database;
import java.sql.*;

public final class ConnectionDb {
    private static final String URL = getConfig(
            "JAVA_PROJECT_DB_URL",
            "jdbc:sqlserver://localhost:1433;databaseName=FermeAgricole;Encrypt=True;TrustServerCertificate=True;"
    );
    private static final String USER = getConfig("JAVA_PROJECT_DB_USER", "sa");
    private static final String PASSWORD = getConfig("JAVA_PROJECT_DB_PASSWORD", "sa");

    private ConnectionDb() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        System.out.println("V\u00e9rification de la base de donn\u00e9es...");
        try (Connection cnx = getConnection(); Statement stmt = cnx.createStatement()) {
            // Cr\u00e9ation de la table Users si absente
            String createUsers = "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Users') " +
                                "CREATE TABLE Users (id INT PRIMARY KEY IDENTITY, name NVARCHAR(100), email NVARCHAR(100) UNIQUE, password NVARCHAR(255), role NVARCHAR(20), farm_id INT NULL)";
            stmt.execute(createUsers);

            // Ajout de farm_id si absent (cas d'une table existante)
            DatabaseMetaData metaData = cnx.getMetaData();
            try (ResultSet rs = metaData.getColumns(null, null, "Users", "farm_id")) {
                if (!rs.next()) {
                    stmt.executeUpdate("ALTER TABLE Users ADD farm_id INT NULL");
                    System.out.println("Colonne farm_id ajout\u00e9e.");
                }
            }

            // Cr\u00e9ation de la table Ouvrier si absente
            String createOuvrier = "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Ouvrier') " +
                                  "CREATE TABLE Ouvrier (id INT PRIMARY KEY, salaire FLOAT, role NVARCHAR(20), farm_id INT)";
            stmt.execute(createOuvrier);
            
            System.out.println("Base de donn\u00e9es initialis\u00e9e.");
        } catch (SQLException e) {
            System.err.println("Erreur d'initialisation : " + e.getMessage());
        }
    }

    private static String getConfig(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
