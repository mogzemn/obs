package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.CourseWeightCreateRequest;
import com.example.obs.business.requests.CourseWeightUpdateRequest;
import com.example.obs.business.responses.CourseWeightResponse;
import com.example.obs.business.service.CourseWeightService;
import com.example.obs.core.utilities.results.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-weights")
@AllArgsConstructor
public class CourseWeightsController {

    private final CourseWeightService courseWeightService;

    @PostMapping
    public ResponseEntity<ApiResponse<CourseWeightResponse>> add(@Valid @RequestBody CourseWeightCreateRequest request) {
        ApiResponse<CourseWeightResponse> response = courseWeightService.add(request);
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseWeightResponse>> update(@PathVariable Long id, @Valid @RequestBody CourseWeightUpdateRequest request) {
        if (request.getId() == null) {
            request.setId(id);
        } else if (!request.getId().equals(id)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Path ID ile istek gövdesindeki ID uyuşmuyor."));
        }
        ApiResponse<CourseWeightResponse> response = courseWeightService.update(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        ApiResponse<Void> response = courseWeightService.delete(id);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseWeightResponse>> getById(@PathVariable Long id) {
        ApiResponse<CourseWeightResponse> response = courseWeightService.getById(id);
        if (response.isSuccess() && response.getData() != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Ders ağırlığı bulunamadı: ID " + id));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseWeightResponse>>> getAll() {
        ApiResponse<List<CourseWeightResponse>> response = courseWeightService.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-course/{courseId}")
    public ResponseEntity<ApiResponse<CourseWeightResponse>> getByCourseId(@PathVariable Long courseId) {
        ApiResponse<CourseWeightResponse> response = courseWeightService.getByCourseId(courseId);
        if (response.isSuccess() && response.getData() != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Derse ait ağırlık bulunamadı: Ders ID " + courseId));
    }
}
