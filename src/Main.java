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
        } catch (ClassNotFoundException e) {
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
            System.out.println("\n*** WELCOME TO THE DANCE COMPETITION SYSTEM ***:");
            System.out.println("1. View Data");
            System.out.println("2. Insert Data");
            System.out.println("3. Update Data");
            System.out.println("4. Delete Data");
            System.out.println("5. Transaction (Insert Locations and Events)");
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
        System.out.println("3. View Events");
        System.out.println("4. Back");
        System.out.println("5. Choose: ");

        String c = scanner.nextLine();

        switch (c) {
            case "1":
                viewCompetitions(conn, scanner);
                break;
            case "2":
                viewPersons(conn, scanner);
                break;
            case "3":
                viewEvents(conn, scanner);
                break;
            case "4":
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
        System.out.println("1. Delete Event");
        System.out.println("2. Back");

        String c = scanner.nextLine();
        switch (c) {
            case "1":
                deleteEvent(conn, scanner);
                break;
            case "2":
                return;
            default:
                System.out.println("Invalid option.");
        }
    }

    //methods
    //view methods
    private static void viewCompetitions(Connection conn, Scanner scanner) {
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
            System.out.println("Enter to return to Menu:");

            scanner.nextLine(); //waits for any input

        } catch (SQLException e) {
            System.out.println("Error retrieving competitions." + e.getMessage());
        }

    }

    private static void viewPersons(Connection conn, Scanner scanner) {
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
            System.out.println("Enter to return to Menu:");

            scanner.nextLine(); //waits for any input

        } catch (SQLException e) {
            System.out.println("Error retrieving persons:");
            System.out.println(e.getMessage());
        }

    }

    private static void viewEvents(Connection conn, Scanner scanner) {
        System.out.println("\n*** VIEW EVENTS *** ");
        String sql = "SELECT EventID, Date, Price, Address FROM Event ORDER BY EventID";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("ID | Date       | Price  | Address");
            System.out.println("---------------------------------------");

            while (rs.next()) {
                System.out.printf("%2d | %-10s | %-6.2f | %-50s%n",
                        rs.getInt("EventID"),
                        rs.getDate("Date"),
                        rs.getDouble("Price"),
                        rs.getString("Address"));
            }
            System.out.println("-------------------------------------");
            System.out.println("Enter to return to Menu:");

            scanner.nextLine(); //waits for any input

        } catch (SQLException e) {
            System.out.println("Error retrieving persons:");
            System.out.println(e.getMessage());
        }

    }

    //insert methods
    private static void insertPerson(Connection conn, Scanner scanner) {
        System.out.println("\n*** INSERT PERSON *** ");

        try {
            System.out.println("*** Enter Person ID: *** ");
            String idStr = scanner.nextLine();

            int personID;
            try {
                personID = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID - must be a number...");
                return;
            }

            System.out.println("Enter Name: ");
            String name = scanner.nextLine();
            if (name.isEmpty()) {
                System.out.println("Cannot be empty");
                return;
            }

            System.out.println("Enter Age: ");
            String ageStr = scanner.nextLine();
            int age;
            try {
                age = Integer.parseInt(ageStr);
                if (age <= 0) {
                    System.out.println("Age must be positive.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid age. Must be a number.");
                return;
            }

            String sql = "INSERT INTO Person (PersonID, Name, Age) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, personID);
            ps.setString(2, name);
            ps.setInt(3,age);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Successfully inserted into person table.");
            } else {
                System.out.println("Failed to insert into person table.");
            }
            ps.close();

        } catch (SQLException e) {
            System.out.println("\nError inserting persons:");
            System.out.println(e.getMessage());
        }
    }

    private static void insertEvent(Connection conn, Scanner scanner) {
        System.out.println("\n*** INSERT EVENTS *** ");

        try {
            System.out.println("Enter Event ID: ");
            String idStr = scanner.nextLine();
            int eventID;
            try {
                eventID = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID - must be a number...");
                return;
            }
            System.out.println("Enter Date (YYYY-MM-DD): ");
            String date = scanner.nextLine().trim();
            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                System.out.println("Invalid date format.");
                return;
            }

            System.out.println("Enter Price (number): ");
            String priceStr = scanner.nextLine();
            double price;
            try {
                price = Double.parseDouble(priceStr);
                if (price <= 0) {System.out.println("Price must be positive.");
                    return;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid price format.");
                return ;
            }
            System.out.println("Enter Address: ");
            String address = scanner.nextLine();
            if (address.isEmpty()) {
                System.out.println("Address cannot be empty...");
                return;
            }
            String sql = "INSERT INTO Event (EventID, Date, Price, Address) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, eventID);
            ps.setString(2, date);
            ps.setDouble(3, price);
            ps.setString(4, address);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Successfully inserted into event table.");

            } else {
                System.out.println("Failed to insert into event table.");
            }
            ps.close();
            } catch (SQLException e) {
            System.out.println("\nError inserting event: " + e.getMessage());
        }
    }

    //update methods
    private static void updatePersonAge(Connection conn, Scanner scanner) {
        System.out.println("\n*** UPDATE PERSON AGE *** ");
        try {
            System.out.println("Enter Person ID to update: ");
            String idStr = scanner.nextLine();

            int personID;
            try {
                personID = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID - must be a number...");
                return;
            }
            System.out.println("Enter Age to update: ");
            String ageStr = scanner.nextLine();
            int newAge;
            try {
                newAge = Integer.parseInt(ageStr);
                if (newAge <= 0) {
                    System.out.println("Age must be positive.");
                    return;
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid age format.");
                return;
            }

            String sql = "UPDATE Person SET Age = ? WHERE PersonID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, newAge);
            ps.setInt(2, personID);

            int rows = ps.executeUpdate();
            ps.close();

            if(rows == 0) {
                System.out.println("SNo person found with ID: " + personID);
            } else {
                System.out.println("Age updated!");
            }


        } catch (SQLException e) {
            System.out.println("\nERROR updating person: " + e.getMessage());
        }
    }

    //delete methods
    private static void deleteEvent(Connection conn, Scanner scanner) {
        System.out.println("\n*** DELETE EVENTS *** ");

        try {
            System.out.println("Enter Event ID to delete: ");
            String idStr = scanner.nextLine();
            int eventID;
            try {
                eventID = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID - must be a number...");
                return;
            }
            String sql = "DELETE FROM Event WHERE EventID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, eventID);
            int rows = ps.executeUpdate();
            ps.close();
            if (rows == 0){
                System.out.println("No event found with ID: " + eventID);
            } else {
                System.out.println("Event deleted!");
            }

        } catch (SQLException e) {
            System.out.println("\nERROR deleting event: " + e.getMessage());
        }

    }

    //transaction method
    private static void transactionWorkflow(Connection conn, Scanner scanner) {

        System.out.println("\n*** INSERT LOCATIONS AND EVENTS *** ");
        try {

            conn.setAutoCommit(false);

            try {
                //location
                System.out.println("Enter Address: ");
                String address = scanner.nextLine();
                if (address.isEmpty()) {
                    System.out.println("Address cannot be empty...");
                    return;
                }
                System.out.println("Enter Capacity: ");
                String capacityStr = scanner.nextLine();
                int capacity;
                try {
                    capacity = Integer.parseInt(capacityStr);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid ID - must be a number...");
                    return;
                }

                //event
                System.out.println("Enter Event ID: ");
                String idStr = scanner.nextLine();
                int eventID;
                try {
                    eventID = Integer.parseInt(idStr);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid ID - must be a number...");
                    return;
                }
                System.out.println("Enter Date (YYYY-MM-DD): ");
                String date = scanner.nextLine().trim();
                if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    System.out.println("Invalid date format.");
                    return;
                }

                System.out.println("Enter Price (number): ");
                String priceStr = scanner.nextLine();
                double price;
                try {
                    price = Double.parseDouble(priceStr);
                    if (price <= 0) {System.out.println("Price must be positive.");
                        return;
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Invalid price format.");
                    return ;
                }
                String locationSQL = "INSERT INTO Location (Address, Capacity) VALUES (?, ?)";
                PreparedStatement locationPS = conn.prepareStatement(locationSQL);
                locationPS.setString(1, address);
                locationPS.setInt(2, capacity);

                int locationRows = locationPS.executeUpdate();
                if (locationRows > 0) { //succeeded location insert, continue to event insert
                    String eventSQL = "INSERT INTO Event (EventID, Date, Price, Address) VALUES (?, ?, ?, ?)";
                    PreparedStatement eventPS = conn.prepareStatement(eventSQL);
                    eventPS.setInt(1, eventID);
                    eventPS.setString(2, date);
                    eventPS.setDouble(3, price);
                    eventPS.setString(4, address);
                    int eventRows = eventPS.executeUpdate();
                    if (eventRows > 0) {
                        System.out.println("Successfully inserted into location and event table.");

                    } else { //rollback when event insert fails
                        System.out.println("Failed to insert into event table.");
                        conn.rollback();
                    }
                    eventPS.close();

                } else { //rollback when location insert fails
                    System.out.println("Failed to insert into event table.");
                    conn.rollback();
                }
                locationPS.close();
                conn.commit();
            } catch (SQLException e) { //rollback when insert throws an exception
                conn.rollback();
                System.out.println("\nTransaction rolled back: " + e.getMessage());
            }

            conn.setAutoCommit(true);

        } catch (SQLException e) { //for conn.AutoCommit and conn.rollback
            System.out.println("\nERROR running transaction: " + e.getMessage());
        }
    }
}
