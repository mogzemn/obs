package com.example.obs.business.abstracts;

import com.example.obs.business.requests.GradeCreateRequest;
import com.example.obs.business.requests.GradeUpdateRequest;
import com.example.obs.business.requests.StudentCreateRequest;
import com.example.obs.business.requests.StudentUpdateRequest;
import com.example.obs.business.responses.GradeResponse;
import com.example.obs.business.responses.StudentResponse;

import java.util.List;

public interface GradeService {
    List<GradeResponse> getAll();
    GradeResponse getById(Long id);
    void add(GradeCreateRequest gradeCreateRequest);
    void update(GradeUpdateRequest gradeUpdateRequest);
    void delete(Long id);
}
