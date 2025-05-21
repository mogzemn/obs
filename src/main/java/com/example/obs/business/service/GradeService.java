package com.example.obs.business.service;

import com.example.obs.business.requests.GradeCreateRequest;
import com.example.obs.business.requests.GradeUpdateRequest;
import com.example.obs.business.responses.GradeResponse;

import java.util.List;

public interface GradeService {
    List<GradeResponse> getAll();
    GradeResponse getById(Long id);
    List<GradeResponse> getByStudentId(Long studentId);
    void add(GradeCreateRequest gradeCreateRequest);
    void update(GradeUpdateRequest gradeUpdateRequest);
    void updateByStudentNumberAndCourseCode(String studentNumber, String courseCode, GradeUpdateRequest gradeUpdateRequest);
    void delete(Long id);
}
