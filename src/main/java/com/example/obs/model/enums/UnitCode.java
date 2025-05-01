package com.example.obs.model.enums;

public enum UnitCode {
    RECTOR_OFFICE(1),
    STUDENT_AFFAIRS(2),
    PERSONNEL_OFFICE(3),
    FINANCE(4),
    IT_SERVICES(5),
    LIBRARY(6),
    INTERNATIONAL_OFFICE(7),
    CAREER_CENTER(8),
    REGISTRY_OFFICE(9),
    OTHER(99);

    private final int code;

    UnitCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
} 