package com.example.obs.business.service;

import com.example.obs.business.requests.CourseInstructorCreateRequest;
import com.example.obs.business.requests.CourseInstructorUpdateRequest;
import com.example.obs.business.responses.CourseInstructorResponse;

import java.util.List;

public interface CourseInstructorService {
    List<CourseInstructorResponse> getAll();
    CourseInstructorResponse getById(Long id);
    void add(CourseInstructorCreateRequest courseInstructorCreateRequest);
    void update(CourseInstructorUpdateRequest courseInstructorUpdateRequest);
    void delete(Long id);
}
