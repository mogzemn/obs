package com.example.obs.business.abstracts;

import com.example.obs.business.requests.CourseCreateRequest;
import com.example.obs.business.requests.CourseUpdateRequest;
import com.example.obs.business.requests.FacultyCreateRequest;
import com.example.obs.business.requests.FacultyUpdateRequest;
import com.example.obs.business.responses.CourseResponse;
import com.example.obs.business.responses.FacultyResponse;

import java.util.List;

public interface CourseService {
    List<CourseResponse> getAll();
    CourseResponse getById(Long id);
    void add(CourseCreateRequest courseCreateRequest);
    void update(CourseUpdateRequest courseUpdateRequest);
    void delete(Long id);
}
