package com.example.obs.core.utilities;

import java.time.LocalDate;
import java.util.Random;

public class IdGenerator {
    
    private static final Random random = new Random();
    

    public static String generateStudentNumber(int facultyCode, int departmentCode) {
        int year = LocalDate.now().getYear() % 100;
        String facultyPart = String.format("%02d", facultyCode);
        String departmentPart = String.format("%02d", departmentCode);
        String randomPart = String.format("%03d", random.nextInt(1000));
        
        return year + facultyPart + departmentPart + randomPart;
    }
    

    public static String generateAcademicNumber(int facultyCode) {
        int year = LocalDate.now().getYear() % 100;
        String facultyPart = String.format("%02d", facultyCode);
        String randomPart = String.format("%04d", random.nextInt(10000));
        
        return "A" + year + facultyPart + randomPart;
    }
    

    public static String generateAdministrativeStaffNumber(int unitCode) {
        int year = LocalDate.now().getYear() % 100;
        String unitPart = String.format("%02d", unitCode);
        String randomPart = String.format("%04d", random.nextInt(10000));
        
        return "P" + year + unitPart + randomPart;
    }
} 