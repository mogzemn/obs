package com.example.obs.business.service.impl;

import com.example.obs.business.service.GradeService;
import com.example.obs.business.requests.GradeCreateRequest;
import com.example.obs.business.requests.GradeUpdateRequest;
import com.example.obs.business.responses.GradeResponse;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dateAccess.GradeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GradeManager implements GradeService {
    private GradeRepository gradeRepository;
    private ModelMapperService modelMapperService;

    @Override
    public List<GradeResponse> getAll() {
        return List.of();
    }

    @Override
    public GradeResponse getById(Long id) {
        return null;
    }

    @Override
    public void add(GradeCreateRequest gradeCreateRequest) {

    }

    @Override
    public void update(GradeUpdateRequest gradeUpdateRequest) {

    }

    @Override
    public void delete(Long id) {

    }
}
