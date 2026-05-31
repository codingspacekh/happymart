package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:happymart.db";
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(DB_URL);
                System.out.println("Connected to SQLite database.");
                initializeDatabase(connection);
            } catch (ClassNotFoundException e) {
                throw new SQLException("SQLite JDBC driver not found: " + e.getMessage());
            }
        }
        return connection;
    }

    private static void initializeDatabase(Connection conn) throws SQLException {
        String createTable = """
            CREATE TABLE IF NOT EXISTS products (
                id        INTEGER PRIMARY KEY AUTOINCREMENT,
                code      TEXT    NOT NULL UNIQUE,
                name      TEXT    NOT NULL,
                amount    INTEGER NOT NULL DEFAULT 0,
                price     REAL    NOT NULL DEFAULT 0.0,
                thumbnail BLOB
            );
        """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTable);
            System.out.println("Products table ready.");
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
