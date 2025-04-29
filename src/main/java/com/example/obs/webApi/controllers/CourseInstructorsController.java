package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.CourseInstructorCreateRequest;
import com.example.obs.business.requests.CourseInstructorUpdateRequest;
import com.example.obs.business.responses.CourseInstructorResponse;
import com.example.obs.business.service.CourseInstructorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/course-instructors")
@AllArgsConstructor
public class CourseInstructorsController {

    private CourseInstructorService courseInstructorService;

    @GetMapping("/all")
    public List<CourseInstructorResponse> getAll() {
        return courseInstructorService.getAll();
    }

    @GetMapping("/{id}")
    public CourseInstructorResponse getById(@PathVariable Long id) {
        return courseInstructorService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void add(@RequestBody CourseInstructorCreateRequest courseInstructorCreateRequest) {
        this.courseInstructorService.add(courseInstructorCreateRequest);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody CourseInstructorUpdateRequest courseInstructorUpdateRequest) {
        courseInstructorUpdateRequest.setId(id);
        this.courseInstructorService.update(courseInstructorUpdateRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        this.courseInstructorService.delete(id);
    }
}