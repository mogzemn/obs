package com.example.obs.model.enums;

import java.math.BigDecimal;

public enum LetterGrade {
    AA(GradeStatus.PASSED, new BigDecimal("4.0")),
    BA(GradeStatus.PASSED, new BigDecimal("3.5")),
    BB(GradeStatus.PASSED, new BigDecimal("3.0")),
    CB(GradeStatus.PASSED, new BigDecimal("2.5")),
    CC(GradeStatus.PASSED, new BigDecimal("2.0")),
    DC(GradeStatus.CONDITIONAL, new BigDecimal("1.5")),
    DD(GradeStatus.CONDITIONAL, new BigDecimal("1.0")),
    FF(GradeStatus.FAILED, new BigDecimal("0.0")),
    D(GradeStatus.ABSENT, new BigDecimal("0.0"));

    private final GradeStatus status;
    private final BigDecimal gradePoint;

    LetterGrade(GradeStatus status, BigDecimal gradePoint) {
        this.status = status;
        this.gradePoint = gradePoint;
    }

    public GradeStatus getStatus() {
        return status;
    }

    public BigDecimal getGradePoint() {
        return gradePoint;
    }

    public boolean isPassed() {
        return this.status == GradeStatus.PASSED;
    }

    public boolean isConditionalPassed() {
        return this.status == GradeStatus.CONDITIONAL;
    }

    public boolean isFailed() {
        return this.status == GradeStatus.FAILED;
    }

    public boolean isAbsent() {
        return this.status == GradeStatus.ABSENT;
    }
} 