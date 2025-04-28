package com.example.obs.business.abstracts;

import com.example.obs.business.requests.CourseInstructorCreateRequest;
import com.example.obs.business.requests.CourseInstructorUpdateRequest;
import com.example.obs.business.requests.FacultyCreateRequest;
import com.example.obs.business.requests.FacultyUpdateRequest;
import com.example.obs.business.responses.CourseInstructorResponse;
import com.example.obs.business.responses.FacultyResponse;

import java.util.List;

public interface CourseInstructorService {
    List<CourseInstructorResponse> getAll();
    CourseInstructorResponse getById(Long id);
    void add(CourseInstructorCreateRequest courseInstructorCreateRequest);
    void update(CourseInstructorUpdateRequest courseInstructorUpdateRequest);
    void delete(Long id);
}
