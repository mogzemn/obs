package com.example.obs.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Course extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String courseCode;

    @Column(nullable = false, length = 50)
    private String courseName;

    @Column(nullable = false)
    private Integer credits;

    @Column(nullable = false)
    private Integer ects;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Lob
    @Column(nullable = true)
    private String description;

    @Column(nullable = true, length = 50)
    private String classroom;

    @Column(nullable = false)
    private Boolean isActive;
}
