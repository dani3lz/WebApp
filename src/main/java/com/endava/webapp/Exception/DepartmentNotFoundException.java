package com.endava.webapp.Exception;

public class DepartmentNotFoundException extends NotFoundException {
    public DepartmentNotFoundException(Long id) {
        super("Department with id " + id + " not found");
    }
}
