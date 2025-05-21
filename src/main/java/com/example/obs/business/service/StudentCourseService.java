package com.example.obs.business.service;

import com.example.obs.business.requests.StudentCourseCreateRequest;
import com.example.obs.business.requests.StudentCourseUpdateRequest;
import com.example.obs.business.responses.StudentCourseResponse;
import com.example.obs.model.enums.Semester;

import java.util.List;

public interface StudentCourseService {
    
    List<StudentCourseResponse> getAll();
    
    StudentCourseResponse getById(Long id);
    
    List<StudentCourseResponse> getByStudentId(Long studentId);
    
    List<StudentCourseResponse> getByCourseId(Long courseId);
    
    List<StudentCourseResponse> getActiveByStudentId(Long studentId);
    
    List<StudentCourseResponse> getActiveByCourseId(Long courseId);
    
    List<StudentCourseResponse> getByStudentIdAndSemester(Long studentId, Semester semester);
    
    List<StudentCourseResponse> getByCourseIdAndSemester(Long courseId, Semester semester);
    
    StudentCourseResponse getByStudentIdAndCourseIdAndSemester(Long studentId, Long courseId, Semester semester);
    
    List<StudentCourseResponse> getAllCompleted();
    
    List<StudentCourseResponse> getCompletedByStudentId(Long studentId);
    
    List<StudentCourseResponse> getByStudentIdAndIsPassed(Long studentId, Boolean isPassed);
    
    void add(StudentCourseCreateRequest studentCourseCreateRequest);
    
    void update(StudentCourseUpdateRequest studentCourseUpdateRequest);
    
    void delete(Long id);
}
