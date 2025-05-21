package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.GradeCreateRequest;
import com.example.obs.business.requests.GradeUpdateRequest;
import com.example.obs.business.responses.GradeResponse;
import com.example.obs.business.service.GradeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@AllArgsConstructor
public class GradesController {

    private GradeService gradeService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('system:manage_all', 'ROLE_ADMIN')") 
    public List<GradeResponse> getAll() {
        return gradeService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('academic:manage_grades', 'student:read_own_grades', 'system:manage_all')") 
    public GradeResponse getById(@PathVariable Long id) {
        return gradeService.getById(id);
    }
    
    @GetMapping("/student/{studentId}")
    @PreAuthorize("(@securityUtils.isOwnStudentId(#studentId) and hasAuthority('student:read_own_grades')) or hasAuthority('academic:read_student_info') or hasAuthority('system:manage_all') or hasRole('ROLE_ADMIN')")
    public List<GradeResponse> getByStudentId(@PathVariable Long studentId) {
        return gradeService.getByStudentId(studentId);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('academic:manage_grades', 'system:manage_all', 'ROLE_ADMIN')") 
    public void add(@RequestBody GradeCreateRequest gradeCreateRequest) {
        this.gradeService.add(gradeCreateRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('academic:manage_grades', 'system:manage_all', 'ROLE_ADMIN')")
    public void update(@PathVariable Long id, @RequestBody GradeUpdateRequest gradeUpdateRequest) {
        gradeUpdateRequest.setId(id);
        this.gradeService.update(gradeUpdateRequest);
    }

    @PutMapping("/student/{studentNumber}/course/{courseCode}")
    @PreAuthorize("hasAnyAuthority('academic:manage_grades', 'system:manage_all', 'ROLE_ADMIN')")
    public void updateByStudentNumberAndCourseCode(
            @PathVariable String studentNumber,
            @PathVariable String courseCode,
            @RequestBody GradeUpdateRequest gradeUpdateRequest) {
        this.gradeService.updateByStudentNumberAndCourseCode(studentNumber, courseCode, gradeUpdateRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('academic:manage_grades', 'system:manage_all', 'ROLE_ADMIN')") 
    public void delete(@PathVariable Long id) {
        this.gradeService.delete(id);
    }
}
