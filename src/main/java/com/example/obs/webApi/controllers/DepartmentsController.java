package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.DepartmentCreateRequest;
import com.example.obs.business.requests.DepartmentUpdateRequest;
import com.example.obs.business.responses.DepartmentResponse;
import com.example.obs.business.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@AllArgsConstructor
public class DepartmentsController {

    private DepartmentService departmentService;

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public List<DepartmentResponse> getAll() {
        return departmentService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public DepartmentResponse getById(@PathVariable Long id) {
        return departmentService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('admin:manage_departments', 'system:manage_all')")
    public void add(@RequestBody DepartmentCreateRequest departmentCreateRequest) {
        this.departmentService.add(departmentCreateRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:manage_departments', 'system:manage_all')")
    public void update(@PathVariable Long id, @RequestBody DepartmentUpdateRequest departmentUpdateRequest) {
        departmentUpdateRequest.setId(id);
        this.departmentService.update(departmentUpdateRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:manage_departments', 'system:manage_all')")
    public void delete(@PathVariable Long id) {
        this.departmentService.delete(id);
    }
}
