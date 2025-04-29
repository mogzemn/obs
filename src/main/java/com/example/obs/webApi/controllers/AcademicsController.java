package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.AcademicCreateRequest;
import com.example.obs.business.requests.AcademicUpdateRequest;
import com.example.obs.business.responses.AcademicResponse;
import com.example.obs.business.service.AcademicService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/academics")
@AllArgsConstructor
public class AcademicsController {

    private AcademicService academicService;

    @GetMapping("/all")
    public List<AcademicResponse> getAll() {
        return academicService.getAll();
    }

    @GetMapping("/{id}")
    public AcademicResponse getById(@PathVariable Long id) {
        return academicService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void add(@RequestBody AcademicCreateRequest academicCreateRequest) {
        this.academicService.add(academicCreateRequest);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody AcademicUpdateRequest academicUpdateRequest) {
        academicUpdateRequest.setId(id);
        this.academicService.update(academicUpdateRequest);
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        this.academicService.delete(id);
    }
}