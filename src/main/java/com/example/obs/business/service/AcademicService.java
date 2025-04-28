package com.example.obs.business.service;

import com.example.obs.business.requests.AcademicCreateRequest;
import com.example.obs.business.requests.AcademicUpdateRequest;
import com.example.obs.business.responses.AcademicResponse;

import java.util.List;

public interface AcademicService {
    List<AcademicResponse> getAll();
    AcademicResponse getById(Long id);
    void add(AcademicCreateRequest academicCreateRequest);
    void update(AcademicUpdateRequest academicUpdateRequest);
    void delete(Long id);
}
