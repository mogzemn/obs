package com.example.obs.model.entity;

import com.example.obs.model.enums.Semester;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "academic_years")
@Getter
@Setter
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicYear extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ElementCollection(targetClass = com.example.obs.model.enums.Semester.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "academic_year_semesters", joinColumns = @JoinColumn(name = "academic_year_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "semester")
    private List<Semester> semesters;

    @PrePersist
    @PreUpdate
    private void generateName() {
        if (startDate != null && endDate != null) {
            int startYear = startDate.getYear();
            int endYear = endDate.getYear();
            this.name = startYear + "-" + endYear;
        }
    }
}
