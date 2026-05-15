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
        System.out.println("V\u00e9rification de la structure de la base de donn\u00e9es...");
        try (Connection cnx = getConnection()) {
            // V\u00e9rifier si la colonne farm_id existe dans Users
            DatabaseMetaData metaData = cnx.getMetaData();
            try (ResultSet rs = metaData.getColumns(null, null, "Users", "farm_id")) {
                if (!rs.next()) {
                    System.out.println("Ajout de la colonne farm_id \u00e0 la table Users...");
                    try (Statement stmt = cnx.createStatement()) {
                        stmt.executeUpdate("ALTER TABLE Users ADD farm_id INT NULL");
                        System.out.println("Colonne farm_id ajout\u00e9e avec succ\u00e8s.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Note: La migration automatique a échoué ou la colonne existe déjà. " + e.getMessage());
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
