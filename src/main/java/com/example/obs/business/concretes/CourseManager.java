package com.example.obs.business.concretes;

import com.example.obs.business.abstracts.CourseService;
import com.example.obs.business.requests.CourseCreateRequest;
import com.example.obs.business.requests.CourseUpdateRequest;
import com.example.obs.business.responses.CourseResponse;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dateAccess.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseManager implements CourseService {
    private CourseRepository courseRepository;
    private ModelMapperService modelMapperService;

    @Override
    public List<CourseResponse> getAll() {
        return List.of();
    }

    @Override
    public CourseResponse getById(Long id) {
        return null;
    }

    @Override
    public void add(CourseCreateRequest courseCreateRequest) {

    }

    @Override
    public void update(CourseUpdateRequest courseUpdateRequest) {

    }

    @Override
    public void delete(Long id) {

    }
}
