package com.example.obs.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

import com.example.obs.model.enums.Role;
import com.example.obs.model.enums.UserStatus;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 11, max = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true, length = 11)
    @Size(min = 11, max = 11, message = "Kimlik numarası 11 haneli olmalıdır.")
    private String identityNumber;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Column(nullable = false, length = 15)
    @Size(max = 15, message = "Telefon numarası en fazla 15 karakter olmalıdır.")
    @Pattern(regexp = "^\\+90\\d{10}$", message = "Telefon numarası +90 ile başlamalı ve ardından 10 rakam gelmelidir (örn: +905xxxxxxxxx).")
    private String phone;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    private Boolean hasLoginPermission;

    @Column(nullable = false)
    private Boolean isAdmin = false;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    
    @Column(name = "failed_login_attempts", columnDefinition = "INTEGER DEFAULT 0")
    private Integer failedLoginAttempts = 0;
    
    @Column(name = "account_locked", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean accountLocked = false;
    
    @Column(name = "lock_time")
    private LocalDateTime lockTime;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Student student;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Academic academic;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private AdministrativeStaff administrativeStaff;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        String roleName = role.name();
        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }
        
        Set<GrantedAuthority> authorities = new HashSet<>();
        
        authorities.add(new SimpleGrantedAuthority(roleName));
        
        role.getPermissionStrings().forEach(permission -> 
            authorities.add(new SimpleGrantedAuthority(permission))
        );
        
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked && hasLoginPermission;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
