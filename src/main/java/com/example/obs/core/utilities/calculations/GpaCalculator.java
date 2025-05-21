package com.example.obs.core.utilities.calculations;

import com.example.obs.model.entity.Course;
import com.example.obs.model.entity.Grade;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class GpaCalculator {
    
    private static final Map<String, BigDecimal> LETTER_GRADE_POINTS = new HashMap<>();
    
    static {
        LETTER_GRADE_POINTS.put("AA", new BigDecimal("4.0"));
        LETTER_GRADE_POINTS.put("BA", new BigDecimal("3.5"));
        LETTER_GRADE_POINTS.put("BB", new BigDecimal("3.0"));
        LETTER_GRADE_POINTS.put("CB", new BigDecimal("2.5"));
        LETTER_GRADE_POINTS.put("CC", new BigDecimal("2.0"));
        LETTER_GRADE_POINTS.put("DC", new BigDecimal("1.5"));
        LETTER_GRADE_POINTS.put("DD", new BigDecimal("1.0"));
        LETTER_GRADE_POINTS.put("FD", new BigDecimal("0.5"));
        LETTER_GRADE_POINTS.put("FF", new BigDecimal("0.0"));
    }
    
    public static Double calculateGpa(List<Grade> grades) {
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }
        
        BigDecimal totalPoints = BigDecimal.ZERO;
        BigDecimal totalCredits = BigDecimal.ZERO;
        
        for (Grade grade : grades) {
            if (grade.getLetterGrade() != null && !grade.getLetterGrade().isEmpty() && grade.getCourse() != null) {
                Course course = grade.getCourse();
                BigDecimal creditValue = new BigDecimal(course.getEcts());
                BigDecimal gradePoint = getGradePointForLetter(grade.getLetterGrade());
                
                BigDecimal weightedPoints = creditValue.multiply(gradePoint);
                
                totalPoints = totalPoints.add(weightedPoints);
                totalCredits = totalCredits.add(creditValue);
            }
        }
        
        if (totalCredits.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        
        BigDecimal gpa = totalPoints.divide(totalCredits, 2, RoundingMode.HALF_UP);
        
        return gpa.doubleValue();
    }
    
    private static BigDecimal getGradePointForLetter(String letterGrade) {
        return LETTER_GRADE_POINTS.getOrDefault(letterGrade, BigDecimal.ZERO);
    }
}
