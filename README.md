# CS157A — Dance Competition 

Simple Java console app that connects to a MySQL database using JDBC and PreparedStatements.

## Prerequisites
- Java JDK 17+ (IntelliJ configured with this JDK)
- MySQL Server + MySQL Workbench
- MySQL Connector/J jar in `lib/` (non-Maven project)

## Project Structure
```
CS157a_Project/
├── src/
│   └── Main.java                 # Main application
├── create_and_populate.sql       # Database schema & sample data
├── lib/
│   └── mysql-connector-j-*.jar   # MySQL JDBC driver
├── app.properties                # DB credentials
└── README.md
```

## Setup

1. **Database**
    - Open MySQL Workbench and run the SQL script located at:
      ```
      create_and_populate.sql
      ```
    - This script will:
      - Create the `danceCompetition` database
      - Create all required tables (Person, Dancer, Judge, Location, Event, Competition, Song, etc.)
      - Insert sample data
      - Create a trigger for score validation

2. **Database Credentials**
    - The `app.properties` file contains the database connection settings:
      ```
      url=jdbc:mysql://localhost:3306/danceCompetition
      user=root
      password=admin
      ```
    - Update the password if your MySQL setup uses a different one.

3. **Run**
    - Open the project in IntelliJ.
    - Ensure `lib/mysql-connector-j-*.jar` is **Add as Library…** (Project Library).
    - Open `src/Main.java` and click the green arrow next to `main`. You should be able to have a working connection!

## Features

The application includes:
- **CRUD Operations**: View, Insert, Update, Delete data
- **Transactions**: Insert Location and Event atomically
- **Database Features** (Option 6):
  - **VIEW**: `EventLocationSummary` - combines Event and Location data for reporting
  - **Stored Procedure**: `RegisterDancer` - registers dancers with business rule validation
  - **CHECK Constraints**: Validates Age, Price, Capacity, and Rounds are positive

## Notes
- If connection fails, verify MySQL is running and the schema/tables exist.
- The VIEW, Stored Procedure, and Constraints are created automatically when running `create_and_populate.sql`.

