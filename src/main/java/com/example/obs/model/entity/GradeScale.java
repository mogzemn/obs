package com.example.obs.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "grade_scales")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GradeScale extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "academic_id", nullable = false)
    private Academic academic;
    
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    @Column(name = "is_default", nullable = false, columnDefinition = "boolean default true")
    private Boolean isDefault = true;
    

    @Column(name = "aa_min", nullable = false)
    private Double aaMin;
    
    @Column(name = "ba_min", nullable = false)
    private Double baMin;
    
    @Column(name = "bb_min", nullable = false)
    private Double bbMin;
    
    @Column(name = "cb_min", nullable = false)
    private Double cbMin;
    
    @Column(name = "cc_min", nullable = false)
    private Double ccMin;
    
    @Column(name = "dc_min", nullable = false)
    private Double dcMin;
    
    @Column(name = "dd_min", nullable = false)
    private Double ddMin;
    
    @Column(name = "ff_min", nullable = false)
    private Double ffMin;

    @ManyToOne
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    private User updatedBy;

}
