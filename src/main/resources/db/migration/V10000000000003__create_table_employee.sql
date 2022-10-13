DROP TABLE IF EXISTS Employee;

CREATE TABLE Employee
(
    employee_id   INT auto_increment PRIMARY KEY,
    first_name    VARCHAR(255) NOT NULL,
    last_name     VARCHAR(255) NOT NULL,
    department_id LONG,
    email         VARCHAR(255) NOT NULL UNIQUE,
    phone_number  NUMBER       NOT NULL UNIQUE,
    salary        DOUBLE       NOT NULL,
    CONSTRAINT employment_salary_check CHECK (salary >= 1.0),
    CONSTRAINT first_name_len CHECK (LENGTH(RTRIM(first_name)) > 0),
    CONSTRAINT last_name_len CHECK (LENGTH(RTRIM(last_name)) > 0),
    CONSTRAINT email_len CHECK (LENGTH(RTRIM(email)) > 0),
    FOREIGN KEY (department_id) REFERENCES Department (department_id)
);