package com.endava.webapp.Repository;

import com.endava.webapp.Entity.Employee;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository employeeRepository;
    private Employee employee1, employee2, employee3;

    @Autowired
    Flyway flyway;

    @BeforeEach
    void setup() {
        employee1 = new Employee("FN1", "LN1", null, "M1", 1, 5.1);
        employee2 = new Employee("FN2", "LN2", null, "M2", 2, 15.2);
        employee3 = new Employee("FN3", "LN3", null, "M3", 3, 115.3);

        flyway.clean();
        flyway.migrate();
    }

    @Test
    void getAllMethodWithDataInDB_shouldReturnListOfEmployees() {
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);

        List<Employee> expected = Arrays.asList(employee1, employee2, employee3);

        assertThat(expected.toString()).isEqualTo(employeeRepository.getAll().toString());
    }

    @Test
    void getAllMethodWithNoDataInDB_shouldReturnEmptyList() {

        assertThat(Collections.emptyList()).isEqualTo(employeeRepository.getAll());
    }

    @Test
    void addMethodWithValidEmployee_shouldReturnThisEmployee() {
        assertAll(
                () -> assertNotNull(employeeRepository.save(employee1)),
                () -> assertThat(employee1.getFirstName()).isEqualTo(employeeRepository.getAll().get(0).getFirstName())
        );

    }

    @Test
    void addMethodWithInvalidEmployee_shouldReturnNull() {
        Employee invalidEmployee = new Employee("F", "L", null, "M", 1, 0.5);

        assertNull(employeeRepository.save(invalidEmployee));
    }

    @Test
    void getByIdMethodWithDataInDB_shouldReturnEmployeeWithThisId() {
        employeeRepository.save(employee2);
        assertThat(employee2.getSalary()).isEqualTo(employeeRepository.getById(1L).getSalary());
    }

    @Test
    void getByIdMethodWithNoDataInDB_shouldReturnNull() {
        assertNull(employeeRepository.getById(1L));
    }

    @Test
    void editMethodWithValidEmployee_shouldReturnChangedEmployee() {
        employeeRepository.save(employee3);
        employee3.setFirstName("FEdit");
        employee3.setLastName("LEdit");
        Employee employeeDB = employeeRepository.editEmployee(1L, employee3);
        assertAll(
                () -> assertThat(employee3.getLastName()).isEqualTo(employeeDB.getLastName()),
                () -> assertThat(employee3.getFirstName()).isEqualTo(employeeDB.getFirstName())
        );
    }

    @Test
    void editMethodWithInvalidEmployee_shouldReturnNull() {
        assertNull(employeeRepository.editEmployee(1L, employee3));
    }
}
