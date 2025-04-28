package com.example.obs.business.service.impl;

import com.example.obs.business.service.StudentService;
import com.example.obs.business.requests.StudentCreateRequest;
import com.example.obs.business.requests.StudentUpdateRequest;
import com.example.obs.business.responses.StudentResponse;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dateAccess.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentManager implements StudentService {
    private StudentRepository studentRepository;
    private ModelMapperService modelMapperService;

    @Override
    public List<StudentResponse> getAll() {
        return List.of();
    }

    @Override
    public StudentResponse getById(Long id) {
        return null;
    }

    @Override
    public void add(StudentCreateRequest studentCreateRequest) {

    }

    @Override
    public void update(StudentUpdateRequest studentUpdateRequest) {

    }

    @Override
    public void delete(Long id) {

    }
}
