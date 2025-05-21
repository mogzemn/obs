package com.example.obs.model.entity;

import com.example.obs.model.enums.AdministrativeUnit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "administrative_staff")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class    AdministrativeStaff extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true, length = 11)
    private String staffNumber;
    
    @Column(nullable = false, unique = true, length = 11)
    private String administrativeNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AdministrativeUnit unitName;
}
