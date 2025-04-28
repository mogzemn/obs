package com.example.obs.business.service.implementation;

import com.example.obs.business.service.CourseInstructorService;
import com.example.obs.business.requests.CourseInstructorCreateRequest;
import com.example.obs.business.requests.CourseInstructorUpdateRequest;
import com.example.obs.business.responses.CourseInstructorResponse;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dateAccess.CourseInstructorRepository;
import com.example.obs.model.entity.CourseInstructor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseInstructorServiceImpl implements CourseInstructorService {
    private CourseInstructorRepository courseInstructorRepository;
    private ModelMapperService modelMapperService;

    @Override
    public List<CourseInstructorResponse> getAll() {
       List<CourseInstructor> courseInstructors = courseInstructorRepository.findAll();

       List<CourseInstructorResponse> responses = courseInstructors.stream()
               .map(courseInstructor -> this.modelMapperService.forResponse()
                       .map(courseInstructor, CourseInstructorResponse.class)).collect(Collectors.toList());

       return responses;
    }

    @Override
    public CourseInstructorResponse getById(Long id) {
        CourseInstructor courseInstructor = courseInstructorRepository.findById(id).orElseThrow();

        CourseInstructorResponse response = this.courseInstructorRepository.findById(id)
                .map(courseInstructorr -> this.modelMapperService.forResponse()
                        .map(courseInstructorr, CourseInstructorResponse.class)).orElseThrow();

        return response;
    }

    @Override
    public void add(CourseInstructorCreateRequest courseInstructorCreateRequest) {
        CourseInstructor courseInstructor = this.modelMapperService.forRequest()
                .map(courseInstructorCreateRequest, CourseInstructor.class);

        courseInstructorRepository.save(courseInstructor);

    }

    @Override
    public void update(CourseInstructorUpdateRequest courseInstructorUpdateRequest) {
        CourseInstructor courseInstructor = this.modelMapperService.forRequest()
                .map(courseInstructorUpdateRequest, CourseInstructor.class);

        courseInstructorRepository.save(courseInstructor);
    }

    @Override
    public void delete(Long id) {
        this.courseInstructorRepository.deleteById(id);
    }
}
