package TodoList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TodoMenu {
    private static final Connection connection;
    private static final Scanner scanner = new Scanner(System.in);

    static {
        ConnectionToDatabase db = new ConnectionToDatabase("todo_app", "postgres", "lisa");
        connection = db.getConnection();
    }

    public static void main(String[] args) {
        try {
            displayMenu();
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database: " + e.getMessage());
        } finally {
            closeConnection(connection);
        }
    }

    private static void displayMenu() throws SQLException {
        int choice;
        do {
            printMenu();
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    findTask();
                    break;
                case 3:
                    showAllTasks();
                    break;
                case 4:
                    updateTask();
                    break;
                case 5:
                    deleteTask();
                    break;
                case 6:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);
    }

    private static void printMenu() {
        System.out.println("===== TODO Menu =====");
        System.out.println("1: Add a new task");
        System.out.println("2: Find a task");
        System.out.println("3: Show all tasks");
        System.out.println("4: Update a task");
        System.out.println("5: Delete a task");
        System.out.println("6: Quit");
        System.out.print("Enter your choice: ");
    }

    private static void addTask() throws SQLException {
        System.out.println("Enter the task details:");
        System.out.print("ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Deadline (YYYY-MM-DD HH:MM:SS): ");
        String deadlineString = scanner.nextLine();
        Timestamp deadline = Timestamp.valueOf(deadlineString);

        System.out.print("Priority (1-5): ");
        int priority = scanner.nextInt();

        System.out.print("Done (true/false): ");
        boolean done = scanner.nextBoolean();

        insertTodo(id, title, description, deadline, priority, done);
        System.out.println("Task added successfully.");
    }

    private static void findTask() throws SQLException {
        System.out.print("Enter the task ID: ");
        int taskId = scanner.nextInt();
        Task foundTask = findTaskById(taskId);
        if (foundTask != null) {
            System.out.println(foundTask);
        } else {
            System.out.println("Task not found.");
        }
    }

    private static void showAllTasks() throws SQLException {
        List<Task> allTasks = findAllTasks();
        if (!allTasks.isEmpty()) {
            for (Task task : allTasks) {
                System.out.println(task);
            }
        } else {
            System.out.println("No tasks found.");
        }
    }

    private static void updateTask() throws SQLException {
        System.out.print("Enter the task ID to update: ");
        int taskIdToUpdate = scanner.nextInt();
        scanner.nextLine();

        Task taskToUpdate = findTaskById(taskIdToUpdate);
        if (taskToUpdate != null) {
            System.out.println("Enter the new details for the task:");
            System.out.print("Title: ");
            String newTitle = scanner.nextLine();
            System.out.print("Description: ");
            String newDescription = scanner.nextLine();
            System.out.print("Deadline (YYYY-MM-DD HH:MM:SS): ");
            String newDeadlineString = scanner.nextLine();
            Timestamp newDeadline = Timestamp.valueOf(newDeadlineString);
            System.out.print("Priority (1-5): ");
            int newPriority = scanner.nextInt();
            System.out.print("Done (true/false): ");
            boolean newDone = scanner.nextBoolean();

            taskToUpdate.setTitle(newTitle);
            taskToUpdate.setDescription(newDescription);
            taskToUpdate.setDeadline(newDeadline);
            taskToUpdate.setPriority(newPriority);
            taskToUpdate.setDone(newDone);

            updateTask(taskToUpdate);
            System.out.println("Task updated successfully.");
        } else {
            System.out.println("Task not found.");
        }
    }

    private static void deleteTask() throws SQLException {
        System.out.print("Enter the task ID to delete: ");
        int taskIdToDelete = scanner.nextInt();

        Task taskToDelete = findTaskById(taskIdToDelete);
        if (taskToDelete != null) {
            deleteTask(taskToDelete);
            System.out.println("Task deleted successfully.");
        } else {
            System.out.println("Task not found.");
        }
    }

    public static void insertTodo(int id, String title, String description, Timestamp deadline, int priority, boolean done) throws SQLException {
        String sql = "INSERT INTO todo (id, title, description, deadline, priority, done) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setString(2, title);
            statement.setString(3, description);
            statement.setTimestamp(4, deadline);
            statement.setInt(5, priority);
            statement.setBoolean(6, done);
            statement.executeUpdate();
        }
    }

    public static Task findTaskById(int taskId) throws SQLException {
        String sql = "SELECT * FROM todo WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, taskId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Task(
                            resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getString("description"),
                            resultSet.getTimestamp("deadline"),
                            resultSet.getInt("priority"),
                            resultSet.getBoolean("done")
                    );
                }
            }
        }
        return null;
    }

    public static List<Task> findAllTasks() throws SQLException {
        List<Task> taskList = new ArrayList<>();
        String sql = "SELECT * FROM todo";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Task task = new Task(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getTimestamp("deadline"),
                        resultSet.getInt("priority"),
                        resultSet.getBoolean("done")
                );
                taskList.add(task);
            }
        }
        return taskList;
    }

    public static void updateTask(Task task) throws SQLException {
        String sql = "UPDATE todo SET title = ?, description = ?, deadline = ?, priority = ?, done = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, task.getTitle());
            statement.setString(2, task.getDescription());
            statement.setTimestamp(3, task.getDeadline());
            statement.setInt(4, task.getPriority());
            statement.setBoolean(5, task.isDone());
            statement.setInt(6, task.getId());
            statement.executeUpdate();
        }
    }

    public static void deleteTask(Task task) throws SQLException {
        String sql = "DELETE FROM todo WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, task.getId());
            statement.executeUpdate();
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
