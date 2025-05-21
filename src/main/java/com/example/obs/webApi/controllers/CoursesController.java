package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.CourseCreateRequest;
import com.example.obs.business.requests.CourseUpdateRequest;
import com.example.obs.business.responses.CourseResponse;
import com.example.obs.business.service.CourseService;
import com.example.obs.core.security.util.SecurityUtils;
import com.example.obs.core.utilities.results.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.obs.model.enums.Permission;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CoursesController {

    private final CourseService courseService;
    private final SecurityUtils securityUtils;

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getAll() {
        List<CourseResponse> courses = courseService.getAll();
        return ResponseEntity.ok(ApiResponse.success(courses));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CourseResponse>> getById(@PathVariable Long id) {
        CourseResponse course = courseService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(course));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('dept_chair:create_course', 'admin:manage_courses', 'system:manage_all')")
    public ResponseEntity<ApiResponse<Void>> add(@RequestBody CourseCreateRequest courseCreateRequest) {

        if (!securityUtils.hasAnyPermission(java.util.Set.of(Permission.ADMIN_MANAGE_COURSES, Permission.SYSTEM_MANAGE_ALL)) &&
            !securityUtils.canCreateCourse(courseCreateRequest.getDepartmentId())) {
            return new ResponseEntity<>(
                ApiResponse.error("Bu bölümde ders oluşturma yetkiniz bulunmamaktadır. Sadece bölüm başkanları kendi bölümlerinde ders oluşturabilir veya genel ders yönetimi yetkisine sahip olmalısınız."),
                HttpStatus.FORBIDDEN);
        }
        
        this.courseService.add(courseCreateRequest);
        return new ResponseEntity<>(ApiResponse.success(null, "Ders başarıyla oluşturuldu"), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('dept_chair:edit_course', 'admin:manage_courses', 'system:manage_all')")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable Long id, @RequestBody CourseUpdateRequest courseUpdateRequest) {
        courseUpdateRequest.setId(id);
        

        CourseResponse existingCourse = courseService.getById(id);
        Long departmentId = existingCourse.getDepartment().getId();
        

        if (!securityUtils.hasAnyPermission(java.util.Set.of(Permission.ADMIN_MANAGE_COURSES,   Permission.SYSTEM_MANAGE_ALL)) &&
            !securityUtils.isDepartmentChairOf(departmentId)) {
            return new ResponseEntity<>(
                ApiResponse.error("Bu dersi güncelleme yetkiniz bulunmamaktadır. Sadece ilgili bölüm başkanları dersleri güncelleyebilir veya genel ders yönetimi yetkisine sahip olmalısınız."),
                HttpStatus.FORBIDDEN);
        }
        
        this.courseService.update(courseUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success(null, "Ders başarıyla güncellendi"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('dept_chair:delete_course', 'admin:manage_courses', 'system:manage_all')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        CourseResponse existingCourse = courseService.getById(id);
        Long departmentId = existingCourse.getDepartment().getId();
        

        if (!securityUtils.hasAnyPermission(java.util.Set.of(Permission.ADMIN_MANAGE_COURSES, Permission.SYSTEM_MANAGE_ALL)) &&
            !securityUtils.isDepartmentChairOf(departmentId)) {
            return new ResponseEntity<>(
                ApiResponse.error("Bu dersi silme yetkiniz bulunmamaktadır. Sadece ilgili bölüm başkanları dersleri silebilir veya genel ders yönetimi yetkisine sahip olmalısınız."),
                HttpStatus.FORBIDDEN);
        }
        
        this.courseService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Ders başarıyla silindi"));
    }
}
