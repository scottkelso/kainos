DROP DATABSE IF EXISTAS TPS_database;
CREATE DATABASE TPS_database;
USE TPS_database;

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

--DROP USER IF EXISTS 'admin'@'localhost';
--create user 'admin'@'localhost' identified with mysql_native_password by '****';
--grant all on TPS_database.* to admin@localhost;

-- sales employee table

-- project table


-- POSSIBLE: department table



