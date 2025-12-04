CREATE DATABASE danceCompetition;
USE danceCompetition;
DROP TABLE IF EXISTS Person, Dancer, Judge, Location, Event, Competition, Song,
    CompetesIn, Performance, JudgesPerformance;
CREATE Table Person (
                        PersonID INT UNSIGNED,
                        Name VARCHAR(30) NOT NULL,
                        Age INT UNSIGNED NOT NULL,
                        PRIMARY KEY (PersonID)
);
CREATE Table Dancer (
                        PersonID INT UNSIGNED,
                        Level VARCHAR(30) NOT NULL,
                        PRIMARY KEY (PersonID),
                        FOREIGN KEY (PersonID) REFERENCES Person(PersonID)
                            ON DELETE CASCADE
);
CREATE Table Judge (
                       PersonID INT UNSIGNED,
                       CompetitionsJudged INT UNSIGNED,
                       PRIMARY KEY (PersonID),
                       FOREIGN KEY (PersonID) REFERENCES Person(PersonID)
                           ON DELETE CASCADE
);
CREATE Table Location (
                          Address VARCHAR(50),
                          Capacity INT UNSIGNED NOT NULL,
                          PRIMARY KEY (Address)
);
CREATE Table Event (
                       EventID INT UNSIGNED,
                       Date DATE NOT NULL,
                       Price DECIMAL(6,2) NOT NULL,
                       Address VARCHAR(50) NOT NULL,
                       PRIMARY KEY (EventID),
                       FOREIGN KEY (Address) REFERENCES Location(Address)
);
CREATE Table Competition (
                             CompetitionID INT UNSIGNED,
                             Rounds INT UNSIGNED NOT NULL,
                             BallroomNumber INT UNSIGNED NOT NULL,
                             EventID INT UNSIGNED NOT NULL,
                             PRIMARY KEY (CompetitionID),
                             FOREIGN KEY (EventID) REFERENCES Event(EventID)
);
CREATE Table Song (
                      SongID INT UNSIGNED,
                      Title VARCHAR(50) NOT NULL,
                      Artist VARCHAR(30) NOT NULL,
                      PRIMARY KEY (SongID)
);
CREATE Table CompetesIn (
                            DancerID INT UNSIGNED,
                            CompetitionID INT UNSIGNED,
                            Ranking INT UNSIGNED,
                            PRIMARY KEY (DancerID, CompetitionID),
                            FOREIGN KEY (DancerID) REFERENCES Dancer(PersonID),
                            FOREIGN KEY (CompetitionID) REFERENCES Competition(CompetitionID)
);
CREATE Table Performance (
                             CompetitionID INT UNSIGNED,
                             DancerID INT UNSIGNED,
                             Round INT UNSIGNED,
                             SongID INT UNSIGNED,
                             PRIMARY KEY (DancerID, CompetitionID, Round, SongID),
                             FOREIGN KEY (DancerID) REFERENCES Dancer(PersonID),
                             FOREIGN KEY (CompetitionID) REFERENCES Competition(CompetitionID),
                             FOREIGN KEY (SongID) REFERENCES Song(SongID)
);
CREATE Table JudgesPerformance (
                                   CompetitionID INT UNSIGNED,
                                   DancerID INT UNSIGNED,
                                   Round INT UNSIGNED,
                                   SongID INT UNSIGNED,
                                   JudgeID INT UNSIGNED,
                                   Score DECIMAL (3,1),
                                   PRIMARY KEY (DancerID, CompetitionID, Round, SongID, JudgeID),
                                   FOREIGN KEY (DancerID) REFERENCES Dancer(PersonID),
                                   FOREIGN KEY (CompetitionID) REFERENCES Competition(CompetitionID),
                                   FOREIGN KEY (SongID) REFERENCES Song(SongID),
                                   FOREIGN KEY (JudgeID) REFERENCES Judge(PersonID)
);
-- Insert Persons (IDs 1-8: 5 dancers, 3 judges)
INSERT INTO Person (PersonID, Name, Age) VALUES
                                             (1, 'Emma Thompson', 22),
                                             (2, 'James Wilson', 25),
                                             (3, 'Sophia Martinez', 20),
                                             (4, 'Michael Chen', 28),
                                             (5, 'Olivia Brown', 23),
                                             (6, 'Victoria Smith', 45),
                                             (7, 'Robert Taylor', 52),
                                             (8, 'Patricia Anderson', 48);
-- Insert Dancers (PersonIDs 1-5)
INSERT INTO Dancer (PersonID, Level) VALUES
                                         (1, 'Advanced'),
                                         (2, 'Professional'),
                                         (3, 'Intermediate'),
                                         (4, 'Professional'),
                                         (5, 'Advanced');
-- Insert Judges (PersonIDs 6-8)
INSERT INTO Judge (PersonID, CompetitionsJudged) VALUES
                                                     (6, 25),
                                                     (7, 30),
                                                     (8, 18);
-- Insert Locations
INSERT INTO Location (Address, Capacity) VALUES
                                             ('123 Grand Ballroom Ave, New York, NY 10001', 500),
                                             ('456 Dance Hall Blvd, Los Angeles, CA 90001', 750),
                                             ('789 Competition Center, Chicago, IL 60601', 600),
                                             ('321 Performance Plaza, Miami, FL 33101', 400),
                                             ('654 Ballroom Drive, Las Vegas, NV 89101', 1000);
-- Insert Events
INSERT INTO Event (EventID, Date, Price, Address) VALUES
                                                      (1, '2024-01-15', 75.00, '123 Grand Ballroom Ave, New York, NY 10001'),
                                                      (2, '2024-02-20', 85.00, '456 Dance Hall Blvd, Los Angeles, CA 90001'),
                                                      (3, '2024-03-10', 70.00, '789 Competition Center, Chicago, IL 60601'),
                                                      (4, '2024-04-05', 90.00, '321 Performance Plaza, Miami, FL 33101'),
                                                      (5, '2024-05-12', 100.00, '654 Ballroom Drive, Las Vegas, NV 89101');
-- Insert Competitions
INSERT INTO Competition (CompetitionID, Rounds, BallroomNumber, EventID) VALUES
                                                                             (1, 3, 1, 1),
                                                                             (2, 4, 2, 1),
                                                                             (3, 3, 1, 2),
                                                                             (4, 4, 2, 3),
                                                                             (5, 3, 1, 4);
-- Insert Songs
INSERT INTO Song (SongID, Title, Artist) VALUES
                                             (1, 'Moonlight Serenade', 'Glenn Miller'),
                                             (2, 'Blue Danube', 'Johann Strauss II'),
                                             (3, 'Waltz of the Flowers', 'Pyotr Tchaikovsky'),
                                             (4, 'Por Una Cabeza', 'Carlos Gardel'),
                                             (5, 'Libertango', 'Astor Piazzolla');
-- Insert CompetesIn (Dancers competing in competitions with rankings)
INSERT INTO CompetesIn (DancerID, CompetitionID, Ranking) VALUES
                                                              (1, 1, 2),
                                                              (2, 1, 1),
                                                              (3, 1, 3),
                                                              (4, 2, 1),
                                                              (5, 2, 2),
                                                              (1, 3, 2),
                                                              (2, 3, 1),
                                                              (3, 4, 3),
                                                              (4, 4, 1),
                                                              (5, 5, 1);
-- Insert Performance (Dancers performing in competitions, rounds, with songs)
INSERT INTO Performance (CompetitionID, DancerID, Round, SongID) VALUES
-- Competition 1 (3 rounds)
(1, 1, 1, 1),
(1, 1, 2, 2),
(1, 1, 3, 3),
(1, 2, 1, 1),
(1, 2, 2, 4),
(1, 2, 3, 5),
(1, 3, 1, 2),
(1, 3, 2, 3),
-- Competition 2 (4 rounds)
(2, 4, 1, 1),
(2, 4, 2, 2),
(2, 4, 3, 3),
(2, 4, 4, 4),
(2, 5, 1, 1),
(2, 5, 2, 4),
(2, 5, 3, 5),
-- Competition 3 (3 rounds)
(3, 1, 1, 2),
(3, 1, 2, 3),
(3, 2, 1, 1),
(3, 2, 2, 4),
(3, 2, 3, 5),
-- Competition 4 (4 rounds)
(4, 3, 1, 1),
(4, 3, 2, 2),
(4, 4, 1, 1),
(4, 4, 2, 2),
(4, 4, 3, 3),
(4, 4, 4, 4),
-- Competition 5 (3 rounds)
(5, 5, 1, 1),
(5, 5, 2, 2),
(5, 5, 3, 3);
-- Insert JudgesPerformance (Judges scoring performances)
INSERT INTO JudgesPerformance (CompetitionID, DancerID, Round, SongID, JudgeID, Score)
VALUES
-- Competition 1 performances
(1, 1, 1, 1, 6, 8.5),
(1, 1, 1, 1, 7, 8.7),
(1, 1, 2, 2, 6, 9.0),
(1, 1, 2, 2, 8, 8.9),
(1, 1, 3, 3, 7, 8.6),
(1, 2, 1, 1, 6, 9.2),
(1, 2, 1, 1, 7, 9.3),
(1, 2, 2, 4, 6, 9.5),
(1, 2, 2, 4, 8, 9.4),
(1, 2, 3, 5, 7, 9.6),
(1, 3, 1, 2, 7, 7.8),
(1, 3, 2, 3, 8, 8.0),
-- Competition 2 performances
(2, 4, 1, 1, 6, 9.4),
(2, 4, 2, 2, 7, 9.5),
(2, 4, 3, 3, 8, 9.6),
(2, 4, 4, 4, 6, 9.8),
(2, 5, 1, 1, 7, 8.7),
(2, 5, 2, 4, 8, 8.8),
(2, 5, 3, 5, 6, 8.9),
-- Competition 3 performances
(3, 1, 1, 2, 7, 8.8),
(3, 1, 2, 3, 8, 8.9),
(3, 2, 1, 1, 6, 9.3),
(3, 2, 2, 4, 7, 9.4),
(3, 2, 3, 5, 8, 9.5),
-- Competition 4 performances
(4, 3, 1, 1, 6, 8.5),
(4, 3, 2, 2, 7, 8.6),
(4, 4, 1, 1, 8, 9.5),
(4, 4, 2, 2, 6, 9.6),
(4, 4, 3, 3, 7, 9.7),
(4, 4, 4, 4, 8, 9.8),
-- Competition 5 performances
(5, 5, 1, 1, 6, 9.2),
(5, 5, 2, 2, 7, 9.3),
(5, 5, 3, 3, 8, 9.4);

-- 1 join for 2 tables: Competition, Event
SELECT c.CompetitionID,
       c.Rounds,
       c.BallroomNumber,
       e.Date,
       e.Price,
       e.Address
FROM Competition AS c
         JOIN Event AS e
              ON e.EventID = c.EventID
ORDER BY c.CompetitionID;
-- 1 join for 3 tables: CompetesIn, Person, Competition
SELECT p.Name AS DancerInfo,
       ci.CompetitionID,
       ci.Ranking
FROM CompetesIn as ci
         JOIN Person AS p
              ON p.PersonID = ci.DancerID
         JOIN Competition AS c ON c.CompetitionID = ci.CompetitionID
ORDER BY ci.CompetitionID, ci.Ranking, p.Name;

-- Event update with WHERE clause
UPDATE Event
SET Price = ROUND(Price*1.10, 2)
WHERE Address LIKE '%Las Vegas%';
SELECT EventID, Price, Address
FROM Event
WHERE Address LIKE '%Las Vegas%';

-- SUBQUERY with at least two tables
SELECT p.Name AS DancerInfo
FROM Person AS p
         JOIN CompetesIn ci
              ON p.PersonID = ci.DancerID
         JOIN Competition c
              ON c.CompetitionID = ci.CompetitionID
         JOIN Event e
              ON e.EventID = c.EventID
WHERE e.Price = (
    SELECT MIN(Price)
    FROM Event
)
ORDER BY p.Name;

--TRIGGER
DELIMITER //
DROP TRIGGER IF EXISTS new_trig//
CREATE TRIGGER new_trig
    BEFORE INSERT ON JudgesPerformance
    FOR EACH ROW
BEGIN
    IF NEW.Score IS NULL THEN
SET NEW.Score = 0.0;
ELSEIF NEW.Score < 0.0 THEN
SET NEW.Score = 0.0;
ELSEIF NEW.Score > 10.0 THEN
SET NEW.Score = 10.0;
END IF;
END//
DELIMITER ;

--DML:
INSERT INTO JudgesPerformance
(CompetitionID, DancerID, Round, SongID, JudgeID, Score)
Values
    (1,1,1,1,8,15.0);
SELECT CompetitionID, DancerID, Round, SongID, JudgeID, Score
FROM JudgesPerformance
WHERE CompetitionID=1 AND DancerID=1 AND Round=1 AND SongID=1 AND JudgeID=8;
--expected result score over 10.0 will be reduced to 10.0
--Score below the minimum
INSERT INTO JudgesPerformance
(CompetitionID, DancerID, Round, SongID, JudgeID, Score)
VALUES
    (1, 1, 3, 3, 8, -5.0);
SELECT CompetitionID, DancerID, Round, SongID, JudgeID, Score
FROM JudgesPerformance
WHERE CompetitionID=1 AND DancerID=1 AND Round=3 AND SongID=3 AND JudgeID=8;

--Result - score should be saved as 0.0
CREATE Index new_index
    ON JudgesPerformance (CompetitionID, Round);
SELECT jp.CompetitionID, jp.Round, p.Name AS DanceInfo, AVG(jp.Score) AS AvgScore
FROM JudgesPerformance jp
         JOIN Person p
              ON p.PersonID = jp.DancerID
WHERE jp.CompetitionID = 4
  AND jp.Round = 3
GROUP BY jp.CompetitionID, jp.Round, p.Name
ORDER BY AvgScore DESC;

-- ============================================================================
-- REGISTRATION TABLE
-- Tracks dancer registrations for events
-- ============================================================================
CREATE TABLE IF NOT EXISTS Registration (
    RegistrationID INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    PersonID INT UNSIGNED NOT NULL,
    EventID INT UNSIGNED NOT NULL,
    RegistrationDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    Status VARCHAR(20) DEFAULT 'Confirmed',
    FOREIGN KEY (PersonID) REFERENCES Person(PersonID),
    FOREIGN KEY (EventID) REFERENCES Event(EventID),
    UNIQUE KEY unique_registration (PersonID, EventID)
);

-- ============================================================================
-- VIEW: EventLocationSummary
-- Combines Event and Location data for convenient reporting
-- Adds a computed PriceCategory column to classify events by price tier
-- ============================================================================
DROP VIEW IF EXISTS EventLocationSummary;

CREATE VIEW EventLocationSummary AS
SELECT 
    e.EventID,
    e.Date AS EventDate,
    e.Price,
    e.Address,
    l.Capacity,
    CASE 
        WHEN e.Price < 50 THEN 'Budget'
        WHEN e.Price BETWEEN 50 AND 100 THEN 'Standard'
        ELSE 'Premium'
    END AS PriceCategory
FROM Event e
LEFT JOIN Location l ON e.Address = l.Address;

-- Test the VIEW:
SELECT * FROM EventLocationSummary;

-- ============================================================================
-- STORED PROCEDURE: RegisterDancer
-- Registers a dancer for an event with business rule enforcement:
--   - Person must exist in the database
--   - Event must exist in the database
--   - Person must be at least 5 years old (minimum age for competition)
--   - Cannot register the same person twice for the same event
-- ============================================================================
DROP PROCEDURE IF EXISTS RegisterDancer;

DELIMITER //

CREATE PROCEDURE RegisterDancer(
    IN p_PersonID INT,
    IN p_EventID INT,
    OUT p_Result VARCHAR(100)
)
BEGIN
    DECLARE v_PersonExists INT DEFAULT 0;
    DECLARE v_EventExists INT DEFAULT 0;
    DECLARE v_PersonAge INT DEFAULT 0;
    DECLARE v_AlreadyRegistered INT DEFAULT 0;
    
    -- Check if person exists and get their age
    SELECT COUNT(*), COALESCE(MAX(Age), 0) 
    INTO v_PersonExists, v_PersonAge
    FROM Person 
    WHERE PersonID = p_PersonID;
    
    -- Check if event exists
    SELECT COUNT(*) INTO v_EventExists
    FROM Event 
    WHERE EventID = p_EventID;
    
    -- Check if already registered
    SELECT COUNT(*) INTO v_AlreadyRegistered
    FROM Registration 
    WHERE PersonID = p_PersonID AND EventID = p_EventID;
    
    -- Apply business rules
    IF v_PersonExists = 0 THEN
        SET p_Result = 'ERROR: Person does not exist';
    ELSEIF v_EventExists = 0 THEN
        SET p_Result = 'ERROR: Event does not exist';
    ELSEIF v_PersonAge < 5 THEN
        SET p_Result = 'ERROR: Dancer must be at least 5 years old';
    ELSEIF v_AlreadyRegistered > 0 THEN
        SET p_Result = 'ERROR: Person already registered for this event';
    ELSE
        INSERT INTO Registration (PersonID, EventID) 
        VALUES (p_PersonID, p_EventID);
        SET p_Result = 'SUCCESS: Dancer registered successfully!';
    END IF;
END //

DELIMITER ;

-- ============================================================================
-- CHECK CONSTRAINTS
-- Ensure data integrity at the database level
-- ============================================================================

-- Constraint: Person Age must be between 1 and 119
ALTER TABLE Person ADD CONSTRAINT chk_person_age 
    CHECK (Age > 0 AND Age < 120);

-- Constraint: Event Price must be positive
ALTER TABLE Event ADD CONSTRAINT chk_event_price 
    CHECK (Price > 0);

-- Constraint: Location Capacity must be positive
ALTER TABLE Location ADD CONSTRAINT chk_location_capacity 
    CHECK (Capacity > 0);

-- Constraint: Competition Rounds must be positive
ALTER TABLE Competition ADD CONSTRAINT chk_competition_rounds 
    CHECK (Rounds > 0);