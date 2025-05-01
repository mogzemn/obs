package com.example.obs.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceSummaryResponse {
    private Long courseId;
    private String courseName;
    private int totalMinutes;           // Toplam ders süresi (dakika)
    private int attendedMinutes;        // Katılınan süre (dakika)
    private int absenceMinutes;         // Devamsızlık süresi (dakika)
    private double attendanceRate;      // Katılım oranı (%)
    private int totalHours;             // Dersin toplam saat sayısı
    private int absenceLimit;           // Devamsızlık limiti (dakika)
    private int remainingAbsenceMinutes; // Kalan devamsızlık hakkı (dakika)
    private boolean isExceedingLimit;    // Devamsızlık limitini aşıyor mu?
    
    // Kalan devamsızlık hakkını formatlı string olarak göster
    public String getRemainingAbsenceFormatted() {
        int hours = remainingAbsenceMinutes / 60;
        int minutes = remainingAbsenceMinutes % 60;
        return String.format("%d saat %d dakika", hours, minutes);
    }
    
    // Devamsızlık süresini formatlı string olarak göster
    public String getAbsenceMinutesFormatted() {
        int hours = absenceMinutes / 60;
        int minutes = absenceMinutes % 60;
        return String.format("%d saat %d dakika", hours, minutes);
    }
} 