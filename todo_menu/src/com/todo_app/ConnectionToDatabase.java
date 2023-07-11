package com.todo_app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionToDatabase {
    private static ConnectionToDatabase instance;
    private final String url;
    private final String user;
    private final String password;
    private Connection connection;

    private ConnectionToDatabase(String databaseName, String user, String password) {
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

    private Connection createConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Connexion à la base de données réussie !");
            }
        } catch (SQLException e) {
            throw new ConnectionException("Erreur lors de la connexion à la base de données.", e);
        }
        return connection;
    }

    // Exception personnalisée pour gérer l'échec de la connexion
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
            // Effectuez d'autres opérations avec la connexion
        } catch (ConnectionException e) {
            System.out.println("Échec de la connexion à la base de données: " + e.getMessage());
            // Traitez l'échec de la connexion
        }
    }
}
