package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.ScheduleCreateRequest;
import com.example.obs.business.requests.ScheduleUpdateRequest;
import com.example.obs.business.responses.ScheduleResponse;
import com.example.obs.business.service.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@AllArgsConstructor
public class SchedulesController {

    private final ScheduleService scheduleService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin:manage_courses', 'dept_chair:assign_instructor', 'acad_affairs:manage_assignments', 'system:manage_all')")
    public ResponseEntity<ScheduleResponse> add(@RequestBody ScheduleCreateRequest createRequest) {
        return new ResponseEntity<>(scheduleService.add(createRequest), HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('admin:manage_courses', 'dept_chair:assign_instructor', 'acad_affairs:manage_assignments', 'system:manage_all')")
    public ResponseEntity<ScheduleResponse> update(@RequestBody ScheduleUpdateRequest updateRequest) {
        return ResponseEntity.ok(scheduleService.update(updateRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:manage_courses', 'dept_chair:assign_instructor', 'acad_affairs:manage_assignments', 'system:manage_all')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ScheduleResponse>> getAll() {
        return ResponseEntity.ok(scheduleService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ScheduleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.getById(id));
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ScheduleResponse>> getByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(scheduleService.getByCourseId(courseId));
    }

    @GetMapping("/course-instructor/{courseInstructorId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ScheduleResponse>> getByCourseInstructorId(@PathVariable Long courseInstructorId) {
        return ResponseEntity.ok(scheduleService.getByCourseInstructorId(courseInstructorId));
    }
}
