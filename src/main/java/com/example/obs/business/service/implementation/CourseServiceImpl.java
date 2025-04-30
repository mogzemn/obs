package com.example.obs.business.service.implementation;

import com.example.obs.business.service.CourseService;
import com.example.obs.business.requests.CourseCreateRequest;
import com.example.obs.business.requests.CourseUpdateRequest;
import com.example.obs.business.responses.CourseResponse;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dataAccess.CourseRepository;
import com.example.obs.model.entity.Course;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {
    private CourseRepository courseRepository;
    private ModelMapperService modelMapperService;

    @Override
    public List<CourseResponse> getAll() {
        List<Course> courses = courseRepository.findAll();

        List<CourseResponse> responses = courses.stream()
                .map(course -> this.modelMapperService.forResponse()
                        .map(course, CourseResponse.class)).collect(Collectors.toList());

        return responses;
    }

    @Override
    public CourseResponse getById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow();

        CourseResponse response = this.modelMapperService.forResponse()
                .map(course, CourseResponse.class);

        return response;
    }

    @Override
    public void add(CourseCreateRequest courseCreateRequest) {
        Course course = this.modelMapperService.forRequest()
                .map(courseCreateRequest, Course.class);

        courseRepository.save(course);
    }

    @Override
    public void update(CourseUpdateRequest courseUpdateRequest) {
        Course course = this.modelMapperService.forRequest()
                .map(courseUpdateRequest, Course.class);

        courseRepository.save(course);
    }

    @Override
    public void delete(Long id) {
        this.courseRepository.deleteById(id);
    }
}