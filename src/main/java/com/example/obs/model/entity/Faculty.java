package com.example.obs.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "faculties")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Faculty extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String facultyName;

    @Column(nullable = false, unique = true, length = 2)
    private String facultyCode;

    @ManyToOne
    @JoinColumn(name = "dean_id")
    private Academic dean;

    @Column(nullable = false)
    private Boolean isActive;
}
