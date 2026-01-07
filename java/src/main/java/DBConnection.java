import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() throws Exception {
        String url = System.getenv("JDBC_URL");
        String user = System.getenv("USERNAME");
        String password = System.getenv("PASSWORD");
        return DriverManager.getConnection(url, user, password);
    }
}