package com.endava.webapp.Integration;

import com.endava.webapp.DTO.EmployeeDTO;
import com.endava.webapp.Entity.Department;
import com.endava.webapp.Entity.Employee;
import com.endava.webapp.Exception.EmployeeNotFoundException;
import com.endava.webapp.Repository.DepartmentRepository;
import com.endava.webapp.Repository.EmployeeRepository;
import com.endava.webapp.Service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class EmployeeControllerIT {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    Flyway flyway;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EmployeeService employeeService;

    private Department department;
    private Employee employee1, employee2;
    private EmployeeDTO employeeDTO1, employeeDTO2, invalidEmployeeDTO;

    private List<Employee> employeeList;
    private List<EmployeeDTO> employeeDTOList;

    @BeforeEach
    public void clean() {
        department = new Department("N1", "L1");

        employee1 = new Employee("F1", "L1", department, "M1", 1, 5.3);
        employee2 = new Employee("F2", "L2", department, "M2", 2, 15.8);

        employeeDTO1 = new EmployeeDTO(1L, "F1", "L1", 1L, "M1", 1, 5.3);
        employeeDTO2 = new EmployeeDTO(2L, "F2", "L2", 1L, "M2", 2, 15.8);
        invalidEmployeeDTO = new EmployeeDTO(1L, "F1", "L1", 1L, "M1", 1, 0.3);

        employeeList = Arrays.asList(employee1, employee2);
        employeeDTOList = Arrays.asList(employeeDTO1, employeeDTO2);

        flyway.clean();
        flyway.migrate();
    }

    @Test
    void getAll_shouldReturnAllEmployees() throws Exception {
        departmentRepository.save(department);
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        MvcResult mvcResult = mockMvc.perform(get("/employees")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(employeeDTOList));
    }

    @Test
    void getEmployeeById_shouldReturnTheEmployees() throws Exception {
        departmentRepository.save(department);
        employeeRepository.save(employee1);

        MvcResult mvcResult = mockMvc.perform(get("/employees/{id}", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(employeeDTO1));
    }

    @Test
    void gettingByIdWithInvalidID_ReturnNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/employees/{id}", 3L)
                        .contentType("application.json"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmployeeNotFoundException))
                .andExpect(result -> assertEquals("Employee with id " + 3 + " not found",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void gettingAllFromAnEmptyTable_shouldReturnNoContent() throws Exception {
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk());
    }

    @Test
    void addNewEmployerWithValidBody_shouldReturnNewEmployee() throws Exception {
        departmentRepository.save(department);
        MvcResult mvcResult = mockMvc.perform(post("/employees")
                        .content(objectMapper.writeValueAsString(employeeDTO1))
                        .contentType("application/json"))
                .andExpect(status().isCreated())
                .andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertAll(
                () -> assertThat(employeeService.getById(1L).getSalary()).isEqualTo(employeeDTO1.getSalary()),
                () -> assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(employeeDTO1))
        );
    }

    @Test
    void whenAddingNewInvalidEmployee_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/employees")
                        .content(objectMapper.writeValueAsString(invalidEmployeeDTO))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void whenEditingAnEmployeeWithValidData_shouldReturnNewEmployeeData() throws Exception {
        departmentRepository.save(department);
        employeeRepository.save(employee1);
        MvcResult mvcResult = mockMvc.perform(put("/employees/{id}", 1L)
                        .content(objectMapper.writeValueAsString(employeeDTO1))
                        .contentType("application/json"))
                .andExpect(status().isCreated())
                .andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertAll(
                () -> assertThat(employeeService.getById(1L).getEmail()).isEqualTo(employeeDTO1.getEmail()),
                () -> assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(employeeDTO1))
        );
    }

    @Test
    void whenEditingAnEmployeeWithInvalidData_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/employees/{id}", 1L)
                        .content(objectMapper.writeValueAsString(invalidEmployeeDTO))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}