package com.endava.webapp.Exception;

public class EmployeeNotFoundException extends NotFoundException {
    public EmployeeNotFoundException(Long id) {
        super("Employee with id " + id + " not found");
    }
}