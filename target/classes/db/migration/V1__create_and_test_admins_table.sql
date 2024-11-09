CREATE TABLE Admins (
    ad_id INT IDENTITY(1, 1) PRIMARY KEY NOT NULL,
    ad_name VARCHAR(25) NOT NULL,
    password VARCHAR(25) NOT NULL
);

INSERT INTO Admins(ad_name, password)
VALUES ('admin', 'admin123');

SELECT * FROM Admins;