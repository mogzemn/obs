package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.GradeScaleCreateRequest;
import com.example.obs.business.requests.GradeScaleUpdateRequest;
import com.example.obs.business.responses.GradeScaleResponse;
import com.example.obs.business.service.GradeScaleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gradescales")
@AllArgsConstructor
public class GradeScaleController {
    
    private GradeScaleService gradeScaleService;
    
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<GradeScaleResponse> getAll() {
        return gradeScaleService.getAll();
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public GradeScaleResponse getById(@PathVariable Long id) {
        return gradeScaleService.getById(id);
    }
    
    @GetMapping("/active")
    @PreAuthorize("isAuthenticated()")
    public List<GradeScaleResponse> getAllActive() {
        return gradeScaleService.getAllActive();
    }
    
    @GetMapping("/default")
    @PreAuthorize("isAuthenticated()")
    public List<GradeScaleResponse> getAllDefault() {
        return gradeScaleService.getAllDefault();
    }
    
    @GetMapping("/department/{departmentId}")
    @PreAuthorize("isAuthenticated()")
    public List<GradeScaleResponse> getByDepartmentId(@PathVariable Long departmentId) {
        return gradeScaleService.getByDepartmentId(departmentId);
    }
    
    @GetMapping("/course/{courseId}")
    @PreAuthorize("isAuthenticated()")
    public List<GradeScaleResponse> getByCourseId(@PathVariable Long courseId) {
        return gradeScaleService.getByCourseId(courseId);
    }
    
    @GetMapping("/academic/{academicId}")
    @PreAuthorize("isAuthenticated()")
    public List<GradeScaleResponse> getByAcademicId(@PathVariable Long academicId) {
        return gradeScaleService.getByAcademicId(academicId);
    }
    
    @GetMapping("/department/{departmentId}/default")
    @PreAuthorize("isAuthenticated()")
    public GradeScaleResponse getDefaultByDepartmentId(@PathVariable Long departmentId) {
        return gradeScaleService.getDefaultByDepartmentId(departmentId);
    }
    
    @GetMapping("/course/{courseId}/default")
    @PreAuthorize("isAuthenticated()")
    public GradeScaleResponse getDefaultByCourseId(@PathVariable Long courseId) {
        return gradeScaleService.getDefaultByCourseId(courseId);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('system:manage_all')")
    public void add(@RequestBody GradeScaleCreateRequest gradeScaleCreateRequest) {
        gradeScaleService.add(gradeScaleCreateRequest);
    }
    
    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ACADEMIC', 'ROLE_STUDENT_AFFAIRS_STAFF')")
    public void update(@RequestBody GradeScaleUpdateRequest gradeScaleUpdateRequest) {
        gradeScaleService.update(gradeScaleUpdateRequest);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:manage_all')")
    public void delete(@PathVariable Long id) {
        gradeScaleService.delete(id);
    }
}
