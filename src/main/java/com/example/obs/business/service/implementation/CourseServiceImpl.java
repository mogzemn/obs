package com.example.obs.business.service.implementation;

import com.example.obs.business.service.CourseService;
import com.example.obs.business.requests.CourseCreateRequest;
import com.example.obs.business.requests.CourseUpdateRequest;
import com.example.obs.business.responses.CourseResponse;
import com.example.obs.core.exceptions.BusinessException;
import com.example.obs.core.exceptions.NotFoundException;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.business.requests.CourseWeightCreateRequest;
import com.example.obs.business.service.CourseWeightService;
import com.example.obs.core.utilities.results.ApiResponse;
import com.example.obs.dataAccess.CourseRepository;
import com.example.obs.dataAccess.DepartmentRepository;
import com.example.obs.model.entity.Course;
import com.example.obs.model.entity.Department;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapperService modelMapperService;
    private final CourseWeightService courseWeightService;  

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getAll() {
        return courseRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getById(Long id) {
        return this.courseRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Ders bilgisi bulunamadı! ID: " + id));
    }

    @Override
    @Transactional
    public void add(CourseCreateRequest courseCreateRequest) {
        String courseCode = courseCreateRequest.getCourseCode();
        
        Optional<Course> existingCourse = courseRepository.findByCourseCode(courseCode);
        if (existingCourse.isPresent()) {
            throw new BusinessException("Bu ders kodu zaten kullanılıyor: " + courseCode);
        }
        
        try {
            Course course = modelMapperService.forRequest().map(courseCreateRequest, Course.class);
            course.setId(null);     
            
            if (courseCreateRequest.getDepartmentId() != null) {
                Department department = departmentRepository.findById(courseCreateRequest.getDepartmentId())
                        .orElseThrow(() -> new NotFoundException("Belirtilen departman bulunamadı! ID: " 
                                + courseCreateRequest.getDepartmentId()));
                course.setDepartment(department);
            }
            
            Course savedCourse = courseRepository.saveAndFlush(course);
            log.info("Ders başarıyla eklendi: {}", savedCourse.getCourseCode());

            CourseWeightCreateRequest weightRequest = new CourseWeightCreateRequest();
            weightRequest.setCourseId(savedCourse.getId());
            weightRequest.setMidtermWeight(courseCreateRequest.getMidtermWeight());
            weightRequest.setAssignmentWeight(courseCreateRequest.getAssignmentWeight());
            weightRequest.setFinalWeight(courseCreateRequest.getFinalWeight());

            ApiResponse<com.example.obs.business.responses.CourseWeightResponse> weightResponse = courseWeightService.add(weightRequest);
            if (!weightResponse.isSuccess()) {
                log.error("Ders eklendi ancak ağırlıklar eklenemedi. Ders ID: {}, Hata: {}", savedCourse.getId(), weightResponse.getMessage());
                throw new BusinessException("Ders eklendi ancak ağırlıklar eklenemedi: " + weightResponse.getMessage());
            }
            log.info("Ders ağırlıkları başarıyla eklendi. Ders ID: {}", savedCourse.getId());

        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("Bu ders kodu zaten kullanılıyor: " + courseCode);
        } catch (Exception e) {
            log.error("Ders eklenirken beklenmeyen hata: {}", e.getMessage(), e);
            throw new BusinessException("Ders eklenirken hata oluştu: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void update(CourseUpdateRequest courseUpdateRequest) {
        try {
            Course existingCourse = courseRepository.findById(courseUpdateRequest.getId())
                    .orElseThrow(() -> new NotFoundException("Güncelleme yapılamadı. ID: "
                            + courseUpdateRequest.getId() + " ile eşleşen bir ders bulunamadı."));
            
            modelMapperService.forRequest().map(courseUpdateRequest, existingCourse);
          
            existingCourse.setId(courseUpdateRequest.getId());

            if (courseUpdateRequest.getDepartmentId() != null) {
                Department department = departmentRepository.findById(courseUpdateRequest.getDepartmentId())
                        .orElseThrow(() -> new NotFoundException("Belirtilen departman bulunamadı! ID: " 
                                + courseUpdateRequest.getDepartmentId()));
                existingCourse.setDepartment(department);
            }
          
            courseRepository.save(existingCourse); 
            log.info("Ders başarıyla güncellendi: {}", existingCourse.getCourseCode());
            
        } catch (DataIntegrityViolationException e) {
            String codeInError = courseUpdateRequest.getCourseCode() != null ? courseUpdateRequest.getCourseCode() : "ID: " + courseUpdateRequest.getId();
            throw new BusinessException("Bu ders kodu zaten kullanılıyor: " + codeInError);
        } catch (Exception e) {
            log.error("Ders güncellenirken beklenmeyen hata: {}", e.getMessage(), e);
            throw new BusinessException("Ders güncellenirken hata oluştu: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            if (!courseRepository.existsById(id)) {
                throw new NotFoundException("Silme işlemi başarısız. ID: " + id + " ile eşleşen bir ders bulunamadı.");
            }
            
            Course course = courseRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Silme işlemi başarısız. ID: " 
                            + id + " ile eşleşen bir ders bulunamadı."));
            
            this.courseRepository.delete(course);
            log.info("Ders başarıyla silindi: ID={}", id);
            
        } catch (Exception e) {
            if (e instanceof NotFoundException) {
                throw e;    
            }
            log.error("Ders silinirken beklenmeyen hata: {}", e.getMessage(), e);
            throw new BusinessException("Ders silinirken hata oluştu: " + e.getMessage());
        }
    }
    
    private CourseResponse mapToResponse(Course course) {
        return modelMapperService.forResponse().map(course, CourseResponse.class);
    }
}
