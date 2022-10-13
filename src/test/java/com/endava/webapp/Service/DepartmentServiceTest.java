package com.endava.webapp.Service;

import com.endava.webapp.DTO.DepartmentDTO;
import com.endava.webapp.Entity.Department;
import com.endava.webapp.Exception.DepartmentNotFoundException;
import com.endava.webapp.Repository.DepartmentRepository;
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
class DepartmentServiceTest {
    @Mock
    DepartmentRepository departmentRepository;

    @InjectMocks
    DepartmentService departmentService;


    private Department department1, department2, department3;
    private DepartmentDTO departmentDTO1, departmentDTO2, departmentDTO3;
    private List<Department> departmentList;
    private List<DepartmentDTO> departmentDTOList;

    @BeforeEach
    void setup() {
        department1 = new Department("N1", "L1");
        department1.setId(1L);
        department2 = new Department("N2", "L2");
        department2.setId(2L);
        department3 = new Department("N3", "L3");
        department3.setId(3L);

        departmentDTO1 = new DepartmentDTO(1L, "N1", "L1");
        departmentDTO2 = new DepartmentDTO(2L, "N2", "L2");
        departmentDTO3 = new DepartmentDTO(3L, "N3", "L3");

        departmentList = Arrays.asList(department1, department2, department3);
        departmentDTOList = Arrays.asList(departmentDTO1, departmentDTO2, departmentDTO3);
    }

    @Test
    void getAllDepartments_shouldReturnAllDepartments() {
        when(departmentRepository.getAll()).thenReturn(departmentList);
        List<DepartmentDTO> expectedList = departmentService.getAll();
        System.out.println(expectedList);
        System.out.println(departmentList);
        int number = new Random().nextInt(3);
        DepartmentDTO expectedRandomDepartment = expectedList.get(number);
        DepartmentDTO randomDepartment = departmentDTOList.get(number);
        assertAll(
                () -> assertThat(expectedRandomDepartment.getName()).isEqualTo(randomDepartment.getName()),
                () -> assertThat(expectedRandomDepartment.getLocation()).isEqualTo(randomDepartment.getLocation())
        );
        verify(departmentRepository).getAll();
    }

    @Test
    void getAllDepartmentsFromEmptyTable_shouldReturnAnEmptyList() {
        when(departmentRepository.getAll()).thenReturn(Collections.emptyList());
        List<DepartmentDTO> expectedList = departmentService.getAll();
        assertThat(expectedList).isEqualTo(Collections.emptyList());
        verify(departmentRepository).getAll();
    }

    @Test
    void getAnDepartmentByID_shouldReturnTheDepartment() {
        when(departmentRepository.getById(1L)).thenReturn(department1);
        DepartmentDTO expectedEmployee = departmentService.getById(1L);
        assertAll(
                () -> assertThat(expectedEmployee.getName()).isEqualTo(department1.getName()),
                () -> assertThat(expectedEmployee.getLocation()).isEqualTo(department1.getLocation())
        );
        verify(departmentRepository).getById(1L);
    }

    @Test
    void getAnDepartmentByWrongID_shouldThrowAnException() {
        when(departmentRepository.getById(5L)).thenReturn(null);
        final DepartmentNotFoundException departmentNotFoundException = new DepartmentNotFoundException(5L);
        assertAll(
                () -> assertThrows(departmentNotFoundException.getClass(), () -> departmentService.getById(5L)),
                () -> assertEquals("Department with id " + 5L + " not found", departmentNotFoundException.getMessage())
        );
        verify(departmentRepository).getById(5L);
    }

    @Test
    void addNewDepartmentWithValidBody_shouldReturnTheDepartment() {
        when(departmentRepository.save(any())).thenReturn(department1);
        DepartmentDTO expectedDepartment = departmentService.addDepartment(departmentDTO1);
        assertThat(expectedDepartment.getName()).isEqualTo(departmentDTO1.getName());
        verify(departmentRepository).save(any());
    }

    @Test
    void updateAnExistingEmployee_shouldReturnNewEmployeeBody() {
        when(departmentRepository.editDepartment(anyLong(), any())).thenReturn(department2);

        DepartmentDTO departmentDTO = departmentService.editDepartment(1L, departmentDTO2);

        assertAll(
                () -> assertThat(department2.getName()).isEqualTo(departmentDTO.getName()),
                () -> assertThat(department2.getLocation()).isEqualTo(departmentDTO.getLocation())
        );
    }
}
