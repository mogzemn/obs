package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.StudentCreateRequest;
import com.example.obs.business.requests.StudentUpdateRequest;
import com.example.obs.business.responses.StudentResponse;
import com.example.obs.business.service.StudentService;
import com.example.obs.core.security.util.SecurityUtils;
import com.example.obs.core.utilities.results.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final SecurityUtils securityUtils;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin:read_all_students', 'academic:read_student_info', 'system:manage_all')")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAll() {
        List<StudentResponse> students = studentService.getAll();
        return ResponseEntity.ok(ApiResponse.success(students));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read_all_students', 'academic:read_student_info', 'system:manage_all') or (@securityUtils.isOwnStudentId(#id) and hasAuthority('student:read_own_info'))")
    public ResponseEntity<ApiResponse<StudentResponse>> getById(@PathVariable Long id) {

        if (!securityUtils.canAccessStudentData(id)) {
            return new ResponseEntity<>(ApiResponse.error("Bu öğrenci bilgilerine erişim yetkiniz bulunmamaktadır"), HttpStatus.FORBIDDEN);
        }
        
        StudentResponse student = studentService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(student));
    }

    @GetMapping("/number/{studentNumber}")
    @PreAuthorize("hasAnyAuthority('admin:read_all_students', 'academic:read_student_info', 'system:manage_all') or (@securityUtils.isOwnStudentByStudentNumber(#studentNumber) and hasAuthority('student:read_own_by_student_number'))")
    public ResponseEntity<ApiResponse<StudentResponse>> getByStudentNumber(@PathVariable String studentNumber) {
        StudentResponse student = studentService.getByStudentNumber(studentNumber);
        return ResponseEntity.ok(ApiResponse.success(student));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATIVE_MANAGER', 'STUDENT_AFFAIRS_STAFF') or hasAuthority('admin:manage_students') or hasAuthority('admin:create_student') or hasAuthority('system:manage_all')")
    public ResponseEntity<ApiResponse<Void>> add(@RequestBody StudentCreateRequest studentCreateRequest) {

        this.studentService.add(studentCreateRequest);
        return new ResponseEntity<>(ApiResponse.success(null, "Öğrenci başarıyla eklendi"), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATIVE_MANAGER', 'STUDENT_AFFAIRS_STAFF') or hasAuthority('admin:manage_students') or hasAuthority('admin:update_student') or hasAuthority('system:manage_all') or (@securityUtils.isOwnStudentId(#id) and hasAuthority('student:update_own_info'))")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable Long id, @RequestBody StudentUpdateRequest request) {

        if (!securityUtils.canUpdateStudent(id)) {
            return new ResponseEntity<>(ApiResponse.error("Bu öğrenci bilgisini güncelleme yetkiniz bulunmamaktadır"), HttpStatus.FORBIDDEN);
        }
        
        request.setId(id);
        studentService.update(request);
        return ResponseEntity.ok(ApiResponse.success(null, "Öğrenci bilgileri başarıyla güncellendi"));
    }

    @PutMapping("/number/{studentNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATIVE_MANAGER', 'STUDENT_AFFAIRS_STAFF') or hasAuthority('admin:manage_students') or hasAuthority('admin:update_student') or hasAuthority('system:manage_all')")
    public ResponseEntity<ApiResponse<Void>> updateByStudentNumber(@PathVariable String studentNumber, @RequestBody StudentUpdateRequest studentUpdateRequest) {
        this.studentService.updateByStudentNumber(studentNumber, studentUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success(null, "Öğrenci başarıyla güncellendi"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATIVE_MANAGER', 'STUDENT_AFFAIRS_STAFF') or hasAuthority('admin:manage_students') or hasAuthority('system:manage_all')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        this.studentService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Öğrenci başarıyla silindi"));
    }
}
