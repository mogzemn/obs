package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.FacultyCreateRequest;
import com.example.obs.business.requests.FacultyUpdateRequest;
import com.example.obs.business.responses.FacultyResponse;
import com.example.obs.business.service.FacultyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculties")
@AllArgsConstructor
public class FacultiesController {

    private FacultyService facultyService;

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public List<FacultyResponse> getAll() {
        return facultyService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public FacultyResponse getById(@PathVariable Long id) {
        return facultyService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('admin:manage_faculties', 'system:manage_all')")
    public void add(@RequestBody FacultyCreateRequest facultyCreateRequest) {
        this.facultyService.add(facultyCreateRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:manage_faculties', 'system:manage_all')")
    public void update(@PathVariable Long id, @RequestBody FacultyUpdateRequest facultyUpdateRequest) {
        facultyUpdateRequest.setId(id);
        this.facultyService.update(facultyUpdateRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:manage_faculties', 'system:manage_all')")
    public void delete(@PathVariable Long id) {
        this.facultyService.delete(id);
    }
}
