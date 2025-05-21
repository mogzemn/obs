package com.example.obs.core.utilities.academicyear;

import com.example.obs.dataAccess.AcademicYearRepository;
import com.example.obs.model.entity.AcademicYear;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AcademicYearUtil {

    private final AcademicYearRepository academicYearRepository;

    public String getCurrentAcademicYear() {
        LocalDate today = LocalDate.now();
        
        Optional<AcademicYear> academicYear = academicYearRepository.findByDate(today);
        
        if (academicYear.isPresent()) {
            return academicYear.get().getName();
        } else {
            int currentYear = today.getYear();
            int month = today.getMonthValue();
            
            if (month >= 9) {
                return currentYear + "-" + (currentYear + 1);
            } else {
                return (currentYear - 1) + "-" + currentYear;
            }
        }
    }
    
    public String getNextAcademicYear() {
        String currentYear = getCurrentAcademicYear();
        String[] years = currentYear.split("-");
        int startYear = Integer.parseInt(years[0]);
        return (startYear + 1) + "-" + (startYear + 2);
    }
    
    public String getAcademicYearNameByDate(LocalDate date) {
        return academicYearRepository.findByDate(date)
                .map(AcademicYear::getName)
                .orElse(null);
    }
}
