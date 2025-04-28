package com.example.obs.business.concretes;

import com.example.obs.business.abstracts.AcademicService;
import com.example.obs.business.requests.AcademicCreateRequest;
import com.example.obs.business.requests.AcademicUpdateRequest;
import com.example.obs.business.responses.AcademicResponse;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dateAccess.AcademicRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class AcademicManager implements AcademicService {
    private AcademicRepository academicRepository;
    private ModelMapperService modelMapperService;

    @Override
    public List<AcademicResponse> getAll() {
        return List.of();
    }

    @Override
    public AcademicResponse getById(Long id) {
        return null;
    }

    @Override
    public void add(AcademicCreateRequest academicCreateRequest) {

    }

    @Override
    public void update(AcademicUpdateRequest academicUpdateRequest) {

    }

    @Override
    public void delete(Long id) {

    }
}
