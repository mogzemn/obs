package com.example.obs.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseAbsenceSummaryResponse {
    private Long courseId;
    private String courseCode;
    private String courseName;
    private int totalCourseHours;       // Dersin toplam saati (dakika)
    private int absenceLimit;           // Devamsızlık limiti (dakika)
    private int totalAbsenceMinutes;    // Toplam devamsızlık (dakika)
    private double absencePercentage;   // Devamsızlık yüzdesi
    private boolean failDueToAbsence;   // Devamsızlıktan kalıyor mu?
    
    // Devamsızlık limitini formatlı string olarak göster
    public String getAbsenceLimitFormatted() {
        int hours = absenceLimit / 60;
        int minutes = absenceLimit % 60;
        return String.format("%d saat %d dakika", hours, minutes);
    }
    
    // Toplam devamsızlık süresini formatlı string olarak göster
    public String getTotalAbsenceFormatted() {
        int hours = totalAbsenceMinutes / 60;
        int minutes = totalAbsenceMinutes % 60;
        return String.format("%d saat %d dakika", hours, minutes);
    }
} 