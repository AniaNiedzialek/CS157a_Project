# CS157A — Dance Competition 

Simple Java console app that connects to a MySQL database using JDBC and PreparedStatements.

## Prerequisites
- Java JDK 17+ (IntelliJ configured with this JDK)
- MySQL Server + MySQL Workbench
- MySQL Connector/J jar in `lib/` (non-Maven project)


## Setup

1. **Database**
    - Open MySQL Workbench:
      ```sql
      CREATE DATABASE IF NOT EXISTS danceCompetition;
      USE danceCompetition;
      ```
    - Run the SQL code in workbench and make sure it compiles correctly.

2. **Local config**
   This step is to ensure credential security when collaborating on coding part.
    - Copy the sample file and edit it with your own MySQL user/password:
      ```
      cp app.properties.example app.properties
      ```
    - `app.properties` should *not* be committed (see .gitignore).
    - What to put in the file?
url=jdbc:mysql://localhost:3306/danceCompetition
user=root
password= // your password to workbench!
    - save the file and you should be able to run Main.java now!

4. **Run**
    - Open the project in IntelliJ.
    - Ensure `lib/mysql-connector-j-*.jar` is **Add as Library…** (Project Library).
    - Open `src/Main.java` and click the green arrow next to `main`. You should be able to have a working connection!

## Notes
- Keep credentials out of Git. Use `app.properties` locally (ignored by git).
- If connection fails, verify MySQL is running and the schema/tables exist.

