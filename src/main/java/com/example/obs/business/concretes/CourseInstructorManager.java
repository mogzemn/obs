package com.example.obs.business.concretes;

import com.example.obs.business.abstracts.CourseInstructorService;
import com.example.obs.business.requests.CourseInstructorCreateRequest;
import com.example.obs.business.requests.CourseInstructorUpdateRequest;
import com.example.obs.business.responses.CourseInstructorResponse;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dateAccess.CourseInstructorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseInstructorManager implements CourseInstructorService {
    private CourseInstructorRepository courseInstructorRepository;
    private ModelMapperService modelMapperService;

    @Override
    public List<CourseInstructorResponse> getAll() {
        return List.of();
    }

    @Override
    public CourseInstructorResponse getById(Long id) {
        return null;
    }

    @Override
    public void add(CourseInstructorCreateRequest courseInstructorCreateRequest) {

    }

    @Override
    public void update(CourseInstructorUpdateRequest courseInstructorUpdateRequest) {

    }

    @Override
    public void delete(Long id) {

    }
}
