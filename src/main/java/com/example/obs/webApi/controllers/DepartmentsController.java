package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.DepartmentCreateRequest;
import com.example.obs.business.requests.DepartmentUpdateRequest;
import com.example.obs.business.responses.DepartmentResponse;
import com.example.obs.business.service.DepartmentService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@AllArgsConstructor
public class DepartmentsController {

    private DepartmentService departmentService;

    @GetMapping("/all")
    public List<DepartmentResponse> getAll() {
        return departmentService.getAll();
    }

    @GetMapping("/{id}")
    public DepartmentResponse getById(@PathVariable int id) {
        return departmentService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void add(@RequestBody DepartmentCreateRequest departmentCreateRequest) {
        this.departmentService.add(departmentCreateRequest);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody DepartmentUpdateRequest departmentUpdateRequest) {
        departmentUpdateRequest.setId(id);
        this.departmentService.update(departmentUpdateRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        this.departmentService.delete(id);
    }
}