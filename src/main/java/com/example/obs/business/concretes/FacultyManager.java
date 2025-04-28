package com.example.obs.business.concretes;

import com.example.obs.business.abstracts.FacultyService;
import com.example.obs.business.requests.FacultyCreateRequest;
import com.example.obs.business.requests.FacultyUpdateRequest;
import com.example.obs.business.responses.FacultyResponse;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dateAccess.FacultyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FacultyManager implements FacultyService {
    private FacultyRepository facultyRepository;
    private ModelMapperService modelMapperService;

    @Override
    public List<FacultyResponse> getAll() {
        return List.of();
    }

    @Override
    public FacultyResponse getById(Long id) {
        return null;
    }

    @Override
    public void add(FacultyCreateRequest facultyCreateRequest) {

    }

    @Override
    public void update(FacultyUpdateRequest facultyUpdateRequest) {

    }

    @Override
    public void delete(Long id) {

    }
}
