package com.example.obs.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transcript_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transcript_id", nullable = false)
    private Transcript transcript;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "grade_id", nullable = false)
    private Grade grade;

    @Column(nullable = false)
    private String academicTerm; // Örnek: "2023-2024 Güz"

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = true)
    private String notes;
    
    // Açık getter metodları
    public Grade getGrade() {
        return this.grade;
    }
    
    public Course getCourse() {
        return this.course;
    }
    
    // Açık setter metodları
    public void setTranscript(Transcript transcript) {
        this.transcript = transcript;
    }
    
    public void setCourse(Course course) {
        this.course = course;
    }
    
    public void setGrade(Grade grade) {
        this.grade = grade;
    }
    
    public void setAcademicTerm(String academicTerm) {
        this.academicTerm = academicTerm;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
} 