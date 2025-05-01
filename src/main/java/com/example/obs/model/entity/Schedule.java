package com.example.obs.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

@Entity
@Table(name = "schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_instructor_id", nullable = false)
    private CourseInstructor courseInstructor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek day;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false, length = 100)
    private String classroom;

    @Column(nullable = false)
    private String academicTerm;

    @Column(nullable = false)
    private Boolean isActive;
    
    @Column(nullable = false, length = 100)
    private String building;
    
    @Column(nullable = true, length = 255)
    private String notes;
    
    // Bu ders oturumunun dakika cinsinden süresini hesapla
    @Transient
    public int getDurationMinutes() {
        return (int) Duration.between(startTime, endTime).toMinutes();
    }
    
    // Bu ders oturumunun saat:dakika formatında süresini göster
    @Transient
    public String getDurationFormatted() {
        int minutes = getDurationMinutes();
        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;
        return String.format("%d saat %d dakika", hours, remainingMinutes);
    }
    
    // Bu ders zamanı verilen tarihle aynı günde mi?
    @Transient
    public boolean isOnSameDay(java.time.LocalDate date) {
        return date.getDayOfWeek() == this.day;
    }
} 