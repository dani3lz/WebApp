package com.endava.webapp.Service;

import com.endava.webapp.DTO.DepartmentDTO;
import com.endava.webapp.Entity.Department;
import com.endava.webapp.Exception.DepartmentNotFoundException;
import com.endava.webapp.Repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.endava.webapp.DTO.Convertor.toEntity;
import static com.endava.webapp.DTO.Convertor.toDTO;

@Service
@Transactional
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<DepartmentDTO> getAll() {
        List<DepartmentDTO> departmentList = new ArrayList<>();
        departmentRepository.getAll().forEach(department -> departmentList.add(toDTO(department)));
        return departmentList;
    }

    public DepartmentDTO getById(Long id) {
        Optional<Department> department = Optional.ofNullable(departmentRepository.getById(id));
        if (department.isPresent()) {
            return toDTO(department.get());
        }
        throw new DepartmentNotFoundException(id);
    }

    public DepartmentDTO addDepartment(DepartmentDTO department) {
        return toDTO(Optional.of(departmentRepository.save(toEntity(department))).get());
    }

    public DepartmentDTO editDepartment(Long id, DepartmentDTO department) {
        Optional<Department> departmentResult = Optional.ofNullable(departmentRepository.editDepartment(id, toEntity(department)));
        if (departmentResult.isPresent()) {
            return toDTO(departmentResult.get());
        }
        throw new DepartmentNotFoundException(id);
    }


}
