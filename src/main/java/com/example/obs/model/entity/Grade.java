package com.example.obs.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.example.obs.model.enums.Semester;

@Entity
@Table(name = "grades", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "course_id", "semester", "academic_year"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "academic_id", nullable = false)
    private Academic academic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Semester semester;

    @Column(name = "academic_year", nullable = false, length = 20)
    private String academicYear;

    @Column(precision = 5, scale = 2)
    private BigDecimal midtermGrade;

    @Column(precision = 5, scale = 2)
    private BigDecimal assignmentGrade;

    @Column(precision = 5, scale = 2)
    private BigDecimal finalGrade;

    @Column(precision = 5, scale = 2)
    private BigDecimal makeupGrade;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal midtermWeight;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal assignmentWeight;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal finalWeight;

    @Column(precision = 5, scale = 2)
    private BigDecimal average;

    @Column(length = 2)
    private String letterGrade;

    private Boolean isPassed;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    private User updatedBy;

    private LocalDateTime updatedAt;
}