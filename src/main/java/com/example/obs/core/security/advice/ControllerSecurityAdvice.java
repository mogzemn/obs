package com.example.obs.core.security.advice;

import com.example.obs.core.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;

@Configuration
@RequiredArgsConstructor
public class ControllerSecurityAdvice {

    private final SecurityUtils securityUtils;

    @PreAuthorize("hasAuthority('system:admin_access')")
    public void adminOnlyOperation() {
    }

    @PreAuthorize("hasAuthority('academic:read_student_info')")
    public void academicOnlyOperation() {
    }

    @PreAuthorize("hasAuthority('student:read_own') and @securityUtils.isOwnStudentId(#studentId)")
    public void studentOwnDataOperation(Long studentId) {
    }

    @PreAuthorize("hasAnyAuthority('admin:read_all_students', 'academic:read_student_info')")
    public void adminOrAcademicOperation() {
    }

    public void checkStudentDataAccess(Long studentId) {
        if (!securityUtils.canAccessStudentData(studentId)) {
            throw new AccessDeniedException("Bu öğrenci verisine erişim yetkiniz bulunmamaktadır.");
        }
        
    }

}
