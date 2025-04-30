package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.StudentCreateRequest;
import com.example.obs.business.requests.StudentUpdateRequest;
import com.example.obs.business.responses.StudentResponse;
import com.example.obs.business.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@AllArgsConstructor
public class StudentsController {

    private StudentService studentService;

    @GetMapping("/all")
    public List<StudentResponse> getAll() {
        return studentService.getAll();
    }

    @GetMapping("/{id}")
    public StudentResponse getById(@PathVariable int id) {
        return studentService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void add(@RequestBody StudentCreateRequest studentCreateRequest) {
        this.studentService.add(studentCreateRequest);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody StudentUpdateRequest studentUpdateRequest) {
        studentUpdateRequest.setId(id);
        this.studentService.update(studentUpdateRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        this.studentService.delete(id);
    }
}