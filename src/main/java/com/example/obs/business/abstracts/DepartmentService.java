package com.example.obs.business.abstracts;

import com.example.obs.business.requests.DepartmentCreateRequest;
import com.example.obs.business.requests.DepartmentUpdateRequest;
import com.example.obs.business.requests.StudentCreateRequest;
import com.example.obs.business.requests.StudentUpdateRequest;
import com.example.obs.business.responses.DepartmentResponse;
import com.example.obs.business.responses.StudentResponse;
import com.example.obs.dateAccess.DepartmentRepository;

import java.util.List;

public interface DepartmentService {
    List<DepartmentResponse> getAll();
    DepartmentResponse getById(Long id);
    void add(DepartmentCreateRequest departmentCreateRequest);
    void update(DepartmentUpdateRequest departmentUpdateRequest);
    void delete(Long id);
}
