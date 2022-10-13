package com.endava.webapp.Controller;

import com.endava.webapp.DTO.DepartmentDTO;
import com.endava.webapp.Service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DepartmentDTO> getAll() {
        return departmentService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DepartmentDTO getById(@PathVariable final Long id) {
        return departmentService.getById(id);
    }

    @PostMapping
    public ResponseEntity<DepartmentDTO> addDepartment(@Valid @RequestBody final DepartmentDTO department) {
        DepartmentDTO departmentResult = departmentService.addDepartment(department);
        if (Objects.isNull(departmentResult)) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(departmentResult, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentDTO editDepartment(@PathVariable final Long id,
                                                        @Valid @RequestBody final DepartmentDTO department) {
        return departmentService.editDepartment(id, department);
    }
}
