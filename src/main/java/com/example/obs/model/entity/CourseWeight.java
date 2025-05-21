package com.example.obs.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "course_weights")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseWeight extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "course_id", nullable = false, unique = true)
    private Course course;

    @Column(name = "midterm_weight", nullable = false, precision = 5, scale = 2)
    private BigDecimal midtermWeight;

    @Column(name = "final_weight", nullable = false, precision = 5, scale = 2)
    private BigDecimal finalWeight;

    @Column(name = "assignment_weight", nullable = false, precision = 5, scale = 2)
    private BigDecimal assignmentWeight;


    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    private User updatedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseWeight that = (CourseWeight) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode(); 
    }
}
