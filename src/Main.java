import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.Properties;


public class Main {
    public static void main(String[] args) {
//        validate if app.properties works
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("app.properties")) {
            props.load(fis);
        } catch (IOException e) {
            System.out.println("Error: could not load app.properties file.");
            return;
        }
//      add your own credentials in app.properties and this as the url: jdbc:mysql://localhost:3306/danceCompetition;
        String url = props.getProperty("url");
        String username = props.getProperty("user");
        String password = props.getProperty("password");
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to MySQL!");

            runMenu(conn);
        }  catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLState=" + e.getSQLState() +
                    " Code=" + e.getErrorCode());
            System.out.println("Message=" + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.out.println("Error closing connection.");
            }
        }
    }


    private static void runMenu(Connection conn) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("*** WELCOME TO THE DANCE COMPETITION SYSTEM ***:");
            System.out.println("1. View Data");
            System.out.println("2. Insert Data");
            System.out.println("3. Update Data");
            System.out.println("4. Delete Data");
            System.out.println("5. Transaction Workflow");
            System.out.println("6. Exit");
            System.out.println("*************************************************");
            System.out.println("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewMenu(conn, scanner);
                    break;
                case "2":
                    insertMenu(conn, scanner);
                    break;
                case "3":
                    updateMenu(conn, scanner);
                    break;
                case "4":
                    deleteMenu(conn, scanner);
                    break;
                case "5":
                    transactionWorkflow(conn, scanner);
                    break;
                case "6":
                    System.out.println("Exiting... Bye!");
                    return;
                default:
                    System.out.println("Wrong choice! Try again...");
            }
        }
    }


    // sub-menus
    private static void viewMenu(Connection conn, Scanner scanner) {
        System.out.println("*** VIEW DATA *** ");
        System.out.println("1. View Competitions");
        System.out.println("2. View Persons");
        System.out.println("3. Back");
        System.out.println("4. Choose: ");

        String c = scanner.nextLine();

        switch (c){
        case "1":
            viewCompetitions(conn);
            break;
        case "2":
            viewPersons(conn);
            break;
        case "3":
            return;
        default:
            System.out.println("Wrong choice! Try again...");
    }
}

private static void insertMenu(Connection conn, Scanner scanner) {
    System.out.println("*** INSERT DATA *** ");
    System.out.println("1.Insert Person");
    System.out.println("2.Insert Event");
    System.out.println("3.Back");

    String c = scanner.nextLine();
    switch (c) {
        case "1":
            insertPerson(conn, scanner);
            break;
        case "2":
            insertEvent(conn, scanner);
            break;
        case "3":
            return;
        default:
            System.out.println("Invalid option.");
    }
}

private static void updateMenu(Connection conn, Scanner scanner) {
    System.out.println("*** UPDATE DATA *** ");
    System.out.println("1.Update Person");
    System.out.println("2.Back");

    String c = scanner.nextLine();

    switch(c) {
        case "1":
            updatePersonAge(conn, scanner);
            break;
        case "2":
            return;
        default:
            System.out.println("Invalid option.");
    }
}

private static void deleteMenu(Connection conn, Scanner scanner) {
    System.out.println("*** DELETE DATA *** ");
    System.out.println("1. Delete Person");
    System.out.println("2. Back");

    String c = scanner.nextLine();
    switch (c) {
        case "1":
            deletePerson(conn, scanner);
            break;
        case "2":
            return;
        default:
            System.out.println("Invalid option.");
    }
}

//methods
private static void viewCompetitions(Connection conn) {
    System.out.println("\n*** VIEW COMPETITIONS *** ");
    String sql = "SELECT CompetitionID, Rounds, BallroomNumber FROM Competition";
    try(PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
        System.out.println("ID | Rounds | Ballroom");
        System.out.println("---------------------------");

        while (rs.next()) {
            System.out.printf("%2d | %6d | %8d%n",
                    rs.getInt("CompetitionID"),
                    rs.getInt("Rounds"),
                    rs.getInt("BallroomNumber"));
        }
        System.out.println("-------------------------------------");
    } catch (SQLException e) {
        System.out.println("Error retrieving competitions." + e.getMessage());
    }

}

    private static void viewPersons(Connection conn) {
        System.out.println("\n*** VIEW PERSONS *** ");
        String sql = "SELECT PersonID, Name, Age FROM Person ORDER BY PersonID";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("ID | Name                 | Age");
            System.out.println("---------------------------------------");

            while (rs.next()) {
                System.out.printf("%2d | %-20s | %3d%n",
                        rs.getInt("PersonID"),
                        rs.getString("Name"),
                        rs.getInt("Age"));
            }
        System.out.println("-------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error retrieving persons:");
            System.out.println(e.getMessage());
        }

    }

    private static void insertPerson(Connection conn, Scanner scanner) {
        System.out.println("\n(Insert Person - will implement)");
    }

    private static void insertEvent(Connection conn, Scanner scanner) {
        System.out.println("\n(Insert Event - will implement)");
    }

    private static void updatePersonAge(Connection conn, Scanner scanner) {
        System.out.println("\n(Update Person Age - will implement)");
    }

    private static void deletePerson(Connection conn, Scanner scanner) {
        System.out.println("\n(Delete Person - will implement)");
    }

    private static void transactionWorkflow(Connection conn, Scanner scanner) {
        System.out.println("\n(Transaction Workflow - will implement)");
    }
}





            // Test query
//            String sql = "SELECT CompetitionID, Rounds, BallroomNumber FROM Competition LIMIT 3";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ResultSet rs = ps.executeQuery();
//
//            System.out.println("CompetitionID | Rounds | Ballroom");
//            while (rs.next()) {
//                System.out.printf("%13d | %6d | %9d%n",
//                        rs.getInt("CompetitionID"),
//                        rs.getInt("Rounds"),
//                        rs.getInt("BallroomNumber"));
//            }
//
//            rs.close();
//            ps.close();
//            conn.close();
//            System.out.println("Query done and connection closed.");
//        }
//        catch (ClassNotFoundException e) {
//            System.out.println("MySQL Driver not found: " + e.getMessage());
//        }
//        catch (SQLException e) {
//            System.out.println("SQLState=" + e.getSQLState() + " Code=" + e.getErrorCode());
//            System.out.println("Message=" + e.getMessage());
//        }
//    }
//}
