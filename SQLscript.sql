CREATE DATABASE TPS_database;

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

-- sales employee table

-- project table


-- POSSIBLE: department table



