package com.example.obs.business.service;

import com.example.obs.business.requests.DepartmentCreateRequest;
import com.example.obs.business.requests.DepartmentUpdateRequest;
import com.example.obs.business.responses.DepartmentResponse;

import java.util.List;

public interface DepartmentService {
    List<DepartmentResponse> getAll();
    DepartmentResponse getById(Long id);
    void add(DepartmentCreateRequest departmentCreateRequest);
    void update(DepartmentUpdateRequest departmentUpdateRequest);
    void delete(Long id);
}
