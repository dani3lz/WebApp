package com.endava.webapp.Controller;

import com.endava.webapp.DTO.DepartmentDTO;
import com.endava.webapp.Exception.DepartmentNotFoundException;
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
class DepartmentControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private DepartmentService departmentService;
    @MockBean
    private EmployeeService employeeService;
    @Autowired
    private MockMvc mockMvc;


    private List<DepartmentDTO> departmentDTOS;
    public static final DepartmentDTO VALID_DEPARTMENT = new DepartmentDTO(7L, "Testing", "Amsterdam");
    public static final DepartmentDTO INVALID_DEPARTMENT = new DepartmentDTO(9L, "", "Dublin");


    @BeforeEach
    void setup() {
        DepartmentDTO departmentDTO1 = new DepartmentDTO(1L, "N1", "L1");
        DepartmentDTO departmentDTO2 = new DepartmentDTO(2L, "N2", "L3");
        departmentDTOS = Arrays.asList(departmentDTO1, departmentDTO2);
    }

    @Test
    void whenGettingByIdWithInvalidID_ReturnNotFound() throws Exception {
        when(departmentService.getById(3L)).thenThrow(new DepartmentNotFoundException(3L));
        mockMvc.perform(MockMvcRequestBuilders.get("/departments/3"))
                .andExpect(status().is(404)).andReturn();

        verify(departmentService).getById(3L);
    }

    @Test
    void whenGettingByIdWithValidID_shouldReturnDepartment() throws Exception {
        when(departmentService.getById(7L)).thenReturn(VALID_DEPARTMENT);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/departments/{id}", 7)
                        .contentType("application/json"))
                .andExpect(status().is(200))
                .andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(VALID_DEPARTMENT));
        verify(departmentService).getById(7L);
    }

    @Test
    void whenGettingAllFromAnEmptyTable_shouldReturnNoContent() throws Exception {
        when(departmentService.getAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/departments"))
                .andExpect(status().is(200));

        verify(departmentService).getAll();
    }

    @Test
    void whenGettingAllFromTable_shouldReturnListOfDepartments() throws Exception {
        when(departmentService.getAll()).thenReturn(departmentDTOS);
        MvcResult mvcResult = mockMvc.perform(get("/departments")
                        .contentType("application/json"))
                .andExpect(status().is(200))
                .andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(departmentDTOS));
        verify(departmentService).getAll();
    }

    @Test
    void whenAddingNewValidDepartment_shouldReturnDepartment() throws Exception {
        when(departmentService.addDepartment(any())).thenReturn(VALID_DEPARTMENT);

        MvcResult mvcResult = mockMvc.perform(post("/departments")
                        .content(objectMapper.writeValueAsString(VALID_DEPARTMENT))
                        .contentType("application/json"))
                .andExpect(status().is(201))
                .andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(VALID_DEPARTMENT));
        verify(departmentService).addDepartment(any());
    }

    @Test
    void whenAddingNewInvalidDepartment_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/departments")
                        .content(objectMapper.writeValueAsString(INVALID_DEPARTMENT))
                        .contentType("application/json"))
                .andExpect(status().is(400))
                .andReturn();
    }

    @Test
    void whenEditingDepartmentWithValidData_shouldReturnChangedDepartment() throws Exception {
        when(departmentService.editDepartment(anyLong(), any())).thenReturn(VALID_DEPARTMENT);

        MvcResult mvcResult = mockMvc.perform(put("/departments/{id}", 1L)
                        .content(objectMapper.writeValueAsString(VALID_DEPARTMENT))
                        .contentType("application/json"))
                .andExpect(status().is(201))
                .andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(VALID_DEPARTMENT));
        verify(departmentService).editDepartment(anyLong(), any());
    }

    @Test
    void whenEditingDepartmentWithInvalidData_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/departments/{id}", 1L)
                        .content(objectMapper.writeValueAsString(INVALID_DEPARTMENT))
                        .contentType("application/json"))
                .andExpect(status().is(400))
                .andReturn();

    }
}
