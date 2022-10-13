package com.endava.webapp.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Department")
public class Department implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "location")
    @NotBlank
    private String location;

    @OneToMany(mappedBy = "department")
    @JsonManagedReference
    private Set<Employee> employeeSet;

    public Department(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public void editEmployee(Department department) {
        this.setName(department.getName());
        this.setLocation(department.getLocation());
    }
}
