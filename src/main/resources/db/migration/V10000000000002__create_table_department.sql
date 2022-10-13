DROP TABLE IF EXISTS Department;

CREATE TABLE Department
(
    department_id INT auto_increment PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    location      VARCHAR(255) NOT NULL,
    CONSTRAINT name_len CHECK (LENGTH(RTRIM(name)) > 1),
    CONSTRAINT location_len CHECK (LENGTH(RTRIM(location)) > 1),
    CONSTRAINT unique_combination UNIQUE(name, location)
);