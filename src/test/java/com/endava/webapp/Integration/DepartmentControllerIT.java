package com.endava.webapp.Integration;

import com.endava.webapp.DTO.DepartmentDTO;
import com.endava.webapp.Entity.Department;
import com.endava.webapp.Exception.DepartmentNotFoundException;
import com.endava.webapp.Repository.DepartmentRepository;
import com.endava.webapp.Service.DepartmentService;
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
class DepartmentControllerIT {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    Flyway flyway;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DepartmentService departmentService;

    Department department1, department2;
    DepartmentDTO departmentDTO1, departmentDTO2, invalidDepartmentDTO;

    List<Department> departmentList;
    List<DepartmentDTO> departmentDTOList;

    @BeforeEach
    public void cleanUp() {
        department1 = new Department("N1", "L1");
        department2 = new Department("N2", "L2");

        departmentDTO1 = new DepartmentDTO(1L, "N1", "L1");
        departmentDTO2 = new DepartmentDTO(2L, "N2", "L2");
        invalidDepartmentDTO = new DepartmentDTO(1L, "", "L");

        departmentList = Arrays.asList(department1, department2);
        departmentDTOList = Arrays.asList(departmentDTO1, departmentDTO2);

        flyway.clean();
        flyway.migrate();
    }

    @Test
    void getAll_shouldReturnAllDepartments() throws Exception {
        departmentRepository.save(department1);
        departmentRepository.save(department2);
        MvcResult mvcResult = mockMvc.perform(get("/departments")
                        .contentType("application/json"))
                .andExpect(status().is(200))
                .andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(departmentDTOList));
    }

    @Test
    void getDepartmentById_shouldReturnTheDepartment() throws Exception {
        departmentRepository.save(department1);

        MvcResult mvcResult = mockMvc.perform(get("/departments/{id}", 1L)
                        .contentType("application/json"))
                .andExpect(status().is(200))
                .andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(departmentDTO1));
    }

    @Test
    void gettingByIdWithInvalidID_ReturnNotFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/departments/{id}", 99L)
                        .contentType("application.json"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DepartmentNotFoundException))
                .andExpect(result -> assertEquals("Department with id " + 99 + " not found", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void gettingAllFromAnEmptyTable_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk());
    }

    @Test
    void addNewDepartmentWithValidBody_shouldReturnNewDepartment() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/departments")
                        .content(objectMapper.writeValueAsString(departmentDTO1))
                        .contentType("application/json"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(departmentService.getById(1L).getName()).isEqualTo(departmentDTO1.getName());
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(departmentDTO1));
    }

    @Test
    void whenAddingNewInvalidDepartment_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/departments")
                        .content(objectMapper.writeValueAsString(invalidDepartmentDTO))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void whenEditingAnDepartmentWithValidData_shouldReturnNewDepartmentData() throws Exception {
        departmentRepository.save(department1);
        departmentRepository.save(department2);
        MvcResult mvcResult = mockMvc.perform(put("/departments/{id}", 2)
                        .content(objectMapper.writeValueAsString(departmentDTO2))
                        .contentType("application/json"))
                .andExpect(status().isCreated())
                .andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(departmentService.getById(2L).getLocation()).isEqualTo(departmentDTO2.getLocation());
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(departmentDTO2));
    }

    @Test
    void whenEditingAnDepartmentWithInvalidData_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/departments/{id}", 1L)
                        .content(objectMapper.writeValueAsString(invalidDepartmentDTO))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}