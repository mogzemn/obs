package com.example.obs.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "administrative_staff")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdministrativeStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(nullable = false, unique = true, length = 20)
    private String staffNumber;

    @Column(length = 255)
    private String jobTitle;
}