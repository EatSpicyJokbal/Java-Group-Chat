import java.sql.*;

public class SqlManager {

    private static Connection conn;
    
    private static final String url = "jdbc:mysql://localhost:3306/groupchatsystem";
    private static final String user = "root";
    private static final String password = "";

    public static void sqlUsers(String username) {
        try {
            conn = DriverManager.getConnection(url, user, password);
            String sql = "INSERT INTO list_users (username) VALUES (?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
