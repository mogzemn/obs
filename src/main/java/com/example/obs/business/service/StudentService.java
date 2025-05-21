package com.example.obs.business.service;

import com.example.obs.business.requests.StudentCreateRequest;
import com.example.obs.business.requests.StudentUpdateRequest;
import com.example.obs.business.responses.StudentResponse;
import java.util.List;

public interface StudentService {
    List<StudentResponse> getAll();
    StudentResponse getById(Long id);
    StudentResponse getByStudentNumber(String studentNumber);
    void add(StudentCreateRequest studentCreateRequest);
    void update(StudentUpdateRequest studentUpdateRequest);
    void updateByStudentNumber(String studentNumber, StudentUpdateRequest studentUpdateRequest);
    void delete(Long id);
}
