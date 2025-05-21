package com.example.obs.core.security.util;

import com.example.obs.dataAccess.AcademicRepository;
import com.example.obs.dataAccess.AdministrativeStaffRepository;
import com.example.obs.dataAccess.StudentRepository;
import com.example.obs.model.enums.Permission;
import com.example.obs.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SecurityUtils {


    private final StudentRepository studentRepository;
    private final AcademicRepository academicRepository;
    private final AdministrativeStaffRepository adminStaffRepository;
    private final com.example.obs.dataAccess.UserRepository userRepository;

    public boolean hasRole(Role role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String roleName = "ROLE_" + role.name();
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(roleName));
    }

    public boolean hasPermission(Permission permission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(permission.getPermission()));
    }

    public boolean hasAllPermissions(Set<Permission> permissions) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> userPermissions = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        
        return permissions.stream()
                .allMatch(permission -> userPermissions.contains(permission.getPermission()));
    }

    public boolean hasAnyPermission(Set<Permission> permissions) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> userPermissions = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        
        return permissions.stream()
                .anyMatch(permission -> userPermissions.contains(permission.getPermission()));
    }

    public boolean isOwnStudentId(Long studentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        
        return studentRepository.findById(studentId)
                .map(student -> student.getUser().getEmail().equals(currentUserEmail))
                .orElse(false);
    }
    
    public boolean canAccessStudentData(Long studentId) {
        if (isOwnStudentId(studentId)) {
            return true;
        }
        
        if (hasPermission(Permission.ACADEMIC_READ_STUDENT_INFO)) {
            return true;
        }
        
        return hasPermission(Permission.ADMIN_READ_ALL_STUDENTS);
    }
    
    public boolean canCreateStudent() {
        return hasPermission(Permission.ADMIN_CREATE_STUDENT) || 
               hasPermission(Permission.ADMIN_MANAGE_STUDENTS) || 
               hasPermission(Permission.SYSTEM_MANAGE_ALL);
    }
    
    public boolean canUpdateStudent(Long studentId) {
        if (isOwnStudentId(studentId) && hasPermission(Permission.STUDENT_UPDATE_OWN_INFO)) {
            return true;
        }
        
        return hasPermission(Permission.ADMIN_UPDATE_STUDENT) || 
               hasPermission(Permission.ADMIN_MANAGE_STUDENTS) || 
               hasPermission(Permission.SYSTEM_MANAGE_ALL);
    }
    
    public boolean isOwnAcademicId(Long academicId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        
        return academicRepository.findById(academicId)
                .map(academic -> academic.getUser().getEmail().equals(currentUserEmail))
                .orElse(false);
    }

    public boolean canAccessAcademicData(Long academicId) {
        if (isOwnAcademicId(academicId)) {
            return true;
        }
        
        if (hasPermission(Permission.ACAD_AFFAIRS_READ_ALL_ACADEMICS)) {
            return true;
        }
        
        return hasPermission(Permission.ADMIN_READ_ALL_ACADEMICS);
    }
    
    public boolean canCreateAcademic() {
        return hasPermission(Permission.ACAD_AFFAIRS_CREATE_ACADEMIC) || 
               hasPermission(Permission.ADMIN_MANAGE_ACADEMICS) || 
               hasPermission(Permission.SYSTEM_MANAGE_ALL);
    }
    
    public boolean canUpdateAcademic(Long academicId) {
        if (isOwnAcademicId(academicId) && hasPermission(Permission.ACADEMIC_UPDATE_OWN_INFO)) {
            return true;
        }
        
        if (hasPermission(Permission.ACAD_AFFAIRS_UPDATE_ACADEMIC) || 
            hasPermission(Permission.ACAD_AFFAIRS_MANAGE_ACADEMICS)) {
            return true;
        }
        
        return hasPermission(Permission.ADMIN_MANAGE_ACADEMICS) || 
               hasPermission(Permission.SYSTEM_MANAGE_ALL);
    }
    
    public boolean isAcademicAffairsStaff() {
        return hasRole(Role.ACADEMIC_AFFAIRS_STAFF);
    }
    
    public boolean isOwnAdministrativeStaffId(Long staffId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        
        return adminStaffRepository.findById(staffId)
                .map(staff -> staff.getUser().getEmail().equals(currentUserEmail))
                .orElse(false);
    }

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public Long getCurrentUserId() {
        String email = getCurrentUserEmail();
        return userRepository.findByEmail(email)
                .map(user -> user.getId())
                .orElseThrow(() -> new IllegalStateException("Kullanıcı bulunamadı: " + email));
    }

    public boolean isAdmin() {
        return hasRole(Role.ADMIN);
    }

    public boolean isDepartmentChair() {
        return hasRole(Role.DEPARTMENT_CHAIR);
    }
    
    public boolean isDepartmentChairOf(Long departmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        
        return academicRepository.findByUser_Email(currentUserEmail)
                .map(academic -> {
                    return academic.getDepartment() != null && 
                           academic.getDepartment().getHead() != null && 
                           academic.getDepartment().getHead().getId().equals(academic.getId()) &&
                           academic.getDepartment().getId().equals(departmentId);
                })
                .orElse(false);
    }
    
    public boolean canCreateCourse(Long departmentId) {
        if (hasPermission(Permission.SYSTEM_MANAGE_ALL)) {
            return true;
        }
        
        if (hasPermission(Permission.DEPT_CHAIR_CREATE_COURSE) && isDepartmentChairOf(departmentId)) {
            return true;
        }
        
        return false;
    }

    public boolean isOwnStudentByStudentNumber(String studentNumber) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return false;
        }
        String currentUserEmail = authentication.getName();

        return studentRepository.findByStudentNumber(studentNumber)
                .map(student -> student.getUser().getEmail().equals(currentUserEmail))
                .orElse(false);
    }

    public boolean isOwnUserId(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return false;
        }
        String currentUserEmail = authentication.getName();

        return this.userRepository.findById(userId)
                .map(user -> user.getEmail().equals(currentUserEmail))
                .orElse(false);
    }

}
