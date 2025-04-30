package com.example.obs.business.service;

import com.example.obs.business.requests.GradeCreateRequest;
import com.example.obs.business.requests.GradeUpdateRequest;
import com.example.obs.business.responses.GradeResponse;

import java.util.List;

public interface GradeService {
    List<GradeResponse> getAll();
    GradeResponse getById(int id);
    void add(GradeCreateRequest gradeCreateRequest);
    void update(GradeUpdateRequest gradeUpdateRequest);
    void delete(int id);
}
