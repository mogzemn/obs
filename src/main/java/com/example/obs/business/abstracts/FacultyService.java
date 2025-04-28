package com.example.obs.business.abstracts;

import com.example.obs.business.requests.FacultyCreateRequest;
import com.example.obs.business.requests.FacultyUpdateRequest;
import com.example.obs.business.requests.StudentCreateRequest;
import com.example.obs.business.requests.StudentUpdateRequest;
import com.example.obs.business.responses.FacultyResponse;
import com.example.obs.business.responses.StudentResponse;

import java.util.List;

public interface FacultyService {
    List<FacultyResponse> getAll();
    FacultyResponse getById(Long id);
    void add(FacultyCreateRequest facultyCreateRequest);
    void update(FacultyUpdateRequest facultyUpdateRequest);
    void delete(Long id);
}
