package com.example.obs.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transcripts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transcript {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private Double gpa; // Genel not ortalaması (kredi ağırlıklı)
    
    @Column(nullable = false)
    private Double cgpa; // Kümülatif not ortalaması (AKTS ağırlıklı)

    @Column(nullable = false)
    private Integer totalCredits;
    
    @Column(nullable = false)
    private Integer totalEcts;

    @Column(nullable = false)
    private Integer completedCredits;
    
    @Column(nullable = false)
    private Integer completedEcts;

    @OneToMany(mappedBy = "transcript", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TranscriptItem> items = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime generatedAt;

    @Column(nullable = false)
    private boolean isOfficial;
    
    // Açık setter metodları
    public void setStudent(Student student) {
        this.student = student;
    }
    
    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }
    
    public void setCgpa(Double cgpa) {
        this.cgpa = cgpa;
    }
    
    public void setTotalCredits(Integer totalCredits) {
        this.totalCredits = totalCredits;
    }
    
    public void setTotalEcts(Integer totalEcts) {
        this.totalEcts = totalEcts;
    }
    
    public void setCompletedCredits(Integer completedCredits) {
        this.completedCredits = completedCredits;
    }
    
    public void setCompletedEcts(Integer completedEcts) {
        this.completedEcts = completedEcts;
    }
    
    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
    
    public void setOfficial(boolean isOfficial) {
        this.isOfficial = isOfficial;
    }
    
    public List<TranscriptItem> getItems() {
        return this.items;
    }
    
    public void setItems(List<TranscriptItem> items) {
        this.items = items;
    }
    
    // Kredi ağırlıklı genel not ortalamasını (GNO) hesapla
    public void calculateGPA() {
        double totalPoints = 0.0;
        int totalCreditsEarned = 0;
        
        for (TranscriptItem item : items) {
            Grade grade = item.getGrade();
            if (grade != null && grade.getLetterGrade() != null) {
                totalPoints += grade.getCreditWeightedGrade();
                totalCreditsEarned += item.getCourse().getCredits();
            }
        }
        
        this.gpa = (totalCreditsEarned > 0) ? totalPoints / totalCreditsEarned : 0.0;
        this.totalCredits = totalCreditsEarned;
        this.completedCredits = calculateCompletedCredits();
    }
    
    // AKTS ağırlıklı kümülatif not ortalamasını (AGNO) hesapla
    public void calculateCGPA() {
        double totalEctsPoints = 0.0;
        int totalEctsEarned = 0;
        
        for (TranscriptItem item : items) {
            Grade grade = item.getGrade();
            if (grade != null && grade.getLetterGrade() != null) {
                totalEctsPoints += grade.getEctsWeightedGrade();
                totalEctsEarned += item.getCourse().getEcts();
            }
        }
        
        this.cgpa = (totalEctsEarned > 0) ? totalEctsPoints / totalEctsEarned : 0.0;
        this.totalEcts = totalEctsEarned;
        this.completedEcts = calculateCompletedEcts();
    }
    
    // Başarılı derslerin toplam kredisini hesapla
    private int calculateCompletedCredits() {
        return items.stream()
                .filter(item -> item.getGrade() != null && item.getGrade().isPassingGrade())
                .mapToInt(item -> item.getCourse().getCredits())
                .sum();
    }
    
    // Başarılı derslerin toplam AKTS'sini hesapla
    private int calculateCompletedEcts() {
        return items.stream()
                .filter(item -> item.getGrade() != null && item.getGrade().isPassingGrade())
                .mapToInt(item -> item.getCourse().getEcts())
                .sum();
    }
} 