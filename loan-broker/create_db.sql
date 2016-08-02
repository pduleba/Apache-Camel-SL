DROP USER IF EXISTS 'demo'@'localhost' ;
DROP DATABASE IF EXISTS demo;

CREATE USER 'demo'@'localhost' IDENTIFIED BY 'demo' PASSWORD EXPIRE NEVER;
CREATE DATABASE demo;
GRANT ALL ON demo.* TO 'demo'@'localhost';

CREATE TABLE `demo`.`houseinfo` (
  `nationalID` VARCHAR(10) NOT NULL,
  `address` VARCHAR(200) NULL,
  `bedroom` VARCHAR(100) NULL,
  `bathroom` VARCHAR(100) NULL,
  `landSize` INT(15) NULL,
  `appraisedValue` INT(15) NULL,
  PRIMARY KEY (`nationalID`));

CREATE TABLE `demo`.`custinfo` (
  `nationalID` VARCHAR(10) NOT NULL,
  `firstName` VARCHAR(200) NULL,
  `lastName` VARCHAR(100) NULL,
  `age` INT(15) NULL,
  `occupation` VARCHAR(100) NULL,
  PRIMARY KEY (`nationalID`));


