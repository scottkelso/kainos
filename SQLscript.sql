DROP DATABASE IF EXISTS TPS_database;
CREATE DATABASE TPS_database;
USE TPS_database;

-- employee
CREATE TABLE employee (
	employeeID int unsigned PRIMARY KEY auto_increment,
    name varchar(40) NOT NULL,
    address varchar(60) NOT NULL,
    NiN varchar(13) unique,
    iBan varchar(34) unique,
    bic varchar(11),
    salary decimal(10,2) unsigned,
    department enum('sales', 'finance', 'hr', 'developer')
);

INSERT INTO employee (name, address, NiN, iBan, bic, salary, department)
VALUES ("Joe smith", "31, linkin avenue, Belfast, BT21 7HN", "BC 13 14 19 c", "1234567890qwertyuiopasdf", "NAIAGB21", 25000.00, "sales"),
	("Fred boy", "somewhere", "QE132445c", "asdfhjklwqeurihj123", "MDLNDGB34", 30000.00, "developer"),
    ("john pizza", "15, Dublin Road, Belfast, BT20 3SH", "BH 236890 k", "ahajskdendksjjg", "PQJBLG15", 15000.00, "finance"),
    ("loius person", "32 place, very placey, location, BT19 UHN", "SJ 13 25 78c", "ashdjilsahdja12365ytg", "GLAGB01", 100000.00, "hr");

-- sales employee table
CREATE TABLE salesEmployee (
	employeeID int unsigned PRIMARY KEY,
    commissionRate decimal (2,2),
    salesTotal decimal (10,2) unsigned,
    FOREIGN KEY (`employeeID`)
		references employee(`employeeID`)
);
    
INSERT INTO salesEmployee(employeeID, commissionRate, salesTotal)
VALUES (1, 0.30, 60000.00); 

-- project table
CREATE TABLE project (
	projectID INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    projectName varchar(30) NOT NULL,
    projectManager INT UNSIGNED,
    FOREIGN KEY (`projectManager`)
		REFERENCES employee(`employeeID`)
);

INSERT INTO project (projectName, projectManager)
VALUES ("The first project", 3),
		("The second project", 2);

CREATE TABLE employee_project (
	employeeID INT UNSIGNED,
    projectID INT UNSIGNED,
    startDate DATETIME DEFAULT NOW(),
    PRIMARY KEY (`employeeID`, `projectID`),
    FOREIGN KEY (`employeeID`)
		REFERENCES employee(`employeeID`),
	FOREIGN KEY (`projectID`)
		REFERENCES project(`projectID`)
);

INSERT INTO employee_project (employeeID, projectID)
VALUES (3, 2),
		(4, 1),
        (1, 2),
        (3, 1);

CREATE TABLE previous_employee_project (
	employeeID INT unsigned,
    projectID INT unsigned,
    startDate DATETIME,
	finishDate DATETIME DEFAULT NOW(),
    PRIMARY KEY(`employeeID`, `projectID`, `startDate`),
    FOREIGN KEY(`employeeID`)
		REFERENCES employee(`employeeID`),
	FOREIGN KEY(`projectID`)
		REFERENCES project(`projectID`)
);



-- POSSIBLE: department table


-- possible finance table


-- PROCEDURES

DELIMITER //
CREATE PROCEDURE employeesPerDepartment()
BEGIN
	SELECT department, COUNT(*) as employeeCount
    FROM employee 
    GROUP BY department;
END; //

CREATE PROCEDURE highestTotalSales ()
BEGIN
	SELECT e.employeeID, e.name
    FROM employee e JOIN salesEmployee se
    ON e.employeeID = se.employeeID
    ORDER BY se.salesTotal desc
    LIMIT 1;
END; //

CREATE PROCEDURE employeesInSales()
BEGIN
	SELECT e.employeeID
    FROM employee e
    WHERE department = "sales";
END; //

CREATE TRIGGER Tr_Delete_Employee_Project
	BEFORE DELETE ON `employee_project` FOR each row
BEGIN
	INSERT INTO `previous_employee_project`(EmployeeID, ProjectID, StartDate, FinishDate)
    VALUES (OLD.employeeID, OLD.projectID, OLD.startDate, Default);
END; //

DELIMITER ;

DELETE FROM employee_project
WHERE employeeID = 4 and projectID = 1;

DELETE FROM employee_project
WHERE employeeID = 3 and projectID = 2;

-- DROP USER IF EXISTS 'admin'@'localhost';
-- create user 'admin'@'localhost' identified with mysql_native_password by '****';
-- grant all on TPS_database.* to admin@localhost;
