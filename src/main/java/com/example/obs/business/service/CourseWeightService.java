package com.example.obs.business.service;

import com.example.obs.business.requests.CourseWeightCreateRequest;
import com.example.obs.business.requests.CourseWeightUpdateRequest;
import com.example.obs.business.responses.CourseWeightResponse;
import com.example.obs.core.utilities.results.ApiResponse;  

import java.util.List;

public interface CourseWeightService {
    ApiResponse<CourseWeightResponse> add(CourseWeightCreateRequest courseWeightCreateRequest);
    ApiResponse<CourseWeightResponse> update(CourseWeightUpdateRequest courseWeightUpdateRequest);
    ApiResponse<Void> delete(Long id); 
    ApiResponse<CourseWeightResponse> getById(Long id);
    ApiResponse<List<CourseWeightResponse>> getAll();
    ApiResponse<CourseWeightResponse> getByCourseId(Long courseId);
}
