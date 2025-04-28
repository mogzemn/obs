package com.example.obs.business.service.impl;

import com.example.obs.business.service.AcademicService;
import com.example.obs.business.requests.AcademicCreateRequest;
import com.example.obs.business.requests.AcademicUpdateRequest;
import com.example.obs.business.responses.AcademicResponse;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dateAccess.AcademicRepository;
import com.example.obs.model.entity.Academic;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class AcademicManager implements AcademicService {
    private AcademicRepository academicRepository;
    private ModelMapperService modelMapperService;

    @Override
    public List<AcademicResponse> getAll() {

        List<Academic> academics = academicRepository.findAll();

        List<AcademicResponse> academicResponses = academics.stream()
                .map(academic -> this.modelMapperService.forResponse()
                .map(academic ,AcademicResponse.class )).collect(java.util.stream.Collectors.toList());

        return academicResponses;
    }

    @Override
    public AcademicResponse getById(Long id) {
        Academic academic = academicRepository.findById(id).orElseThrow();

        AcademicResponse academicResponse = this.modelMapperService.forResponse()
                .map(academic, AcademicResponse.class);

        return academicResponse;
    }

    @Override
    public void add(AcademicCreateRequest academicCreateRequest) {
        Academic academic = this.modelMapperService.forRequest()
                .map(academicCreateRequest, Academic.class);

        academicRepository.save(academic);
    }

    @Override
    public void update(AcademicUpdateRequest academicUpdateRequest) {

        Academic academic = this.modelMapperService.forRequest()
                .map(academicUpdateRequest, Academic.class);

        academicRepository.save(academic);

    }

    @Override
    public void delete(Long id) {

        this.academicRepository.deleteById(id);
    }
}
