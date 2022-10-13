package com.endava.webapp.DTO;

import com.endava.webapp.Entity.Department;
import com.endava.webapp.Entity.Employee;
import com.endava.webapp.Exception.DepartmentNotFoundException;
import com.endava.webapp.Repository.DepartmentRepository;

import java.util.Optional;

public class Convertor {
    public static Department toEntity(final DepartmentDTO departmentDTO) {
        return new Department(departmentDTO.getId(),
                departmentDTO.getName(),
                departmentDTO.getLocation(),
                departmentDTO.getEmployeeSet());
    }

    public static DepartmentDTO toDTO(final Department department) {
        return new DepartmentDTO(department.getId(),
                department.getName(),
                department.getLocation(),
                department.getEmployeeSet());
    }

    public static Employee toEntity(final EmployeeDTO employeeDTO, final DepartmentRepository departmentRepository) {
        Department department = Optional.of(departmentRepository.getById(employeeDTO.getDepartmentId()))
                .orElseThrow(() -> new DepartmentNotFoundException(employeeDTO.getDepartmentId()));
        return new Employee(employeeDTO.getId(),
                employeeDTO.getFirstName(),
                employeeDTO.getLastName(),
                department,
                employeeDTO.getEmail(),
                employeeDTO.getPhoneNumber(),
                employeeDTO.getSalary());
    }

    public static EmployeeDTO toDTO(final Employee employee) {
        return new EmployeeDTO(employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getDepartment().getId(),
                employee.getEmail(), employee.getPhoneNumber(),
                employee.getSalary());
    }
}
