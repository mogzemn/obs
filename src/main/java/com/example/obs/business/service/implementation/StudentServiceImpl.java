package com.example.obs.business.service.implementation;

import com.example.obs.business.service.StudentService;
import com.example.obs.business.requests.StudentCreateRequest;
import com.example.obs.business.requests.StudentUpdateRequest;
import com.example.obs.business.responses.StudentResponse;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dateAccess.StudentRepository;
import com.example.obs.model.entity.Student;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
    private StudentRepository studentRepository;
    private ModelMapperService modelMapperService;

    @Override
    public List<StudentResponse> getAll() {
        List<Student> students = studentRepository.findAll();

        List<StudentResponse> responses = students.stream()
                .map(student -> this.modelMapperService.forResponse()
                        .map(student, StudentResponse.class)).collect(Collectors.toList());

        return responses;
    }

    @Override
    public StudentResponse getById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow();

        StudentResponse response = this.modelMapperService.forResponse()
                .map(student, StudentResponse.class);

        return response;
    }

    @Override
    public void add(StudentCreateRequest studentCreateRequest) {
        Student student = this.modelMapperService.forRequest()
                .map(studentCreateRequest, Student.class);

        studentRepository.save(student);
    }

    @Override
    public void update(StudentUpdateRequest studentUpdateRequest) {
        Student student = this.modelMapperService.forRequest()
                .map(studentUpdateRequest, Student.class);

        studentRepository.save(student);
    }

    @Override
    public void delete(Long id) {
        this.studentRepository.deleteById(id);
    }
}