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
    - Run `sql/create_and_populate.sql` (execute entire script).

2. **Local config**
    - Copy the sample file and edit it with your own MySQL user/password:
      ```
      cp app.properties.example app.properties
      ```
    - `app.properties` should *not* be committed (see .gitignore).

3. **Run**
    - Open the project in IntelliJ.
    - Ensure `lib/mysql-connector-j-*.jar` is **Add as Library…** (Project Library).
    - Open `src/Main.java` and click the green ▶️ next to `main`.


## Notes
- Keep credentials out of Git. Use `app.properties` locally (ignored by git).
- If connection fails, verify MySQL is running and the schema/tables exist.

