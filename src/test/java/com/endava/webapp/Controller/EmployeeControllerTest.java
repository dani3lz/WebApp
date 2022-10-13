package com.endava.webapp.Controller;

import com.endava.webapp.DTO.EmployeeDTO;
import com.endava.webapp.Exception.EmployeeNotFoundException;
import com.endava.webapp.Service.DepartmentService;
import com.endava.webapp.Service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@AutoConfigureMockMvc
class EmployeeControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private EmployeeService employeeService;
    @MockBean
    private DepartmentService departmentService;
    @Autowired
    private MockMvc mockMvc;


    private List<EmployeeDTO> employeeDTOS;
    public static final EmployeeDTO VALID_EMPLOYEE = new EmployeeDTO(3L, "Harley", "David", 1L, "harley.david@yahoo.com", 1234, 300.0);
    public static final EmployeeDTO INVALID_EMPLOYEE = new EmployeeDTO(20L, "Kane", "Anderson", 1L, "kane@official.net", 1234, 0.4);


    @BeforeEach
    void setup() {
        EmployeeDTO employeeDTO1 = new EmployeeDTO(1L, "F1", "L1", 1L, "M1", 1, 5.0);
        EmployeeDTO employeeDTO2 = new EmployeeDTO(2L, "F2", "L2", 2L, "M2", 2, 15.0);
        EmployeeDTO employeeDTO3 = new EmployeeDTO(3L, "F3", "L3", 3L, "M3", 3, 115.0);
        employeeDTOS = Arrays.asList(employeeDTO1, employeeDTO2, employeeDTO3);
    }

    @Test
    void whenGettingByIdWithInvalidID_ReturnNotFound() throws Exception {

        when(employeeService.getById(3L)).thenThrow(new EmployeeNotFoundException(3L));
        mockMvc.perform(MockMvcRequestBuilders.get("/employees/3"))
                .andExpect(status().is(404)).andReturn();

        verify(employeeService).getById(3L);
    }

    @Test
    void whenGettingByIdWithValidID_shouldReturnEmployee() throws Exception {

        when(employeeService.getById(3L)).thenReturn(VALID_EMPLOYEE);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/employees/{id}", 3)
                        .contentType("application/json"))
                .andExpect(status().is(200))
                .andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(VALID_EMPLOYEE));
        verify(employeeService).getById(3L);
    }

    @Test
    void whenGettingAllFromAnEmptyTable_shouldReturnNoContent() throws Exception {

        when(employeeService.getAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(status().is(200));

        verify(employeeService).getAll();
    }

    @Test
    void whenGettingAllFromTable_shouldReturnListOfEmployees() throws Exception {

        when(employeeService.getAll()).thenReturn(employeeDTOS);
        MvcResult mvcResult = mockMvc.perform(get("/employees")
                        .contentType("application/json"))
                .andExpect(status().is(200))
                .andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(employeeDTOS));
        verify(employeeService).getAll();
    }

    @Test
    void whenAddingValidEmployee_shouldReturnEmployee() throws Exception {
        when(employeeService.addEmployee(any(EmployeeDTO.class))).thenReturn(VALID_EMPLOYEE);

        MvcResult mvcResult = mockMvc.perform(post("/employees")
                        .content(objectMapper.writeValueAsString(VALID_EMPLOYEE))
                        .contentType("application/json"))
                .andExpect(status().is(201))
                .andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(VALID_EMPLOYEE));
        verify(employeeService).addEmployee(any(EmployeeDTO.class));
    }

    @Test
    void whenAddingInvalidEmployee_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/employees")
                        .content(objectMapper.writeValueAsString(INVALID_EMPLOYEE))
                        .contentType("application/json"))
                .andExpect(status().is(400))
                .andReturn();
    }

    @Test
    void whenEditingAnEmployeeWithValidData_shouldReturnChangedEmployee() throws Exception {
        when(employeeService.editEmployee(anyLong(), any(EmployeeDTO.class))).thenReturn(VALID_EMPLOYEE);

        MvcResult mvcResult = mockMvc.perform(put("/employees/{id}", 1L)
                        .content(objectMapper.writeValueAsString(VALID_EMPLOYEE))
                        .contentType("application/json"))
                .andExpect(status().is(201))
                .andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(VALID_EMPLOYEE));
        verify(employeeService).editEmployee(anyLong(), any(EmployeeDTO.class));
    }

    @Test
    void whenEditingAnEmployeeWithInvalidData_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/employees/{id}", 1L)
                        .content(objectMapper.writeValueAsString(INVALID_EMPLOYEE))
                        .contentType("application/json"))
                .andExpect(status().is(400))
                .andReturn();

    }
}
