package com.example.obs.core.utilities.numbergeneration;

import com.example.obs.dataAccess.AcademicRepository;
import com.example.obs.dataAccess.DepartmentRepository;
import com.example.obs.model.entity.Academic;
import com.example.obs.model.entity.Department;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@Component
public class AcademicNumberGenerator {

    private final AcademicRepository academicRepository;
    private final DepartmentRepository departmentRepository;

    public AcademicNumberGenerator(AcademicRepository academicRepository,
                                   DepartmentRepository departmentRepository) {
        this.academicRepository = academicRepository;
        this.departmentRepository = departmentRepository;
    }


    public String generateRegistrationNumber(Long departmentId) {
        String yearCode = getYearCode();
        String facultyCodeLastDigit = getFacultyCodeLastDigit(departmentId);
        String prefix = "A" + yearCode + "F" + facultyCodeLastDigit;
        String orderNumber = getNextOrderNumber(prefix);

        return prefix + orderNumber;
    }


    private String getYearCode() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return String.valueOf(currentYear).substring(2);
    }


    private String getFacultyCodeLastDigit(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Bölüm bulunamadı! ID: " + departmentId));
        String facultyCode = department.getFaculty().getFacultyCode();
        return String.valueOf(facultyCode.charAt(facultyCode.length() - 1));
    }


    private String getNextOrderNumber(String prefix) {
        List<Academic> matchingAcademics = academicRepository.findAll().stream()
                .filter(academic -> academic.getAcademicNumber() != null &&
                        academic.getAcademicNumber().startsWith(prefix))
                .toList();

        int nextOrder = matchingAcademics.size() + 1;
        return String.format("%03d", nextOrder);
    }
}