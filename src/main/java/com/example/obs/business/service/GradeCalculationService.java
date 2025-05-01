package com.example.obs.business.service;

import com.example.obs.model.entity.Grade;
import com.example.obs.model.entity.Student;
import com.example.obs.model.entity.Transcript;
import com.example.obs.model.enums.LetterGrade;

import java.math.BigDecimal;

public interface GradeCalculationService {
    

    Transcript calculateAndUpdateStudentGPA(Long studentId);

    LetterGrade calculateLetterGrade(double numericGrade);
    
    /**
     * Bir ders için ağırlıklı notu hesaplar
     * @param midtermGrade Vize notu
     * @param finalGrade Final notu
     * @param assignmentGrade Ödev notu
     * @param makeupGrade Bütünleme notu
     * @param midtermWeight Vize ağırlığı
     * @param finalWeight Final ağırlığı
     * @param assignmentWeight Ödev ağırlığı
     * @return Ağırlıklı not ortalaması
     */
    BigDecimal calculateWeightedGrade(
            BigDecimal midtermGrade, 
            BigDecimal finalGrade, 
            BigDecimal assignmentGrade, 
            BigDecimal makeupGrade,
            BigDecimal midtermWeight,
            BigDecimal finalWeight,
            BigDecimal assignmentWeight);
    
    /**
     * Belirli bir dönem için öğrencinin GNO (kredi ağırlıklı) ve 
     * AGNO (AKTS ağırlıklı) değerlerini hesaplar
     * @param studentId Öğrenci ID
     * @param academicYear Akademik yıl
     * @param semester Dönem
     * @return Öğrencinin transkripti
     */
    Transcript calculateSemesterGPA(Long studentId, String academicYear, String semester);
} 