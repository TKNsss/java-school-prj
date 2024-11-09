CREATE TABLE Employees (
    em_id CHAR(5) PRIMARY KEY NOT NULL,
    status VARCHAR(15) NULL,
    first_name NVARCHAR(30) NULL,
    last_name NVARCHAR(30) NULL,
    gender VARCHAR(10) NULL,
    phone CHAR(10) NULL,
    dob DATE NULL,
    address NVARCHAR(25) NULL,
    position VARCHAR(20) NULL,
    salary DECIMAL(10, 2) DEFAULT 0,
);