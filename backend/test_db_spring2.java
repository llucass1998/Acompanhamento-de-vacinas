import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class test_db_spring2 {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/vacina_kids";
        String user = "vacina_user";
        String password = "senha_local";
        
        System.out.println("Connecting to " + url + " with user " + user);
        
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection successful!");
            conn.close();
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }
}
