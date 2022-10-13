package com.endava.webapp.Repository;

import com.endava.webapp.Entity.Department;
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
public class DepartmentRepositoryTest {
    @Autowired
    private DepartmentRepository departmentRepository;
    private Department department1, department2, department3;

    @Autowired
    Flyway flyway;

    @BeforeEach
    void setup() {
        department1 = new Department("N1", "L1");
        department2 = new Department("N2", "L2");
        department3 = new Department("N3", "L3");

        flyway.clean();
        flyway.migrate();
    }

    @Test
    void getAllMethodWithDataInDB_shouldReturnListOfDepartments() {
        department1.setEmployeeSet(Collections.emptySet());
        department2.setEmployeeSet(Collections.emptySet());
        department3.setEmployeeSet(Collections.emptySet());

        departmentRepository.save(department1);
        departmentRepository.save(department2);
        departmentRepository.save(department3);

        List<Department> expected = Arrays.asList(department1, department2, department3);

        assertThat(expected.toString()).isEqualTo(departmentRepository.getAll().toString());
    }

    @Test
    void getAllMethodWithNoDataInDB_shouldReturnEmptyList() {

        assertThat(Collections.emptyList()).isEqualTo(departmentRepository.getAll());
    }

    @Test
    void addMethodWithValidDepartment_shouldReturnThisDepartment() {
        assertAll(
                () -> assertNotNull(departmentRepository.save(department1)),
                () -> assertThat(department1.getLocation()).isEqualTo(departmentRepository.getAll().get(0).getLocation())
        );

    }

    @Test
    void getByIdMethodWithDataInDB_shouldReturnDepartmentWithThisId() {
        departmentRepository.save(department2);
        assertThat(department2.getName()).isEqualTo(departmentRepository.getById(1L).getName());
    }

    @Test
    void getByIdMethodWithNoDataInDB_shouldReturnNull() {
        assertNull(departmentRepository.getById(1L));
    }

    @Test
    void editMethodWithValidDepartment_shouldReturnChangedDepartment() {
        departmentRepository.save(department3);
        department3.setName("New Name");
        department3.setLocation("New Location");
        Department employeeDB = departmentRepository.editDepartment(1L, department3);
        assertAll(
                () -> assertThat(department3.getName()).isEqualTo(employeeDB.getName()),
                () -> assertThat(department3.getLocation()).isEqualTo(employeeDB.getLocation()),
                () -> assertThat(department3.getName()).isEqualTo("New Name")
        );
    }

    @Test
    void editMethodWithInvalidDepartment_shouldReturnNull() {
        assertNull(departmentRepository.editDepartment(1L, department3));
    }
}
