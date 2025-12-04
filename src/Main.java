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
            System.out.println("6. Database Features (View, Stored Procedure, Constraints)");
            System.out.println("7. Exit");
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
                    databaseFeaturesMenu(conn, scanner);
                    break;
                case "7":
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

    // ==================== DATABASE FEATURES MENU ====================
    private static void databaseFeaturesMenu(Connection conn, Scanner scanner) {
        System.out.println("\n*** DATABASE FEATURES ***");
        System.out.println("1. Setup Database Features (Create View, Stored Procedure, Constraints)");
        System.out.println("2. View Event-Location Summary (VIEW Demo)");
        System.out.println("3. Register Dancer for Event (Stored Procedure Demo)");
        System.out.println("4. Test Constraints (Constraint Demo)");
        System.out.println("5. View Registered Dancers");
        System.out.println("6. Back");
        System.out.println("Choose: ");

        String c = scanner.nextLine();

        switch (c) {
            case "1":
                setupDatabaseFeatures(conn);
                break;
            case "2":
                viewEventLocationSummary(conn, scanner);
                break;
            case "3":
                callRegisterDancerProcedure(conn, scanner);
                break;
            case "4":
                testConstraints(conn, scanner);
                break;
            case "5":
                viewRegisteredDancers(conn, scanner);
                break;
            case "6":
                return;
            default:
                System.out.println("Invalid option.");
        }
    }

    // Setup all database features: VIEW, STORED PROCEDURE, and CONSTRAINTS
    private static void setupDatabaseFeatures(Connection conn) {
        System.out.println("\n*** SETTING UP DATABASE FEATURES ***\n");
        
        try (Statement stmt = conn.createStatement()) {
            
            // ==================== 1. CREATE VIEW ====================
            System.out.println("1. Creating VIEW: EventLocationSummary...");
            
            // Drop view if exists
            try {
                stmt.execute("DROP VIEW IF EXISTS EventLocationSummary");
            } catch (SQLException e) {
                // Ignore if view doesn't exist
            }
            
            // Create the VIEW - joins Event and Location for reporting
            String createViewSQL = 
                "CREATE VIEW EventLocationSummary AS " +
                "SELECT e.EventID, e.Date AS EventDate, e.Price, " +
                "       e.Address, l.Capacity, " +
                "       CASE " +
                "           WHEN e.Price < 50 THEN 'Budget' " +
                "           WHEN e.Price BETWEEN 50 AND 100 THEN 'Standard' " +
                "           ELSE 'Premium' " +
                "       END AS PriceCategory " +
                "FROM Event e " +
                "LEFT JOIN Location l ON e.Address = l.Address";
            
            stmt.execute(createViewSQL);
            System.out.println("   VIEW 'EventLocationSummary' created successfully!");
            System.out.println("   This VIEW combines Event and Location data for easy reporting.\n");
            
            // ==================== 2. CREATE REGISTRATION TABLE ====================
            System.out.println("2. Creating Registration table (if not exists)...");
            
            String createRegistrationTable = 
                "CREATE TABLE IF NOT EXISTS Registration (" +
                "    RegistrationID INT UNSIGNED AUTO_INCREMENT PRIMARY KEY, " +
                "    PersonID INT UNSIGNED NOT NULL, " +
                "    EventID INT UNSIGNED NOT NULL, " +
                "    RegistrationDate DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "    Status VARCHAR(20) DEFAULT 'Confirmed', " +
                "    FOREIGN KEY (PersonID) REFERENCES Person(PersonID), " +
                "    FOREIGN KEY (EventID) REFERENCES Event(EventID), " +
                "    UNIQUE KEY unique_registration (PersonID, EventID)" +
                ")";
            
            stmt.execute(createRegistrationTable);
            System.out.println("   Registration table ready!\n");
            
            // ==================== 3. CREATE STORED PROCEDURE ====================
            System.out.println("3. Creating STORED PROCEDURE: RegisterDancer...");
            
            // Drop procedure if exists
            try {
                stmt.execute("DROP PROCEDURE IF EXISTS RegisterDancer");
            } catch (SQLException e) {
                // Ignore
            }
            
            // Create stored procedure that enforces business rules:
            // - Person must exist
            // - Event must exist
            // - Person must be at least 5 years old to register
            // - Cannot register twice for the same event
            String createProcedureSQL = 
                "CREATE PROCEDURE RegisterDancer(" +
                "    IN p_PersonID INT, " +
                "    IN p_EventID INT, " +
                "    OUT p_Result VARCHAR(100)" +
                ") " +
                "BEGIN " +
                "    DECLARE v_PersonExists INT DEFAULT 0; " +
                "    DECLARE v_EventExists INT DEFAULT 0; " +
                "    DECLARE v_PersonAge INT DEFAULT 0; " +
                "    DECLARE v_AlreadyRegistered INT DEFAULT 0; " +
                "    " +
                "    SELECT COUNT(*), COALESCE(MAX(Age), 0) INTO v_PersonExists, v_PersonAge " +
                "    FROM Person WHERE PersonID = p_PersonID; " +
                "    " +
                "    SELECT COUNT(*) INTO v_EventExists " +
                "    FROM Event WHERE EventID = p_EventID; " +
                "    " +
                "    SELECT COUNT(*) INTO v_AlreadyRegistered " +
                "    FROM Registration WHERE PersonID = p_PersonID AND EventID = p_EventID; " +
                "    " +
                "    IF v_PersonExists = 0 THEN " +
                "        SET p_Result = 'ERROR: Person does not exist'; " +
                "    ELSEIF v_EventExists = 0 THEN " +
                "        SET p_Result = 'ERROR: Event does not exist'; " +
                "    ELSEIF v_PersonAge < 5 THEN " +
                "        SET p_Result = 'ERROR: Dancer must be at least 5 years old'; " +
                "    ELSEIF v_AlreadyRegistered > 0 THEN " +
                "        SET p_Result = 'ERROR: Person already registered for this event'; " +
                "    ELSE " +
                "        INSERT INTO Registration (PersonID, EventID) VALUES (p_PersonID, p_EventID); " +
                "        SET p_Result = 'SUCCESS: Dancer registered successfully!'; " +
                "    END IF; " +
                "END";
            
            stmt.execute(createProcedureSQL);
            System.out.println("   STORED PROCEDURE 'RegisterDancer' created successfully!");
            System.out.println("   This procedure enforces rules:");
            System.out.println("   - Person must exist in database");
            System.out.println("   - Event must exist in database");
            System.out.println("   - Dancer must be at least 5 years old");
            System.out.println("   - Cannot register twice for the same event\n");
            
            // ==================== 4. ADD CHECK CONSTRAINTS ====================
            System.out.println("4. Adding CHECK CONSTRAINTS...");
            
            // Add constraint for Person Age (must be positive)
            try {
                stmt.execute("ALTER TABLE Person ADD CONSTRAINT chk_person_age CHECK (Age > 0 AND Age < 120)");
                System.out.println("   Added constraint: Person.Age must be between 1 and 119");
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate") || e.getMessage().contains("already exists")) {
                    System.out.println("   Constraint chk_person_age already exists - skipping");
                } else {
                    System.out.println("   Note: " + e.getMessage());
                }
            }
            
            // Add constraint for Event Price (must be positive)
            try {
                stmt.execute("ALTER TABLE Event ADD CONSTRAINT chk_event_price CHECK (Price > 0)");
                System.out.println("   Added constraint: Event.Price must be positive");
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate") || e.getMessage().contains("already exists")) {
                    System.out.println("   Constraint chk_event_price already exists - skipping");
                } else {
                    System.out.println("   Note: " + e.getMessage());
                }
            }
            
            // Add constraint for Location Capacity (must be positive)
            try {
                stmt.execute("ALTER TABLE Location ADD CONSTRAINT chk_location_capacity CHECK (Capacity > 0)");
                System.out.println("   Added constraint: Location.Capacity must be positive");
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate") || e.getMessage().contains("already exists")) {
                    System.out.println("   Constraint chk_location_capacity already exists - skipping");
                } else {
                    System.out.println("   Note: " + e.getMessage());
                }
            }
            
            // Add constraint for Competition Rounds (must be positive)
            try {
                stmt.execute("ALTER TABLE Competition ADD CONSTRAINT chk_competition_rounds CHECK (Rounds > 0)");
                System.out.println("   Added constraint: Competition.Rounds must be positive");
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate") || e.getMessage().contains("already exists")) {
                    System.out.println("   Constraint chk_competition_rounds already exists - skipping");
                } else {
                    System.out.println("   Note: " + e.getMessage());
                }
            }
            
            System.out.println("\n*** ALL DATABASE FEATURES SETUP COMPLETE! ***");
            System.out.println("You can now test the VIEW, STORED PROCEDURE, and CONSTRAINTS from the menu.\n");
            
        } catch (SQLException e) {
            System.out.println("Error setting up database features: " + e.getMessage());
        }
    }

    // Demo: Query the VIEW
    private static void viewEventLocationSummary(Connection conn, Scanner scanner) {
        System.out.println("\n*** EVENT-LOCATION SUMMARY (VIEW Demo) ***");
        System.out.println("This data comes from the 'EventLocationSummary' VIEW\n");
        
        String sql = "SELECT * FROM EventLocationSummary ORDER BY EventID";
        
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            System.out.println("EventID | Date       | Price   | Address                    | Capacity | Category");
            System.out.println("--------------------------------------------------------------------------------------");
            
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                int capacity = rs.getInt("Capacity");
                String capacityStr = rs.wasNull() ? "N/A" : String.valueOf(capacity);
                
                System.out.printf("%7d | %-10s | $%6.2f | %-26s | %8s | %s%n",
                        rs.getInt("EventID"),
                        rs.getDate("EventDate"),
                        rs.getDouble("Price"),
                        rs.getString("Address"),
                        capacityStr,
                        rs.getString("PriceCategory"));
            }
            
            if (!hasData) {
                System.out.println("No events found. Add some events first!");
            }
            
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Make sure to run 'Setup Database Features' first (Option 1)");
        }
    }

    // Demo: Call the STORED PROCEDURE
    private static void callRegisterDancerProcedure(Connection conn, Scanner scanner) {
        System.out.println("\n*** REGISTER DANCER (Stored Procedure Demo) ***");
        System.out.println("This uses the 'RegisterDancer' stored procedure\n");
        
        try {
            // Show available persons
            System.out.println("Available Persons:");
            String personSQL = "SELECT PersonID, Name, Age FROM Person ORDER BY PersonID";
            try (PreparedStatement ps = conn.prepareStatement(personSQL);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("  ID: %d - %s (Age: %d)%n",
                            rs.getInt("PersonID"),
                            rs.getString("Name"),
                            rs.getInt("Age"));
                }
            }
            
            // Show available events
            System.out.println("\nAvailable Events:");
            String eventSQL = "SELECT EventID, Date, Address FROM Event ORDER BY EventID";
            try (PreparedStatement ps = conn.prepareStatement(eventSQL);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("  ID: %d - %s at %s%n",
                            rs.getInt("EventID"),
                            rs.getDate("Date"),
                            rs.getString("Address"));
                }
            }
            
            System.out.println("\nEnter Person ID to register: ");
            int personID;
            try {
                personID = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID format.");
                return;
            }
            
            System.out.println("Enter Event ID to register for: ");
            int eventID;
            try {
                eventID = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID format.");
                return;
            }
            
            // Call the stored procedure
            String callSQL = "{CALL RegisterDancer(?, ?, ?)}";
            try (CallableStatement cs = conn.prepareCall(callSQL)) {
                cs.setInt(1, personID);
                cs.setInt(2, eventID);
                cs.registerOutParameter(3, java.sql.Types.VARCHAR);
                
                cs.execute();
                
                String result = cs.getString(3);
                System.out.println("\n*** PROCEDURE RESULT: " + result + " ***");
            }
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Make sure to run 'Setup Database Features' first (Option 1)");
        }
    }

    // Demo: Test CHECK CONSTRAINTS
    private static void testConstraints(Connection conn, Scanner scanner) {
        System.out.println("\n*** TEST CONSTRAINTS Demo ***");
        System.out.println("This demonstrates that CHECK constraints prevent invalid data.\n");
        
        System.out.println("Choose a constraint test:");
        System.out.println("1. Try to insert Person with invalid age (negative or zero)");
        System.out.println("2. Try to insert Event with invalid price (negative or zero)");
        System.out.println("3. Try to insert Person with age > 119 (too old)");
        System.out.println("4. Back");
        
        String choice = scanner.nextLine();
        
        switch (choice) {
            case "1":
                testInvalidAge(conn, scanner);
                break;
            case "2":
                testInvalidPrice(conn, scanner);
                break;
            case "3":
                testTooOldAge(conn, scanner);
                break;
            case "4":
                return;
            default:
                System.out.println("Invalid option.");
        }
    }
    
    private static void testInvalidAge(Connection conn, Scanner scanner) {
        System.out.println("\n*** Testing: Insert Person with Age = -5 ***");
        System.out.println("This should FAIL due to CHECK constraint (Age > 0)\n");
        
        String sql = "INSERT INTO Person (PersonID, Name, Age) VALUES (9999, 'Test Invalid Age', -5)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
            System.out.println("ERROR: Insert succeeded! Constraint may not be active.");
            
            // Clean up
            conn.createStatement().execute("DELETE FROM Person WHERE PersonID = 9999");
        } catch (SQLException e) {
            System.out.println("SUCCESS! Constraint working correctly!");
            System.out.println("The database rejected the invalid data:");
            System.out.println("  -> " + e.getMessage());
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private static void testInvalidPrice(Connection conn, Scanner scanner) {
        System.out.println("\n*** Testing: Insert Event with Price = -100 ***");
        System.out.println("This should FAIL due to CHECK constraint (Price > 0)\n");
        
        String sql = "INSERT INTO Event (EventID, Date, Price, Address) VALUES (9999, '2025-01-01', -100, 'Test Address')";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
            System.out.println("ERROR: Insert succeeded! Constraint may not be active.");
            
            // Clean up
            conn.createStatement().execute("DELETE FROM Event WHERE EventID = 9999");
        } catch (SQLException e) {
            System.out.println("SUCCESS! Constraint working correctly!");
            System.out.println("The database rejected the invalid data:");
            System.out.println("  -> " + e.getMessage());
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private static void testTooOldAge(Connection conn, Scanner scanner) {
        System.out.println("\n*** Testing: Insert Person with Age = 150 ***");
        System.out.println("This should FAIL due to CHECK constraint (Age < 120)\n");
        
        String sql = "INSERT INTO Person (PersonID, Name, Age) VALUES (9998, 'Test Too Old', 150)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
            System.out.println("ERROR: Insert succeeded! Constraint may not be active.");
            
            // Clean up
            conn.createStatement().execute("DELETE FROM Person WHERE PersonID = 9998");
        } catch (SQLException e) {
            System.out.println("SUCCESS! Constraint working correctly!");
            System.out.println("The database rejected the invalid data:");
            System.out.println("  -> " + e.getMessage());
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    // View registered dancers
    private static void viewRegisteredDancers(Connection conn, Scanner scanner) {
        System.out.println("\n*** REGISTERED DANCERS ***");
        
        String sql = "SELECT r.RegistrationID, p.Name, e.Date AS EventDate, e.Address, r.RegistrationDate, r.Status " +
                     "FROM Registration r " +
                     "JOIN Person p ON r.PersonID = p.PersonID " +
                     "JOIN Event e ON r.EventID = e.EventID " +
                     "ORDER BY r.RegistrationDate DESC";
        
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            System.out.println("RegID | Dancer Name          | Event Date | Location               | Registered On       | Status");
            System.out.println("----------------------------------------------------------------------------------------------------");
            
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.printf("%5d | %-20s | %-10s | %-22s | %-19s | %s%n",
                        rs.getInt("RegistrationID"),
                        rs.getString("Name"),
                        rs.getDate("EventDate"),
                        rs.getString("Address"),
                        rs.getTimestamp("RegistrationDate"),
                        rs.getString("Status"));
            }
            
            if (!hasData) {
                System.out.println("No registrations yet. Use option 3 to register dancers!");
            }
            
            System.out.println("----------------------------------------------------------------------------------------------------");
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Make sure to run 'Setup Database Features' first (Option 1)");
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
