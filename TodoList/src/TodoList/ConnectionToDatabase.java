package TodoList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionToDatabase {
    private static ConnectionToDatabase instance;
    private final String url;
    private final String user;
    private final String password;
    private Connection connection;

    public ConnectionToDatabase(String databaseName, String user, String password) {
        this.url = "jdbc:postgresql://localhost/" + databaseName;
        this.user = user;
        this.password = password;
        this.connection = createConnection();
    }

    public static ConnectionToDatabase getInstance(String databaseName, String user, String password) {
        if (instance == null) {
            synchronized (ConnectionToDatabase.class) {
                if (instance == null) {
                    instance = new ConnectionToDatabase(databaseName, user, password);
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    Connection createConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Connection to the database successful!");
            }
        } catch (SQLException e) {
            throw new ConnectionException("Error connecting to the database.", e);
        }
        return connection;
    }

    public static class ConnectionException extends RuntimeException {
        public ConnectionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static void main(String[] args) {
        String databaseName = "todo_app";
        String user = "postgres";
        String password = "lisa";

        try {
            ConnectionToDatabase connectionToDatabase = ConnectionToDatabase.getInstance(databaseName, user, password);
            Connection connection = connectionToDatabase.getConnection();
            // Effectuer d'autres opérations avec la connexion
        } catch (ConnectionException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            // Traiter l'échec de la connexion
        }
    }
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection to the database closed.");
            }
        } catch (SQLException e) {
            // Gérer l'exception appropriée ici
        }
    }
}
