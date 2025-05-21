package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.StudentCourseCreateRequest;
import com.example.obs.business.requests.StudentCourseUpdateRequest;
import com.example.obs.business.responses.StudentCourseResponse;
import com.example.obs.business.service.StudentCourseService;
import com.example.obs.model.enums.Semester;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/studentcourses")
@AllArgsConstructor
public class StudentCourseController {
    
    private StudentCourseService studentCourseService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT_AFFAIRS_STAFF', 'ADMINISTRATIVE_MANAGER') or hasAuthority('system:manage_all')")
    public List<StudentCourseResponse> getAll() {
        return studentCourseService.getAll();
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('student:read_own_courses') or hasAuthority('academic:read_student_info') or hasAnyRole('ADMIN', 'STUDENT_AFFAIRS_STAFF', 'ADMINISTRATIVE_MANAGER') or hasAuthority('system:manage_all')") 
    public StudentCourseResponse getById(@PathVariable Long id) {
        return studentCourseService.getById(id);
    }
    
    @GetMapping("/student/{studentId}")
    @PreAuthorize("(@securityUtils.isOwnStudentId(#studentId) and hasAuthority('student:read_own_courses')) or hasAuthority('academic:read_student_info') or hasAnyRole('ADMIN', 'STUDENT_AFFAIRS_STAFF', 'ADMINISTRATIVE_MANAGER') or hasAuthority('system:manage_all')")
    public List<StudentCourseResponse> getByStudentId(@PathVariable Long studentId) {
        return studentCourseService.getByStudentId(studentId);
    }
    
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAuthority('academic:read_own_courses') or hasAuthority('student:read_own_courses') or hasAnyRole('ADMIN', 'STUDENT_AFFAIRS_STAFF', 'ADMINISTRATIVE_MANAGER') or hasAuthority('system:manage_all')")
    public List<StudentCourseResponse> getByCourseId(@PathVariable Long courseId) {
        return studentCourseService.getByCourseId(courseId);
    }
    
    @GetMapping("/student/{studentId}/active")
    @PreAuthorize("(@securityUtils.isOwnStudentId(#studentId) and hasAuthority('student:read_own_courses')) or hasAuthority('academic:read_student_info') or hasAnyRole('ADMIN', 'STUDENT_AFFAIRS_STAFF', 'ADMINISTRATIVE_MANAGER') or hasAuthority('system:manage_all')")
    public List<StudentCourseResponse> getActiveByStudentId(@PathVariable Long studentId) {
        return studentCourseService.getActiveByStudentId(studentId);
    }
    
    @GetMapping("/course/{courseId}/active")
    @PreAuthorize("hasAuthority('academic:read_own_courses') or hasAuthority('student:read_own_courses') or hasAnyRole('ADMIN', 'STUDENT_AFFAIRS_STAFF', 'ADMINISTRATIVE_MANAGER') or hasAuthority('system:manage_all')")
    public List<StudentCourseResponse> getActiveByCourseId(@PathVariable Long courseId) {
        return studentCourseService.getActiveByCourseId(courseId);
    }
    
    @GetMapping("/student/{studentId}/semester/{semester}")
    @PreAuthorize("(@securityUtils.isOwnStudentId(#studentId) and hasAuthority('student:read_own_courses')) or hasAuthority('academic:read_student_info') or hasAnyRole('ADMIN', 'STUDENT_AFFAIRS_STAFF', 'ADMINISTRATIVE_MANAGER') or hasAuthority('system:manage_all')")
    public List<StudentCourseResponse> getByStudentIdAndSemester(
            @PathVariable Long studentId, 
            @PathVariable Semester semester) {
        return studentCourseService.getByStudentIdAndSemester(studentId, semester);
    }
    
    @GetMapping("/course/{courseId}/semester/{semester}")
    @PreAuthorize("hasAuthority('academic:read_own_courses') or hasAuthority('student:read_own_courses') or hasAnyRole('ADMIN', 'STUDENT_AFFAIRS_STAFF', 'ADMINISTRATIVE_MANAGER') or hasAuthority('system:manage_all')")
    public List<StudentCourseResponse> getByCourseIdAndSemester(
            @PathVariable Long courseId, 
            @PathVariable Semester semester) {
        return studentCourseService.getByCourseIdAndSemester(courseId, semester);
    }
    
    @GetMapping("/student/{studentId}/course/{courseId}/semester/{semester}")
    @PreAuthorize("(@securityUtils.isOwnStudentId(#studentId) and hasAuthority('student:read_own_courses')) or hasAuthority('academic:read_student_info') or hasAnyRole('ADMIN', 'STUDENT_AFFAIRS_STAFF', 'ADMINISTRATIVE_MANAGER') or hasAuthority('system:manage_all')")
    public StudentCourseResponse getByStudentIdAndCourseIdAndSemester(
            @PathVariable Long studentId, 
            @PathVariable Long courseId, 
            @PathVariable Semester semester) {
        return studentCourseService.getByStudentIdAndCourseIdAndSemester(studentId, courseId, semester);
    }
    
    @GetMapping("/completed")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT_AFFAIRS_STAFF', 'ADMINISTRATIVE_MANAGER') or hasAuthority('system:manage_all')")
    public List<StudentCourseResponse> getAllCompleted() {
        return studentCourseService.getAllCompleted();
    }
    
    @GetMapping("/student/{studentId}/completed")
    @PreAuthorize("(@securityUtils.isOwnStudentId(#studentId) and hasAuthority('student:read_own_courses')) or hasAuthority('academic:read_student_info') or hasAnyRole('ADMIN', 'STUDENT_AFFAIRS_STAFF', 'ADMINISTRATIVE_MANAGER') or hasAuthority('system:manage_all')")
    public List<StudentCourseResponse> getCompletedByStudentId(@PathVariable Long studentId) {
        return studentCourseService.getCompletedByStudentId(studentId);
    }
    
    @GetMapping("/student/{studentId}/passed/{isPassed}")
    @PreAuthorize("(@securityUtils.isOwnStudentId(#studentId) and hasAuthority('student:read_own_courses')) or hasAuthority('academic:read_student_info') or hasAnyRole('ADMIN', 'STUDENT_AFFAIRS_STAFF', 'ADMINISTRATIVE_MANAGER') or hasAuthority('system:manage_all')")
    public List<StudentCourseResponse> getByStudentIdAndIsPassed(
            @PathVariable Long studentId, 
            @PathVariable Boolean isPassed) {
        return studentCourseService.getByStudentIdAndIsPassed(studentId, isPassed);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('student:register_course') or hasAuthority('student_affairs:manage_registration') or hasAuthority('admin:manage_students') or hasAuthority('system:manage_all')")
    public void add(@RequestBody StudentCourseCreateRequest studentCourseCreateRequest) {
        studentCourseService.add(studentCourseCreateRequest);
    }
    
    @PutMapping
    @PreAuthorize("hasAnyAuthority('academic:manage_grades', 'student_affairs:manage_registration', 'admin:manage_students', 'system:manage_all')")
    public void update(@RequestBody StudentCourseUpdateRequest studentCourseUpdateRequest) {
        studentCourseService.update(studentCourseUpdateRequest);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('student_affairs:manage_registration', 'admin:manage_students', 'system:manage_all')")
    public void delete(@PathVariable Long id) {
        studentCourseService.delete(id);
    }
}
