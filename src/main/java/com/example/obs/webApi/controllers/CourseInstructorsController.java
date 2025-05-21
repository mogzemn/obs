package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.CourseInstructorCreateRequest;
import com.example.obs.business.requests.CourseInstructorUpdateRequest;
import com.example.obs.business.responses.CourseInstructorResponse;
import com.example.obs.business.responses.CourseResponse;
import com.example.obs.business.service.CourseInstructorService;
import com.example.obs.business.service.CourseService;
import com.example.obs.core.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-instructors")
@RequiredArgsConstructor
public class CourseInstructorsController {

    private final CourseInstructorService courseInstructorService;
    private final CourseService courseService;
    private final SecurityUtils securityUtils;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATIVE_MANAGER', 'ACADEMIC_AFFAIRS_STAFF', 'DEPARTMENT_CHAIR') or hasAuthority('system:manage_all')")
    public List<CourseInstructorResponse> getAll() {
        return courseInstructorService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATIVE_MANAGER', 'ACADEMIC_AFFAIRS_STAFF', 'DEPARTMENT_CHAIR', 'ACADEMIC', 'STUDENT') or hasAuthority('system:manage_all')")
    public CourseInstructorResponse getById(@PathVariable Long id) {
        return courseInstructorService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('dept_chair:assign_instructor') or hasAuthority('system:manage_all')")
    public ResponseEntity<?> add(@RequestBody CourseInstructorCreateRequest courseInstructorCreateRequest) {

        CourseResponse course = courseService.getById(courseInstructorCreateRequest.getCourseId());
        Long departmentId = course.getDepartment().getId();
        

        if (!securityUtils.isDepartmentChairOf(departmentId) && !securityUtils.hasPermission(com.example.obs.model.enums.Permission.SYSTEM_MANAGE_ALL)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Bu kursa öğretim üyesi atama yetkiniz bulunmamaktadır. Sadece ilgili bölüm başkanları atama yapabilir.");
        }
        

        this.courseInstructorService.add(courseInstructorCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('dept_chair:assign_instructor') or hasAuthority('system:manage_all')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CourseInstructorUpdateRequest courseInstructorUpdateRequest) {
        courseInstructorUpdateRequest.setId(id);
        

        CourseInstructorResponse existingAssignment = courseInstructorService.getById(id);

        CourseResponse course = courseService.getById(existingAssignment.getCourse().getId());
        Long departmentId = course.getDepartment().getId();
        

        if (!securityUtils.isDepartmentChairOf(departmentId) && !securityUtils.hasPermission(com.example.obs.model.enums.Permission.SYSTEM_MANAGE_ALL)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Bu kurs atamasını güncelleme yetkiniz bulunmamaktadır. Sadece ilgili bölüm başkanları güncelleme yapabilir.");
        }
        
        this.courseInstructorService.update(courseInstructorUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('dept_chair:assign_instructor') or hasAuthority('system:manage_all')")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        CourseInstructorResponse existingAssignment = courseInstructorService.getById(id);

        CourseResponse course = courseService.getById(existingAssignment.getCourse().getId());
        Long departmentId = course.getDepartment().getId();
        

        if (!securityUtils.isDepartmentChairOf(departmentId) && !securityUtils.hasPermission(com.example.obs.model.enums.Permission.SYSTEM_MANAGE_ALL)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Bu kurs atamasını silme yetkiniz bulunmamaktadır. Sadece ilgili bölüm başkanları silme işlemi yapabilir.");
        }
        
        this.courseInstructorService.delete(id);
        return ResponseEntity.ok().build();
    }
}
