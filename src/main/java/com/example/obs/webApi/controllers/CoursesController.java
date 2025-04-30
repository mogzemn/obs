package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.CourseCreateRequest;
import com.example.obs.business.requests.CourseUpdateRequest;
import com.example.obs.business.responses.CourseResponse;
import com.example.obs.business.service.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/courses")
@AllArgsConstructor
public class CoursesController {

    private CourseService courseService;

    @GetMapping("/all")
    public List<CourseResponse> getAll() {
        return courseService.getAll();
    }

    @GetMapping("/{id}")
    public CourseResponse getById(@PathVariable int id) {
        return courseService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void add(@RequestBody CourseCreateRequest courseCreateRequest) {
        this.courseService.add(courseCreateRequest);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody CourseUpdateRequest courseUpdateRequest) {
        courseUpdateRequest.setId(id);
        this.courseService.update(courseUpdateRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        this.courseService.delete(id);
    }
}