import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/danceCompetition?useSSL=false&serverTimezone=UTC";
        String username = "root";
        String password = "";

        try {
            // Load driver (optional in modern Java but still fine)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to MySQL!");

            // Test query
            String sql = "SELECT CompetitionID, Rounds, BallroomNumber FROM Competition LIMIT 3";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("CompetitionID | Rounds | Ballroom");
            while (rs.next()) {
                System.out.printf("%13d | %6d | %9d%n",
                        rs.getInt("CompetitionID"),
                        rs.getInt("Rounds"),
                        rs.getInt("BallroomNumber"));
            }

            rs.close();
            ps.close();
            conn.close();
            System.out.println("Query done and connection closed.");
        }
        catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found: " + e.getMessage());
        }
        catch (SQLException e) {
            System.out.println("SQLState=" + e.getSQLState() + " Code=" + e.getErrorCode());
            System.out.println("Message=" + e.getMessage());
        }
    }
}
