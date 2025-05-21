package com.example.obs.core.utilities.gradecalculation;

import com.example.obs.business.service.GradeScaleService;
import com.example.obs.model.entity.GradeScale;
import com.example.obs.model.enums.GradeStatus;
import com.example.obs.model.enums.LetterGrade;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component
public class GradeCalculator {

    private final GradeScaleService gradeScaleService;

    public GradeCalculator(GradeScaleService gradeScaleService) {
        this.gradeScaleService = gradeScaleService;
    }


    public BigDecimal calculateAverage(
            BigDecimal midtermGrade, BigDecimal assignmentGrade,
            BigDecimal finalGrade, BigDecimal makeupGrade,
            BigDecimal midtermWeight, BigDecimal assignmentWeight, BigDecimal finalWeight) {


        if (gradesAreInvalid(midtermGrade, assignmentGrade, finalGrade, makeupGrade)) {
            return null;
        }


        BigDecimal effectiveFinalGrade = (makeupGrade != null) ? makeupGrade : finalGrade;


        BigDecimal average = midtermGrade.multiply(midtermWeight)
                .add(assignmentGrade.multiply(assignmentWeight))
                .add(effectiveFinalGrade.multiply(finalWeight));


        return average.setScale(2, RoundingMode.HALF_UP);
    }


    public LetterGrade calculateLetterGrade(BigDecimal average, Long courseId, Long departmentId) {
        if (average == null) {
            return null;
        }


        GradeScale scale = gradeScaleService.findApplicableScale(courseId, departmentId);
        return determineLetterGradeByScale(average.doubleValue(), scale);
    }


    public LetterGrade calculateLetterGrade(BigDecimal average) {
        if (average == null) {
            return null;
        }
        return determineStaticLetterGrade(average.doubleValue());
    }


    public LetterGrade markAsAbsent() {
        return LetterGrade.D;
    }


    public GradeStatus determineStatus(LetterGrade letterGrade) {
        return (letterGrade != null) ? letterGrade.getStatus() : GradeStatus.FAILED;
    }


    public boolean isPassed(LetterGrade letterGrade) {
        return (letterGrade != null) && (letterGrade.isPassed() || letterGrade.isConditionalPassed());
    }


    public boolean isFullyPassed(LetterGrade letterGrade) {
        return (letterGrade != null) && letterGrade.isPassed();
    }


    public boolean isConditionallyPassed(LetterGrade letterGrade) {
        return (letterGrade != null) && letterGrade.isConditionalPassed();
    }


    public boolean isFailed(LetterGrade letterGrade) {
        return (letterGrade == null) || letterGrade.isFailed();
    }


    public boolean isAbsent(LetterGrade letterGrade) {
        return (letterGrade != null) && letterGrade.isAbsent();
    }


    public BigDecimal getGradePoint(LetterGrade letterGrade) {
        return (letterGrade != null) ? letterGrade.getGradePoint() : BigDecimal.ZERO;
    }


    private boolean gradesAreInvalid(BigDecimal midtermGrade, BigDecimal assignmentGrade,
                                     BigDecimal finalGrade, BigDecimal makeupGrade) {
        return midtermGrade == null || assignmentGrade == null || 
               (finalGrade == null && makeupGrade == null);
    }

    private LetterGrade determineLetterGradeByScale(double average, GradeScale scale) {
        if (average >= scale.getAaMin()) return LetterGrade.AA;
        if (average >= scale.getBaMin()) return LetterGrade.BA;
        if (average >= scale.getBbMin()) return LetterGrade.BB;
        if (average >= scale.getCbMin()) return LetterGrade.CB;
        if (average >= scale.getCcMin()) return LetterGrade.CC;
        if (average >= scale.getDcMin()) return LetterGrade.DC;
        if (average >= scale.getDdMin()) return LetterGrade.DD;
        return LetterGrade.FF;
    }

    private LetterGrade determineStaticLetterGrade(double average) {
        if (average >= 90) return LetterGrade.AA;
        if (average >= 85) return LetterGrade.BA;
        if (average >= 80) return LetterGrade.BB;
        if (average >= 75) return LetterGrade.CB;
        if (average >= 70) return LetterGrade.CC;
        if (average >= 65) return LetterGrade.DC;
        if (average >= 60) return LetterGrade.DD;
        return LetterGrade.FF;
    }
}
