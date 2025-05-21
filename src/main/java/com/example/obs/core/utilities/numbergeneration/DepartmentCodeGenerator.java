package com.example.obs.core.utilities.numbergeneration;

import com.example.obs.dataAccess.DepartmentRepository;
import com.example.obs.model.entity.Department;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class DepartmentCodeGenerator {

    private final DepartmentRepository departmentRepository;

    public String generateNextCode() {
        Optional<Department> lastDepartment = departmentRepository.findTopByOrderByCodeDesc();
        if (lastDepartment.isPresent()) {
            String lastCode = lastDepartment.get().getDepartmentCode();
            int lastNumber = Integer.parseInt(lastCode);
            int nextNumber = lastNumber + 1;
            return String.format("%02d", nextNumber);
        } else {
            return "01";
        }
    }
}
