package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.AcademicYearCreateRequest;
import com.example.obs.business.requests.AcademicYearUpdateRequest;
import com.example.obs.business.responses.AcademicYearResponse;
import com.example.obs.business.service.AcademicYearService;
import com.example.obs.core.utilities.results.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academic-years")
@AllArgsConstructor
public class AcademicYearsController {

    private final AcademicYearService academicYearService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AcademicYearResponse>> add(@Valid @RequestBody AcademicYearCreateRequest request) {
        AcademicYearResponse response = academicYearService.add(request);
        return new ResponseEntity<>(ApiResponse.success(response, "Akademik yıl başarıyla eklendi"), HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AcademicYearResponse>> update(@Valid @RequestBody AcademicYearUpdateRequest request) {
        AcademicYearResponse response = academicYearService.update(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Akademik yıl başarıyla güncellendi"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        academicYearService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Akademik yıl başarıyla silindi"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<AcademicYearResponse>> getById(@PathVariable Long id) {
        AcademicYearResponse response = academicYearService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<AcademicYearResponse>>> getAll() {
        List<AcademicYearResponse> responses = academicYearService.getAll();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/by-name")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<AcademicYearResponse>> getByName(@RequestParam String name) {
        AcademicYearResponse response = academicYearService.getByName(name);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/by-years")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<AcademicYearResponse>> getByStartYearAndEndYear(@RequestParam Integer startYear, @RequestParam Integer endYear) {
        AcademicYearResponse response = academicYearService.getByStartYearAndEndYear(startYear, endYear);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
