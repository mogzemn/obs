package com.example.obs.model.enums;

public enum Permission {

    STUDENT_READ_OWN_INFO("student:read_own"),
    STUDENT_UPDATE_OWN_INFO("student:update_own"),
    STUDENT_READ_OWN_COURSES("student:read_own_courses"),
    STUDENT_READ_OWN_GRADES("student:read_own_grades"),
    STUDENT_READ_OWN_ATTENDANCE("student:read_own_attendance"),
    STUDENT_REGISTER_COURSE("student:register_course"),
    STUDENT_READ_OWN_BY_STUDENT_NUMBER("student:read_own_by_student_number"),
    

    ACADEMIC_READ_OWN_INFO("academic:read_own"),
    ACADEMIC_UPDATE_OWN_INFO("academic:update_own"),
    ACADEMIC_READ_OWN_COURSES("academic:read_own_courses"),
    ACADEMIC_READ_STUDENT_INFO("academic:read_student_info"),
    ACADEMIC_MANAGE_GRADES("academic:manage_grades"),
    ACADEMIC_MANAGE_ATTENDANCE("academic:manage_attendance"),
    ACADEMIC_MANAGE_COURSE_CONTENT("academic:manage_course_content"),
    

    DEPT_CHAIR_CREATE_COURSE("dept_chair:create_course"),
    DEPT_CHAIR_EDIT_COURSE("dept_chair:edit_course"),
    DEPT_CHAIR_DELETE_COURSE("dept_chair:delete_course"),
    DEPT_CHAIR_ASSIGN_INSTRUCTOR("dept_chair:assign_instructor"),
    DEPT_CHAIR_VIEW_DEPARTMENT_STATS("dept_chair:view_department_stats"),
    DEPT_CHAIR_APPROVE_CURRICULUM("dept_chair:approve_curriculum"),
    

    ACAD_AFFAIRS_READ_OWN_INFO("acad_affairs:read_own"),
    ACAD_AFFAIRS_UPDATE_OWN_INFO("acad_affairs:update_own"),
    ACAD_AFFAIRS_READ_ALL_ACADEMICS("acad_affairs:read_all_academics"),
    ACAD_AFFAIRS_CREATE_ACADEMIC("acad_affairs:create_academic"),
    ACAD_AFFAIRS_UPDATE_ACADEMIC("acad_affairs:update_academic"),
    ACAD_AFFAIRS_MANAGE_ACADEMICS("acad_affairs:manage_academics"),
    ACAD_AFFAIRS_READ_ACADEMIC_COURSES("acad_affairs:read_academic_courses"),
    ACAD_AFFAIRS_READ_DEPARTMENTS("acad_affairs:read_departments"),
    ACAD_AFFAIRS_MANAGE_PERSONNEL_FILES("acad_affairs:manage_personnel_files"),
    ACAD_AFFAIRS_MANAGE_ASSIGNMENTS("acad_affairs:manage_assignments"),


    STUDENT_AFFAIRS_MANAGE_REGISTRATION("student_affairs:manage_registration"),
    STUDENT_AFFAIRS_MANAGE_TRANSCRIPTS("student_affairs:manage_transcripts"),
    STUDENT_AFFAIRS_ISSUE_DOCUMENTS("student_affairs:issue_documents"),
    

    ADMIN_READ_OWN_INFO("admin:read_own"),
    ADMIN_UPDATE_OWN_INFO("admin:update_own"),
    ADMIN_READ_ALL_STUDENTS("admin:read_all_students"),
    ADMIN_MANAGE_STUDENTS("admin:manage_students"),
    ADMIN_CREATE_STUDENT("admin:create_student"),
    ADMIN_UPDATE_STUDENT("admin:update_student"),
    ADMIN_READ_ALL_ACADEMICS("admin:read_all_academics"),
    ADMIN_MANAGE_ACADEMICS("admin:manage_academics"),
    ADMIN_READ_ALL_COURSES("admin:read_all_courses"),
    ADMIN_MANAGE_COURSES("admin:manage_courses"),
    ADMIN_MANAGE_DEPARTMENTS("admin:manage_departments"),
    ADMIN_MANAGE_FACULTIES("admin:manage_faculties"),
    

    SYSTEM_ADMIN_ACCESS("system:admin_access"),
    SYSTEM_MANAGE_ALL("system:manage_all");
    
    private final String permission;
    
    Permission(String permission) {
        this.permission = permission;
    }
    
    public String getPermission() {
        return permission;
    }
}
