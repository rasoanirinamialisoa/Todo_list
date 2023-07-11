import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserCRUD {
    private static final Connection connection;
    private static PreparedStatement statement;

    static {
        ConnectionToDatabase db = new ConnectionToDatabase();
        connection = db.createConnection();
        try {
            statement = connection.prepareStatement("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // insert a user
    public static void insertUser(int id, String name, String email, String password) {
        try {
            String sql = "INSERT INTO \"user\" (id, name, email, password) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setString(3, email);
            statement.setString(4, password);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // find a single user by its id
    public static User findUserById(int id) {
        try {
            String sql = "SELECT * FROM \"user\" WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // find all users
    public static List<User> findAllUsers() {
        List<User> userList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM \"user\"";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    // update a user
    public static void updateUser(int id, String newName, String newEmail, String newPassword) {
        try {
            String sql = "UPDATE \"user\" SET name = ?, email = ?, password = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newName);
            statement.setString(2, newEmail);
            statement.setString(3, newPassword);
            statement.setInt(4, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // delete a user using its ID
    public static void deleteUser(int id) {
        try {
            String sql = "DELETE FROM \"user\" WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}