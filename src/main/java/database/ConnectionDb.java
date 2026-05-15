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
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("!!! ERREUR DE CONNEXION SQL SERVER !!!");
            System.err.println("URL test\u00e9e : " + URL);
            System.err.println("Utilisateur : " + USER);
            System.err.println("Message : " + e.getMessage());
            throw e;
        }
    }

    public static void initializeDatabase() {
        System.out.println(">>> Initialisation de la base de donn\u00e9es...");
        try (Connection cnx = getConnection()) {
            try (Statement stmt = cnx.createStatement()) {
                // 1. Cr\u00e9er la table Users si elle n'existe pas
                String sqlUsers = "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Users') " +
                                 "CREATE TABLE Users (id INT PRIMARY KEY IDENTITY, name NVARCHAR(100), email NVARCHAR(100) UNIQUE, password NVARCHAR(255), role NVARCHAR(20), farm_id INT NULL)";
                stmt.execute(sqlUsers);
                
                // 2. S'assurer que farm_id existe (au cas o\u00f9 la table existait d\u00e9j\u00e0 sans cette colonne)
                DatabaseMetaData metaData = cnx.getMetaData();
                try (ResultSet rs = metaData.getColumns(null, null, "Users", "farm_id")) {
                    if (!rs.next()) {
                        stmt.executeUpdate("ALTER TABLE Users ADD farm_id INT NULL");
                        System.out.println("-> Colonne farm_id ajout\u00e9e.");
                    }
                }

                // 3. Cr\u00e9er la table Ouvrier si elle n'existe pas
                String sqlOuvrier = "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Ouvrier') " +
                                   "CREATE TABLE Ouvrier (id INT PRIMARY KEY, salaire FLOAT, role NVARCHAR(20), farm_id INT)";
                stmt.execute(sqlOuvrier);

                System.out.println(">>> Base de donn\u00e9es pr\u00eate.");
            }
        } catch (SQLException e) {
            System.err.println("!!! ERREUR LORS DE L'INITIALISATION : " + e.getMessage());
        }
    }

    public static void closecnx(Connection cnx) throws SQLException {
        if (cnx != null) cnx.close();
    }

    private static String getConfig(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
