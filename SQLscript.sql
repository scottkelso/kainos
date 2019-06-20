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
    salary decimal(10,2),
    department enum('sales', 'finance', 'hr', 'developer')
);

ALTER TABLE employee
ADD CONSTRAINT chk_salary
	CHECK (salary > 0);

INSERT INTO employee (name, address, NiN, iBan, bic, salary, department)
VALUES ("Joe smith", "31, linkin avenue, Belfast, BT21 7HN", "BC 13 14 19 c", "1234567890qwertyuiopasdf", "NAIAGB21", 25000.00, "sales"),
	("Fred boy", "somewhere", "QE132445c", "asdfhjklwqeurihj123", "MDLNDGB34", 30000.00, "developer"),
    ("john pizza", "15, Dublin Road, Belfast, BT20 3SH", "BH 236890 k", "ahajskdendksjjg", "PQJBLG15", 15000.00, "finance"),
    ("loius person", "32 place, very placey, location, BT19 UHN", "SJ 13 25 78c", "ashdjilsahdja12365ytg", "GLAGB01", 100000.00, "hr");

-- sales employee table
CREATE TABLE salesEmployee (
	employeeID int unsigned PRIMARY KEY,
    commissionRate decimal (2,2),
    salesTotal decimal (10,2),
    FOREIGN KEY (`employeeID`)
		references employee(`employeeID`)
);

ALTER TABLE salesEmployee
ADD CONSTRAINT chk_sales
	CHECK (salesTotal >= 0);
    
INSERT INTO salesEmployee(employeeID, commissionRate, salesTotal)
VALUES (1, 0.30, 60000.00); 
-- project table


-- POSSIBLE: department table


-- possible finance table


-- PROCEDURES

DELIMITER //
CREATE PROCEDURE employeesPerDepartment()
BEGIN
	SELECT department, COUNT(*) as employeeCount
    FROM employee 
    GROUP BY department;
END //

CREATE PROCEDURE highestTotalSales ()
BEGIN
	SELECT e.employeeID, e.name
    FROM employee e JOIN salesEmployee se
    ON e.employeeID = se.employeeID
    ORDER BY se.salesTotal desc
    LIMIT 1;
END //

CREATE PROCEDURE employeesInSales()
BEGIN
	SELECT e.employeeID
    FROM employee e
    WHERE department = "sales";
END //

DELIMITER ;
-- DROP USER IF EXISTS 'admin'@'localhost';
-- create user 'admin'@'localhost' identified with mysql_native_password by '****';
-- grant all on TPS_database.* to admin@localhost;
