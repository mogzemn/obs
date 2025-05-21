package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.ExamCreateRequest;
import com.example.obs.business.requests.ExamUpdateRequest;
import com.example.obs.business.responses.ExamResponse;
import com.example.obs.business.service.ExamService;
import com.example.obs.core.security.util.SecurityUtils;
import com.example.obs.model.enums.ExamType;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamsController {

    private final ExamService examService;
    private final SecurityUtils securityUtils;

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public List<ExamResponse> getAll() {
        return examService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ExamResponse getById(@PathVariable Long id) {
        return examService.getById(id);
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("isAuthenticated()")
    public List<ExamResponse> getByCourseId(@PathVariable Long courseId) {
        return examService.getByCourseId(courseId);
    }

    @GetMapping("/course/{courseId}/semester/{semesterEnum}/academic-year/{academicYearId}")
    @PreAuthorize("isAuthenticated()")
    public List<ExamResponse> getByCourseAndSemesterAndAcademicYear(
            @PathVariable Long courseId,
            @PathVariable com.example.obs.model.enums.Semester semesterEnum,
            @PathVariable Long academicYearId) {
        return examService.getByCourseAndSemesterAndAcademicYear(courseId, semesterEnum, academicYearId);
    }

    @GetMapping("/course/{courseId}/exam-type/{examType}")
    @PreAuthorize("isAuthenticated()")
    public List<ExamResponse> getByCourseAndExamType(
            @PathVariable Long courseId,
            @PathVariable ExamType examType) {
        return examService.getByCourseAndExamType(courseId, examType);
    }

    @GetMapping("/date-range")
    @PreAuthorize("isAuthenticated()")
    public List<ExamResponse> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return examService.getByDateRange(start, end);
    }

    @GetMapping("/semester/{semesterEnum}/academic-year/{academicYearId}")
    @PreAuthorize("isAuthenticated()")
    public List<ExamResponse> getBySemesterAndAcademicYear(
            @PathVariable com.example.obs.model.enums.Semester semesterEnum,
            @PathVariable Long academicYearId) {
        return examService.getBySemesterAndAcademicYear(semesterEnum, academicYearId);
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ExamResponse>> getByDepartmentId(@PathVariable Long departmentId) {
        List<ExamResponse> responses = examService.getByDepartmentId(departmentId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ExamResponse>> getByStudentId(@PathVariable Long studentId) {
        List<ExamResponse> responses = examService.getByStudentId(studentId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/academic-year/{academicYearId}/semester/{semesterEnum}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ExamResponse>> getByAcademicYearIdAndSemesterEnum(
            @PathVariable Long academicYearId,
            @PathVariable com.example.obs.model.enums.Semester semesterEnum) {
        List<ExamResponse> responses = examService.getByAcademicYearIdAndSemesterEnum(academicYearId, semesterEnum);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/all-departments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ExamResponse>> getAllForAllDepartments() {
        List<ExamResponse> responses = examService.getAllForAllDepartments();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('academic:manage_course_content', 'admin:manage_courses', 'system:manage_all')")
    public ResponseEntity<?> add(@RequestBody ExamCreateRequest examCreateRequest) {

        examCreateRequest.setCreatedById(securityUtils.getCurrentUserId());
        
        this.examService.add(examCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('academic:manage_course_content', 'admin:manage_courses', 'system:manage_all')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ExamUpdateRequest examUpdateRequest) {
        examUpdateRequest.setId(id);

        examUpdateRequest.setUpdatedById(securityUtils.getCurrentUserId());
        
        this.examService.update(examUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('academic:manage_course_content', 'admin:manage_courses', 'system:manage_all')")
    public ResponseEntity<?> delete(@PathVariable Long id) {        
        this.examService.delete(id);
        return ResponseEntity.ok().build();
    }
}
