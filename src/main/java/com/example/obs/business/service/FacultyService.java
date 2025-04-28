package com.example.obs.business.service;

import com.example.obs.business.requests.FacultyCreateRequest;
import com.example.obs.business.requests.FacultyUpdateRequest;
import com.example.obs.business.responses.FacultyResponse;

import java.util.List;

public interface FacultyService {
    List<FacultyResponse> getAll();
    FacultyResponse getById(Long id);
    void add(FacultyCreateRequest facultyCreateRequest);
    void update(FacultyUpdateRequest facultyUpdateRequest);
    void delete(Long id);
}
