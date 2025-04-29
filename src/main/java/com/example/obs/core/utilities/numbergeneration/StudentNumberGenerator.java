package com.example.obs.core.utilities.numbergeneration;

import com.example.obs.dateAccess.DepartmentRepository;
import com.example.obs.dateAccess.StudentRepository;
import com.example.obs.model.entity.Department;
import com.example.obs.model.entity.Student;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;


@Component
public class StudentNumberGenerator {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;

    public StudentNumberGenerator(StudentRepository studentRepository,
                                  DepartmentRepository departmentRepository) {
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
    }


    public String generateStudentNumber(Long departmentId) {
        StringBuilder studentNumber = new StringBuilder();


        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String yearLastTwoDigits = String.valueOf(currentYear).substring(2);
        studentNumber.append(yearLastTwoDigits);


        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Bölüm bulunamadı"));
        String facultyCode = department.getFaculty().getFacultyCode();
        studentNumber.append(facultyCode);


        String departmentCode = department.getDepartmentCode();
        studentNumber.append(departmentCode);


        List<Student> studentsInDepartmentThisYear = studentRepository.findByDepartmentIdAndStudentNumberStartingWith(
                departmentId, yearLastTwoDigits + facultyCode + departmentCode);
        int orderNumber = studentsInDepartmentThisYear.size() + 1;


        String formattedOrderNumber = String.format("%03d", orderNumber);
        studentNumber.append(formattedOrderNumber);

        return studentNumber.toString();
    }
}