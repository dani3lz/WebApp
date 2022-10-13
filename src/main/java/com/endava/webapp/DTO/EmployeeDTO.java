package com.endava.webapp.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeDTO {
    private Long id;

    @NotBlank(message = "First name can't be blank!")
    private String firstName;

    @NotBlank(message = "Last name can't be blank!")
    private String lastName;

    @NotNull(message = "Department_id can't be null")
    private Long departmentId;

    @NotBlank(message = "Email can't be blank!")
    private String email;

    @NotNull(message = "Phone number can't be null")
    private Integer phoneNumber;

    @Min(1)
    private Double salary;

}
