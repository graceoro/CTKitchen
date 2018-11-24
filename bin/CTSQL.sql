DROP DATABASE IF EXISTS CutthroatKitchen;
CREATE DATABASE CutthroatKitchen;
USE CutthroatKitchen;
CREATE TABLE Users (
	username VARCHAR(45) PRIMARY KEY NOT NULL,
    password_ VARCHAR(45) NOT NULL,
    characterID INT(11) NOT NULL
);

CREATE TABLE Scoreboard (
	scoreID INT(11)  PRIMARY KEY NOT NULL AUTO_INCREMENT,
    username VARCHAR(45) NOT NULL,
    score INT(11) NOT NULL,
    
    FOREIGN KEY fk(username) REFERENCES Users(username)
);

