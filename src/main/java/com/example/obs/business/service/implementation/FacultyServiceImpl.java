package com.example.obs.business.service.implementation;

import com.example.obs.business.service.FacultyService;
import com.example.obs.business.requests.FacultyCreateRequest;
import com.example.obs.business.requests.FacultyUpdateRequest;
import com.example.obs.business.responses.FacultyResponse;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dateAccess.FacultyRepository;
import com.example.obs.model.entity.Faculty;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FacultyServiceImpl implements FacultyService {
    private FacultyRepository facultyRepository;
    private ModelMapperService modelMapperService;

    @Override
    public List<FacultyResponse> getAll() {
        List<Faculty> faculties = facultyRepository.findAll();

        List<FacultyResponse> responses = faculties.stream()
                .map(faculty -> this.modelMapperService.forResponse()
                        .map(faculty, FacultyResponse.class)).collect(Collectors.toList());

        return responses;
    }

    @Override
    public FacultyResponse getById(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow();

        FacultyResponse response = this.modelMapperService.forResponse()
                .map(faculty, FacultyResponse.class);

        return response;
    }

    @Override
    public void add(FacultyCreateRequest facultyCreateRequest) {
        Faculty faculty = this.modelMapperService.forRequest()
                .map(facultyCreateRequest, Faculty.class);

        facultyRepository.save(faculty);
    }

    @Override
    public void update(FacultyUpdateRequest facultyUpdateRequest) {
        Faculty faculty = this.modelMapperService.forRequest()
                .map(facultyUpdateRequest, Faculty.class);

        facultyRepository.save(faculty);
    }

    @Override
    public void delete(Long id) {
        this.facultyRepository.deleteById(id);
    }
}