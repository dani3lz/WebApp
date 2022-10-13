package com.endava.webapp.Service;

import com.endava.webapp.DTO.EmployeeDTO;
import com.endava.webapp.Entity.Employee;
import com.endava.webapp.Exception.EmployeeNotFoundException;
import com.endava.webapp.Repository.DepartmentRepository;
import com.endava.webapp.Repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.endava.webapp.DTO.Convertor.toDTO;
import static com.endava.webapp.DTO.Convertor.toEntity;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<EmployeeDTO> getAll() {
        List<EmployeeDTO> employeeList = new ArrayList<>();
        employeeRepository.getAll().forEach(employee -> employeeList.add(toDTO(employee)));
        return employeeList;
    }

    public EmployeeDTO getById(Long id) {
        Optional<Employee> employee = Optional.ofNullable(employeeRepository.getById(id));
        if (employee.isPresent()) {
            return toDTO(employee.get());
        }
        throw new EmployeeNotFoundException(id);
    }

    public EmployeeDTO addEmployee(EmployeeDTO employee) {
        return toDTO(Optional.of(employeeRepository.save(toEntity(employee, departmentRepository))).get());
    }

    public EmployeeDTO editEmployee(Long id, EmployeeDTO employee) {
        Optional<Employee> employeeResult = Optional.ofNullable(employeeRepository.editEmployee(id, toEntity(employee, departmentRepository)));
        if (employeeResult.isPresent()) {
            return toDTO(employeeResult.get());
        }
        throw new EmployeeNotFoundException(id);
    }


}
