package com.example.obs.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import com.example.obs.model.enums.Semester;

@Entity
@Table(name = "grades", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "course_id", "semester", "academic_year"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Grade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(precision = 5, scale = 2)
    private BigDecimal average;

    @Column(name = "midterm_weight", nullable = false, precision = 5, scale = 2)
    private BigDecimal midtermWeight;

    @Column(name = "assignment_weight", nullable = false, precision = 5, scale = 2)
    private BigDecimal assignmentWeight;

    @Column(name = "final_weight", nullable = false, precision = 5, scale = 2)
    private BigDecimal finalWeight;

    @Column(length = 2)
    private String letterGrade;

    private Boolean isPassed;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    private User updatedBy;

}
