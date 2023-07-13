package TodoList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionToDataBase {
    private String url;
    private String user;
    private String password;

    public ConnectionToDataBase() {
        this.url = "jdbc:postgresql://localhost:5432/"+Setting.DATABASE_NAME;
        this.user = Setting.USER;
        this.password = Setting.PASSWORD;
    }

    public Connection createConnection(){
        try{
            Connection connection = DriverManager.getConnection(
                    this.url, this.user, this.password
            );
            return connection;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
