package com.endava.webapp.DTO;

import com.endava.webapp.Entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {
    private Long id;
    @NotBlank(message = "Name can't be blank!")
    private String name;
    @NotBlank(message = "Location can't be blank!")
    private String location;
    private Set<Employee> employeeSet;

    public DepartmentDTO(Long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }
}
