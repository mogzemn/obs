package com.example.obs.business.service.implementation;

import com.example.obs.business.requests.CourseWeightCreateRequest;
import com.example.obs.business.requests.CourseWeightUpdateRequest;
import com.example.obs.business.responses.CourseWeightResponse;
import com.example.obs.business.service.CourseWeightService;
import com.example.obs.core.exceptions.NotFoundException;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.core.utilities.results.ApiResponse; 
import com.example.obs.dataAccess.CourseRepository;
import com.example.obs.dataAccess.CourseWeightRepository;
import com.example.obs.model.entity.Course;
import com.example.obs.model.entity.CourseWeight;
import lombok.AllArgsConstructor; 
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor 
public class CourseWeightServiceImpl implements CourseWeightService {

    private final CourseWeightRepository courseWeightRepository;
    private final CourseRepository courseRepository;
    private final ModelMapperService modelMapperService;

        

    @Override
    public ApiResponse<CourseWeightResponse> add(CourseWeightCreateRequest courseWeightCreateRequest) {
        BigDecimal totalWeight = courseWeightCreateRequest.getMidtermWeight()
                .add(courseWeightCreateRequest.getFinalWeight())
                .add(courseWeightCreateRequest.getAssignmentWeight());
        if (totalWeight.compareTo(new BigDecimal("100.00")) != 0) {
            return ApiResponse.error("Toplam ağırlık %100 olmalıdır.");
        }

        Course course = courseRepository.findById(courseWeightCreateRequest.getCourseId())
                .orElseThrow(() -> new NotFoundException("Ders bulunamadı: ID " + courseWeightCreateRequest.getCourseId()));

        if (courseWeightRepository.findByCourseId(courseWeightCreateRequest.getCourseId()).isPresent()) {
            return ApiResponse.error("Bu ders için ağırlıklar zaten tanımlanmış.");
        }

        CourseWeight courseWeight = modelMapperService.forRequest().map(courseWeightCreateRequest, CourseWeight.class);
        courseWeight.setId(null); 
        courseWeight.setCourse(course);

        CourseWeight savedCourseWeight = courseWeightRepository.save(courseWeight);
        CourseWeightResponse response = modelMapperService.forResponse().map(savedCourseWeight, CourseWeightResponse.class);
        response.setCourseName(course.getCourseName());
        return ApiResponse.success(response, "Ders ağırlıkları başarıyla eklendi.");
    }

    @Override
    public ApiResponse<CourseWeightResponse> update(CourseWeightUpdateRequest courseWeightUpdateRequest) {
        CourseWeight existingCourseWeight = courseWeightRepository.findById(courseWeightUpdateRequest.getId())
                .orElseThrow(() -> new NotFoundException("Ders ağırlığı bulunamadı: ID " + courseWeightUpdateRequest.getId()));

        BigDecimal midtermWeight = courseWeightUpdateRequest.getMidtermWeight() != null ? courseWeightUpdateRequest.getMidtermWeight() : existingCourseWeight.getMidtermWeight();
        BigDecimal finalWeight = courseWeightUpdateRequest.getFinalWeight() != null ? courseWeightUpdateRequest.getFinalWeight() : existingCourseWeight.getFinalWeight();
        BigDecimal assignmentWeight = courseWeightUpdateRequest.getAssignmentWeight() != null ? courseWeightUpdateRequest.getAssignmentWeight() : existingCourseWeight.getAssignmentWeight();

        BigDecimal totalWeight = midtermWeight.add(finalWeight).add(assignmentWeight);
        if (totalWeight.compareTo(new BigDecimal("100.00")) != 0) {
            return ApiResponse.error("Toplam ağırlık %100 olmalıdır.");
        }

        modelMapperService.forRequest().map(courseWeightUpdateRequest, existingCourseWeight);

        CourseWeight updatedCourseWeight = courseWeightRepository.save(existingCourseWeight);
        CourseWeightResponse response = modelMapperService.forResponse().map(updatedCourseWeight, CourseWeightResponse.class);
        response.setCourseName(updatedCourseWeight.getCourse().getCourseName());
        return ApiResponse.success(response, "Ders ağırlıkları başarıyla güncellendi.");
    }

    @Override
    public ApiResponse<Void> delete(Long id) {
        if (!courseWeightRepository.existsById(id)) {
            return ApiResponse.error("Silinecek ders ağırlığı bulunamadı: ID " + id);
        }
        courseWeightRepository.deleteById(id);
        return ApiResponse.success(null, "Ders ağırlığı başarıyla silindi."); 
    }

    @Override
    public ApiResponse<CourseWeightResponse> getById(Long id) {
        CourseWeight courseWeight = courseWeightRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ders ağırlığı bulunamadı: ID " + id));
        CourseWeightResponse response = modelMapperService.forResponse().map(courseWeight, CourseWeightResponse.class);
        response.setCourseName(courseWeight.getCourse().getCourseName());
        return ApiResponse.success(response, "Ders ağırlığı bulundu.");
    }

    @Override
    public ApiResponse<List<CourseWeightResponse>> getAll() {
        List<CourseWeight> courseWeights = courseWeightRepository.findAll();
        List<CourseWeightResponse> responses = courseWeights.stream()
                .map(cw -> {
                    CourseWeightResponse res = modelMapperService.forResponse().map(cw, CourseWeightResponse.class);
                    res.setCourseName(cw.getCourse().getCourseName());
                    return res;
                })
                .collect(Collectors.toList());
        return ApiResponse.success(responses, "Tüm ders ağırlıkları listelendi.");
    }

    @Override
    public ApiResponse<CourseWeightResponse> getByCourseId(Long courseId) {
        CourseWeight courseWeight = courseWeightRepository.findByCourseId(courseId)
                .orElseThrow(() -> new NotFoundException("Derse ait ağırlık bulunamadı: Ders ID " + courseId));
        CourseWeightResponse response = modelMapperService.forResponse().map(courseWeight, CourseWeightResponse.class);
        response.setCourseName(courseWeight.getCourse().getCourseName());
        return ApiResponse.success(response, "Derse ait ağırlık bulundu.");
    }
}
