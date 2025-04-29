package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.FacultyCreateRequest;
import com.example.obs.business.requests.FacultyUpdateRequest;
import com.example.obs.business.responses.FacultyResponse;
import com.example.obs.business.service.FacultyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/faculties")
@AllArgsConstructor
public class FacultysController {

    private FacultyService facultyService;

    @GetMapping("/all")
    public List<FacultyResponse> getAll() {
        return facultyService.getAll();
    }

    @GetMapping("/{id}")
    public FacultyResponse getById(@PathVariable Long id) {
        return facultyService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void add(@RequestBody FacultyCreateRequest facultyCreateRequest) {
        this.facultyService.add(facultyCreateRequest);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody FacultyUpdateRequest facultyUpdateRequest) {
        facultyUpdateRequest.setId(id);
        this.facultyService.update(facultyUpdateRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        this.facultyService.delete(id);
    }
}