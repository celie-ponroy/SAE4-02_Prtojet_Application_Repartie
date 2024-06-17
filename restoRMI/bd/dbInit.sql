CREATE TABLE RESTAURANT (
    numResto INT PRIMARY KEY AUTO_INCREMENT,
    nomResto VARCHAR(64) NOT NULL,
    adresse VARCHAR(255) NOT NULL,
    nbPlaces INT NOT NULL,
    xGPS DOUBLE NOT NULL,
    yGPS DOUBLE NOT NULL
);

CREATE TABLE RESERVATION (
    id INT PRIMARY KEY AUTO_INCREMENT,
    numResto INT NOT NULL,
    nomClient VARCHAR(64) NOT NULL,
    prenomClient VARCHAR(64) NOT NULL,
    nbConvives INT NOT NULL,
    numTel VARCHAR(16) NOT NULL,
    dateRes DATE NOT NULL,
    FOREIGN KEY (numResto) REFERENCES RESTAURANT(numResto)
);