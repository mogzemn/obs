package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.GradeCreateRequest;
import com.example.obs.business.requests.GradeUpdateRequest;
import com.example.obs.business.responses.GradeResponse;
import com.example.obs.business.service.GradeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/grades")
@AllArgsConstructor
public class GradesController {

    private GradeService gradeService;

    @GetMapping("/all")
    public List<GradeResponse> getAll() {
        return gradeService.getAll();
    }

    @GetMapping("/{id}")
    public GradeResponse getById(@PathVariable Long id) {
        return gradeService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void add(@RequestBody GradeCreateRequest gradeCreateRequest) {
        this.gradeService.add(gradeCreateRequest);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody GradeUpdateRequest gradeUpdateRequest) {
        gradeUpdateRequest.setId(id);
        this.gradeService.update(gradeUpdateRequest);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        this.gradeService.delete(id);
    }
}