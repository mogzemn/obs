package com.example.obs.business.service.implementation;

import com.example.obs.business.service.GradeService;
import com.example.obs.business.requests.GradeCreateRequest;
import com.example.obs.business.requests.GradeUpdateRequest;
import com.example.obs.business.responses.GradeResponse;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dataAccess.GradeRepository;
import com.example.obs.model.entity.Grade;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GradeServiceImpl implements GradeService {
    private GradeRepository gradeRepository;
    private ModelMapperService modelMapperService;

    @Override
    public List<GradeResponse> getAll() {
        List<Grade> grades = gradeRepository.findAll();

        List<GradeResponse> responses = grades.stream()
                .map(grade -> this.modelMapperService.forResponse()
                        .map(grade, GradeResponse.class)).collect(Collectors.toList());

        return responses;
    }

    @Override
    public GradeResponse getById(int id) {
        Grade grade = gradeRepository.findById(id).orElseThrow();

        GradeResponse response = this.modelMapperService.forResponse()
                .map(grade, GradeResponse.class);

        return response;
    }

    @Override
    public void add(GradeCreateRequest gradeCreateRequest) {
        Grade grade = this.modelMapperService.forRequest()
                .map(gradeCreateRequest, Grade.class);

        gradeRepository.save(grade);
    }

    @Override
    public void update(GradeUpdateRequest gradeUpdateRequest) {
        Grade grade = this.modelMapperService.forRequest()
                .map(gradeUpdateRequest, Grade.class);

        gradeRepository.save(grade);
    }

    @Override
    public void delete(int id) {
        this.gradeRepository.deleteById(id);
    }
}