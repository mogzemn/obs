package com.example.obs.business.service;

import com.example.obs.business.requests.ExamCreateRequest;
import com.example.obs.business.requests.ExamUpdateRequest;
import com.example.obs.business.responses.ExamResponse;
import com.example.obs.model.enums.ExamType;
import com.example.obs.model.enums.Semester;

import java.time.LocalDateTime;
import java.util.List;

public interface ExamService {
    List<ExamResponse> getAll();
    ExamResponse getById(Long id);
    void add(ExamCreateRequest examCreateRequest);
    void update(ExamUpdateRequest examUpdateRequest);
    void delete(Long id);
    List<ExamResponse> getByCourseId(Long courseId);
    List<ExamResponse> getByCourseAndSemesterAndAcademicYear(Long courseId, Semester semesterEnum, Long academicYearId);
    List<ExamResponse> getByCourseAndExamType(Long courseId, ExamType examType);
    List<ExamResponse> getByDateRange(LocalDateTime start, LocalDateTime end);
    List<ExamResponse> getBySemesterAndAcademicYear(com.example.obs.model.enums.Semester semesterEnum, Long academicYearId);
    List<ExamResponse> getByDepartmentId(Long departmentId);
    List<ExamResponse> getByStudentId(Long studentId);
    List<ExamResponse> getByAcademicYearIdAndSemesterEnum(Long academicYearId, Semester semesterEnum);
    List<ExamResponse> getAllForAllDepartments();
}
