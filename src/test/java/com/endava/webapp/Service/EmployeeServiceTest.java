package com.endava.webapp.Service;

import com.endava.webapp.DTO.EmployeeDTO;
import com.endava.webapp.Entity.Department;
import com.endava.webapp.Entity.Employee;
import com.endava.webapp.Exception.EmployeeNotFoundException;
import com.endava.webapp.Repository.DepartmentRepository;
import com.endava.webapp.Repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    DepartmentRepository departmentRepository;
    @InjectMocks
    EmployeeService employeeService;

    private Employee employee1, employee2, employee3;
    private EmployeeDTO employeeDTO1, employeeDTO2, employeeDTO3;
    Department department;
    private List<Employee> employeeList;
    private List<EmployeeDTO> employeeDTOList;

    @BeforeEach
    void setup() {
        department = new Department("NTest", "LTest");
        department.setId(1L);

        employee1 = new Employee(1L,"FN1", "LN1", department, "M1", 1, 5.1);
        employee2 = new Employee(2L,"FN2", "LN2", department, "M2", 2, 15.2);
        employee3 = new Employee(3L,"FN3", "LN3", department, "M3", 3, 115.3);

        employeeDTO1 = new EmployeeDTO(1L, "FN1", "LN1", 1L, "M1", 1, 5.1);
        employeeDTO2 = new EmployeeDTO(2L, "FN2", "LN2", 1L, "M2", 2, 15.2);
        employeeDTO3 = new EmployeeDTO(3L, "FN3", "LN3", 1L, "M3", 3, 115.3);

        employeeDTOList = Arrays.asList(employeeDTO1, employeeDTO2, employeeDTO3);
        employeeList = Arrays.asList(employee1, employee2, employee3);
    }

    @Test
    void getAllEmployees_shouldReturnAllEmployees() {
        when(employeeRepository.getAll()).thenReturn(employeeList);
        List<EmployeeDTO> expectedList = employeeService.getAll();
        int number = new Random().nextInt(3);
        EmployeeDTO expectedRandomEmployee = expectedList.get(number);
        EmployeeDTO randomEmployee = employeeDTOList.get(number);
        assertAll(
                () -> assertThat(expectedRandomEmployee.getFirstName()).isEqualTo(randomEmployee.getFirstName()),
                () -> assertThat(expectedRandomEmployee.getLastName()).isEqualTo(randomEmployee.getLastName()),
                () -> assertThat(expectedRandomEmployee.getEmail()).isEqualTo(randomEmployee.getEmail()),
                () -> assertThat(expectedRandomEmployee.getSalary()).isEqualTo(randomEmployee.getSalary())
        );
        verify(employeeRepository).getAll();
    }

    @Test
    void getAllEmployeesFromEmptyTable_shouldReturnEmptyList() {
        when(employeeRepository.getAll()).thenReturn(Collections.emptyList());
        List<EmployeeDTO> expectedList = employeeService.getAll();
        assertThat(expectedList).isEqualTo(Collections.emptyList());
        verify(employeeRepository).getAll();
    }

    @Test
    void getAnEmployeeByID_shouldReturnTheEmployee() {
        when(employeeRepository.getById(1L)).thenReturn(employee1);
        EmployeeDTO expectedEmployee = employeeService.getById(1L);
        assertAll(
                () -> assertThat(expectedEmployee.getFirstName()).isEqualTo(employee1.getFirstName()),
                () -> assertThat(expectedEmployee.getLastName()).isEqualTo(employee1.getLastName()),
                () -> assertThat(expectedEmployee.getEmail()).isEqualTo(employee1.getEmail()),
                () -> assertThat(expectedEmployee.getSalary()).isEqualTo(employee1.getSalary())
        );
        verify(employeeRepository).getById(1L);
    }

    @Test
    void getAnEmployeeByWrongID_shouldThrowAnException() {
        when(employeeRepository.getById(5L)).thenReturn(null);
        final EmployeeNotFoundException employeeNotFoundException = new EmployeeNotFoundException(5L);
        assertAll(
                () -> assertThrows(employeeNotFoundException.getClass(), () -> employeeService.getById(5L)),
                () -> assertEquals("Employee with id " + 5L + " not found", employeeNotFoundException.getMessage())
        );
        verify(employeeRepository).getById(5L);
    }

    @Test
    void addNewEmployeeWithValidBody_shouldReturnTheEmployee() {
        when(departmentRepository.getById(1L)).thenReturn(department);
        when(employeeRepository.save(any())).thenReturn(employee1);
        EmployeeDTO expectedEmployee = employeeService.addEmployee(employeeDTO1);
        assertThat(expectedEmployee.getPhoneNumber()).isEqualTo(employeeDTO1.getPhoneNumber());
        verify(employeeRepository).save(any());
    }

    @Test
    void updateAnExistingEmployee_shouldReturnNewEmployeeBody() {
        when(departmentRepository.getById(1L)).thenReturn(department);
        when(employeeRepository.editEmployee(anyLong(), any())).thenReturn(employee2);

        EmployeeDTO employeeDTO = employeeService.editEmployee(1L, employeeDTO2);

        assertAll(
                () -> assertThat(employee2.getFirstName()).isEqualTo(employeeDTO.getFirstName()),
                () -> assertThat(employee2.getLastName()).isEqualTo(employeeDTO.getLastName()),
                () -> assertThat(employee2.getDepartment().getId()).isEqualTo(employeeDTO.getDepartmentId()),
                () -> assertThat(employee2.getEmail()).isEqualTo(employeeDTO.getEmail()),
                () -> assertThat(employee2.getSalary()).isEqualTo(employeeDTO.getSalary())
        );
    }
}
