package com.example.obs.business.service;

import com.example.obs.business.requests.GradeScaleCreateRequest;
import com.example.obs.business.requests.GradeScaleUpdateRequest;
import com.example.obs.business.responses.GradeScaleResponse;
import com.example.obs.model.entity.GradeScale;

import java.util.List;

public interface GradeScaleService {
    
    List<GradeScaleResponse> getAll();
    
    GradeScaleResponse getById(Long id);
    
    List<GradeScaleResponse> getAllActive();
    
    List<GradeScaleResponse> getAllDefault();
    
    List<GradeScaleResponse> getByDepartmentId(Long departmentId);
    
    List<GradeScaleResponse> getByCourseId(Long courseId);
    
    List<GradeScaleResponse> getByAcademicId(Long academicId);
    
    GradeScaleResponse getDefaultByDepartmentId(Long departmentId);
    
    GradeScaleResponse getDefaultByCourseId(Long courseId);
    
    void add(GradeScaleCreateRequest gradeScaleCreateRequest);
    
    void update(GradeScaleUpdateRequest gradeScaleUpdateRequest);
    
    void delete(Long id);
    
    GradeScale findApplicableScale(Long courseId, Long departmentId);
}
