package com.example.obs.model.enums;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Role {

    ADMIN(Stream.of(
            Permission.SYSTEM_ADMIN_ACCESS,
            Permission.SYSTEM_MANAGE_ALL,
            Permission.ADMIN_READ_OWN_INFO,
            Permission.ADMIN_UPDATE_OWN_INFO,
            Permission.ADMIN_READ_ALL_STUDENTS,
            Permission.ADMIN_MANAGE_STUDENTS,
            Permission.ADMIN_CREATE_STUDENT,
            Permission.ADMIN_UPDATE_STUDENT,
            Permission.ADMIN_READ_ALL_ACADEMICS,
            Permission.ADMIN_MANAGE_ACADEMICS,
            Permission.ADMIN_READ_ALL_COURSES,
            Permission.ADMIN_MANAGE_COURSES,
            Permission.ADMIN_MANAGE_DEPARTMENTS,
            Permission.ADMIN_MANAGE_FACULTIES
    ).collect(Collectors.toSet())),
    
    STUDENT(Stream.of(
            Permission.STUDENT_READ_OWN_INFO,
            Permission.STUDENT_UPDATE_OWN_INFO,
            Permission.STUDENT_READ_OWN_COURSES,
            Permission.STUDENT_READ_OWN_GRADES,
            Permission.STUDENT_READ_OWN_ATTENDANCE,
            Permission.STUDENT_REGISTER_COURSE,
            Permission.STUDENT_READ_OWN_BY_STUDENT_NUMBER
    ).collect(Collectors.toSet())),
    
    ACADEMIC(Stream.of(
            Permission.ACADEMIC_READ_OWN_INFO,
            Permission.ACADEMIC_UPDATE_OWN_INFO,
            Permission.ACADEMIC_READ_OWN_COURSES,
            Permission.ACADEMIC_READ_STUDENT_INFO,
            Permission.ACADEMIC_MANAGE_GRADES,
            Permission.ACADEMIC_MANAGE_ATTENDANCE,
            Permission.ACADEMIC_MANAGE_COURSE_CONTENT
    ).collect(Collectors.toSet())),
    

    DEPARTMENT_CHAIR(Stream.concat(
            ACADEMIC.getPermissions().stream(),
            Stream.of(
                Permission.DEPT_CHAIR_CREATE_COURSE,
                Permission.DEPT_CHAIR_EDIT_COURSE,
                Permission.DEPT_CHAIR_DELETE_COURSE,
                Permission.DEPT_CHAIR_ASSIGN_INSTRUCTOR,
                Permission.DEPT_CHAIR_VIEW_DEPARTMENT_STATS,
                Permission.DEPT_CHAIR_APPROVE_CURRICULUM
            )
        ).collect(Collectors.toSet())),


    ADMINISTRATIVE_STAFF(Stream.of(

            Permission.ADMIN_READ_OWN_INFO,
            Permission.ADMIN_UPDATE_OWN_INFO
    ).collect(Collectors.toSet())),
    

    STUDENT_AFFAIRS_STAFF(Stream.concat(
            ADMINISTRATIVE_STAFF.getPermissions().stream(),
            Stream.of(
                Permission.ADMIN_READ_ALL_STUDENTS,
                Permission.ADMIN_MANAGE_STUDENTS,
                Permission.ADMIN_CREATE_STUDENT,
                Permission.ADMIN_UPDATE_STUDENT,
                Permission.STUDENT_AFFAIRS_MANAGE_REGISTRATION,
                Permission.STUDENT_AFFAIRS_MANAGE_TRANSCRIPTS,
                Permission.STUDENT_AFFAIRS_ISSUE_DOCUMENTS
            )
        ).collect(Collectors.toSet())),


    ACADEMIC_AFFAIRS_STAFF(Stream.concat(
            ADMINISTRATIVE_STAFF.getPermissions().stream(),
            Stream.of(
                Permission.ACAD_AFFAIRS_READ_ALL_ACADEMICS,
                Permission.ACAD_AFFAIRS_CREATE_ACADEMIC,
                Permission.ACAD_AFFAIRS_UPDATE_ACADEMIC,
                Permission.ACAD_AFFAIRS_MANAGE_ACADEMICS,
                Permission.ACAD_AFFAIRS_READ_ACADEMIC_COURSES,
                Permission.ACAD_AFFAIRS_READ_DEPARTMENTS,
                Permission.ACAD_AFFAIRS_MANAGE_PERSONNEL_FILES,
                Permission.ACAD_AFFAIRS_MANAGE_ASSIGNMENTS
            )
        ).collect(Collectors.toSet())),


    ADMINISTRATIVE_MANAGER(Stream.concat(
            Stream.concat(
                STUDENT_AFFAIRS_STAFF.getPermissions().stream(),
                ACADEMIC_AFFAIRS_STAFF.getPermissions().stream()
            ),
            Stream.of(
                Permission.ADMIN_READ_ALL_COURSES,
                Permission.ADMIN_MANAGE_COURSES,
                Permission.ADMIN_MANAGE_DEPARTMENTS,
                Permission.ADMIN_MANAGE_FACULTIES
            )
        ).collect(Collectors.toSet())),
    

    ROLE_ADMIN(ADMIN.getPermissions()),
    ROLE_STUDENT(STUDENT.getPermissions()),
    ROLE_ACADEMIC(ACADEMIC.getPermissions()),
    ROLE_DEPARTMENT_CHAIR(DEPARTMENT_CHAIR.getPermissions()),
    ROLE_ADMINISTRATIVE(ADMINISTRATIVE_MANAGER.getPermissions());
    
    private final Set<Permission> permissions;
    
    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }
    
    public Set<Permission> getPermissions() {
        return permissions;
    }
    
    public Set<String> getPermissionStrings() {
        return getPermissions().stream()
                .map(Permission::getPermission)
                .collect(Collectors.toSet());
    }
    

    public static Role[] getAdministrativeRoles() {
        return new Role[] {
            ADMINISTRATIVE_STAFF,
            STUDENT_AFFAIRS_STAFF, 
            ACADEMIC_AFFAIRS_STAFF, 
            ADMINISTRATIVE_MANAGER
        };
    }
    

    public boolean isAdministrativeRole() {
        for (Role role : getAdministrativeRoles()) {
            if (this == role) {
                return true;
            }
        }
        return false;
    }
}
