package com.example.obs.core.utilities.numbergeneration;

import com.example.obs.dataAccess.DepartmentRepository;
import com.example.obs.dataAccess.StudentRepository;
import com.example.obs.model.entity.Department;
import com.example.obs.model.entity.Student;
import org.springframework.stereotype.Component;
import lombok.AllArgsConstructor;

import java.util.Calendar;
import java.util.List;

@Component
@AllArgsConstructor
public class StudentNumberGenerator {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;

    public String generateStudentNumber(Long departmentId) {
        String yearCode = getCurrentYearCode();
        Department department = getDepartmentById(departmentId);

        String facultyCode = department.getFaculty().getFacultyCode();
        String departmentCode = department.getDepartmentCode();
        String orderNumber = getOrderNumberForDepartment(departmentId, yearCode, facultyCode, departmentCode);

        return yearCode + facultyCode + departmentCode + orderNumber;
    }

    private String getCurrentYearCode() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return String.valueOf(currentYear).substring(2);
    }

    private Department getDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Bölüm bulunamadı! ID: " + departmentId));
    }

    private String getOrderNumberForDepartment(Long departmentId, String yearCode, String facultyCode, String departmentCode) {
        String codePrefix = yearCode + facultyCode + departmentCode;
        List<Student> studentsInDepartmentThisYear = studentRepository.findByDepartmentIdAndStudentNumberStartingWith(departmentId, codePrefix);

        int orderNumber = studentsInDepartmentThisYear.size() + 1;
        return String.format("%03d", orderNumber);
    }
}