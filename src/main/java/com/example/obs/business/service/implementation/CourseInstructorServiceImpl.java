package com.example.obs.business.service.implementation;

import com.example.obs.business.responses.AcademicResponse;
import com.example.obs.business.service.CourseInstructorService;
import com.example.obs.business.requests.CourseInstructorCreateRequest;
import com.example.obs.business.requests.CourseInstructorUpdateRequest;
import com.example.obs.business.responses.CourseInstructorResponse;
import com.example.obs.core.exceptions.BusinessException;
import com.example.obs.core.exceptions.NotFoundException;
import com.example.obs.core.utilities.academicyear.AcademicYearUtil;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dataAccess.AcademicYearRepository;
import com.example.obs.dataAccess.CourseInstructorRepository;
import com.example.obs.model.entity.AcademicYear;
import com.example.obs.model.entity.CourseInstructor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class CourseInstructorServiceImpl implements CourseInstructorService {
    private final CourseInstructorRepository courseInstructorRepository;
    private final ModelMapperService modelMapperService;
    private final AcademicYearUtil academicYearUtil;
    private final AcademicYearRepository academicYearRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CourseInstructorResponse> getAll() {
        List<CourseInstructor> instructors = courseInstructorRepository.findAll();
        return instructors.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CourseInstructorResponse getById(Long id) {
        CourseInstructor instructor = this.courseInstructorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Eğitmen bilgisi bulunamadı! ID: " + id));
        return mapToResponse(instructor);
    }

    @Override
    public void add(CourseInstructorCreateRequest courseInstructorCreateRequest) {
        try {
            AcademicYear academicYear = academicYearRepository.findById(courseInstructorCreateRequest.getAcademicYearId())
                    .orElseThrow(() -> new NotFoundException("Akademik yıl bulunamadı: ID " + 
                            courseInstructorCreateRequest.getAcademicYearId()));

            CourseInstructor courseInstructor = this.modelMapperService.forRequest()
                    .map(courseInstructorCreateRequest, CourseInstructor.class);
            
            if (courseInstructorCreateRequest.getAcademicId() == null) {
                throw new BusinessException("Akademisyen ID'si boş olamaz. Lütfen bir akademisyen seçin.");
            }
            
            courseInstructor.setAcademicYear(academicYear.getName());

            courseInstructorRepository.save(courseInstructor);
        } catch (Exception e) {
            throw new BusinessException("Ders eğitmeni eklenirken bir hata oluştu: " + e.getMessage());
        }
    }

    @Override
    public void update(CourseInstructorUpdateRequest courseInstructorUpdateRequest) {
        if (!courseInstructorRepository.existsById(courseInstructorUpdateRequest.getId())) {
            throw new NotFoundException("Güncelleme yapılamadı. ID: "
                    + courseInstructorUpdateRequest.getId() + " ile eşleşen bir eğitmen bulunamadı.");
        }

        CourseInstructor existingCourseInstructor = courseInstructorRepository.findById(courseInstructorUpdateRequest.getId())
                .orElseThrow(() -> new NotFoundException("Eğitmen bilgisi bulunamadı! ID: " + courseInstructorUpdateRequest.getId()));

        CourseInstructor courseInstructor = this.modelMapperService.forRequest()
                .map(courseInstructorUpdateRequest, CourseInstructor.class);

        courseInstructor.setAcademic(existingCourseInstructor.getAcademic());
        courseInstructor.setCourse(existingCourseInstructor.getCourse());
        
        courseInstructor.setAcademicYear(existingCourseInstructor.getAcademicYear());
        courseInstructor.setSemester(existingCourseInstructor.getSemester());

        courseInstructorRepository.save(courseInstructor);
    }

    @Override
    public void delete(Long id) {
        if (!courseInstructorRepository.existsById(id)) {
            throw new NotFoundException("Silme işlemi başarısız. ID: " + id + " ile eşleşen bir eğitmen bulunamadı.");
        }
        this.courseInstructorRepository.deleteById(id);
    }
    
    private CourseInstructorResponse mapToResponse(CourseInstructor courseInstructor) {
        CourseInstructorResponse response = modelMapperService.forResponse().map(courseInstructor, CourseInstructorResponse.class);
        
        if (courseInstructor.getAcademic() != null) {
            response.setAcademic(modelMapperService.forResponse().map(courseInstructor.getAcademic(), AcademicResponse.class));
        }
        
        return response;
    }
}
