package com.example.obs.model.enums;

public enum LetterGrade {
    AA(4.0),
    BA(3.5),
    BB(3.0),
    CB(2.5),
    CC(2.0),
    DC(1.5),
    DD(1.0),
    FD(0.5),
    FF(0.0);

    private final double numericValue;

    LetterGrade(double numericValue) {
        this.numericValue = numericValue;
    }

    public double getNumericValue() {
        return numericValue;
    }


    public static LetterGrade fromNumericGrade(double numericGrade) {
        if (numericGrade >= 90) return AA;
        else if (numericGrade >= 85) return BA;
        else if (numericGrade >= 80) return BB;
        else if (numericGrade >= 75) return CB;
        else if (numericGrade >= 70) return CC;
        else if (numericGrade >= 65) return DC;
        else if (numericGrade >= 60) return DD;
        else if (numericGrade >= 50) return FD;
        else return FF;
    }


    public boolean isPassing() {
        return this != FF;
    }
} 