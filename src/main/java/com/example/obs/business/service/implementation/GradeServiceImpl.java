package com.example.obs.business.service.implementation;

import com.example.obs.business.service.GradeService;
import com.example.obs.business.requests.GradeCreateRequest;
import com.example.obs.business.requests.GradeUpdateRequest;
import com.example.obs.business.responses.GradeResponse;
import com.example.obs.core.exceptions.BusinessException;
import com.example.obs.core.exceptions.NotFoundException;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dataAccess.AcademicRepository;
import com.example.obs.dataAccess.AcademicYearRepository;
import com.example.obs.dataAccess.CourseRepository;
import com.example.obs.dataAccess.GradeRepository;
import com.example.obs.dataAccess.StudentRepository;
import com.example.obs.model.entity.*;
import com.example.obs.business.service.CourseWeightService;    
import com.example.obs.business.responses.CourseWeightResponse;     
import com.example.obs.core.utilities.results.ApiResponse;  
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class GradeServiceImpl implements GradeService {
    private final GradeRepository gradeRepository;
    private final ModelMapperService modelMapperService;
    private final AcademicYearRepository academicYearRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final AcademicRepository academicRepository;
    private final CourseWeightService courseWeightService;

    private void calculateAndSetAverageGrade(Grade grade) {
        if (grade == null || grade.getCourse() == null || grade.getCourse().getId() == null) {
            return; 
        }

        Long courseId = grade.getCourse().getId();
        ApiResponse<CourseWeightResponse> weightResponse = courseWeightService.getByCourseId(courseId);

        if (!weightResponse.isSuccess() || weightResponse.getData() == null) {
            throw new BusinessException("Ders için ağırlık tanımları bulunamadı: ID " + courseId);
        }
        CourseWeightResponse courseWeights = weightResponse.getData();

        BigDecimal totalWeightedGrade = BigDecimal.ZERO;
        BigDecimal totalWeight = BigDecimal.ZERO;

        if (grade.getMidtermGrade() != null && courseWeights.getMidtermWeight() != null && courseWeights.getMidtermWeight().compareTo(BigDecimal.ZERO) > 0) {
            totalWeightedGrade = totalWeightedGrade.add(grade.getMidtermGrade().multiply(courseWeights.getMidtermWeight()));
            totalWeight = totalWeight.add(courseWeights.getMidtermWeight());
        }
   
        if (grade.getAssignmentGrade() != null && courseWeights.getAssignmentWeight() != null && courseWeights.getAssignmentWeight().compareTo(BigDecimal.ZERO) > 0) {
            totalWeightedGrade = totalWeightedGrade.add(grade.getAssignmentGrade().multiply(courseWeights.getAssignmentWeight()));
            totalWeight = totalWeight.add(courseWeights.getAssignmentWeight());
        }
    
        if (grade.getFinalGrade() != null && courseWeights.getFinalWeight() != null && courseWeights.getFinalWeight().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal effectiveFinalGrade = grade.getFinalGrade();
            if (grade.getMakeupGrade() != null && grade.getMakeupGrade().compareTo(effectiveFinalGrade) > 0) {
                effectiveFinalGrade = grade.getMakeupGrade();
            }
            totalWeightedGrade = totalWeightedGrade.add(effectiveFinalGrade.multiply(courseWeights.getFinalWeight()));
            totalWeight = totalWeight.add(courseWeights.getFinalWeight());
        }
        else if (grade.getFinalGrade() == null && grade.getMakeupGrade() != null && courseWeights.getFinalWeight() != null && courseWeights.getFinalWeight().compareTo(BigDecimal.ZERO) > 0) {
            totalWeightedGrade = totalWeightedGrade.add(grade.getMakeupGrade().multiply(courseWeights.getFinalWeight()));
            totalWeight = totalWeight.add(courseWeights.getFinalWeight());
        }

        if (totalWeight.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal average = totalWeightedGrade.divide(totalWeight, 2, RoundingMode.HALF_UP);
            grade.setAverage(average);
        } else {
            grade.setAverage(BigDecimal.ZERO);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<GradeResponse> getAll() {
        return gradeRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public GradeResponse getById(Long id) {
        return this.gradeRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Not bilgisi bulunamadı! ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GradeResponse> getByStudentId(Long studentId) {
        return gradeRepository.findByStudentId(studentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void add(GradeCreateRequest gradeCreateRequest) {
        Student student = studentRepository.findById(gradeCreateRequest.getStudentId())
                .orElseThrow(() -> new NotFoundException("Öğrenci bulunamadı: ID " + gradeCreateRequest.getStudentId()));
        Course course = courseRepository.findById(gradeCreateRequest.getCourseId())
                .orElseThrow(() -> new NotFoundException("Ders bulunamadı: ID " + gradeCreateRequest.getCourseId()));
        Academic academic = academicRepository.findById(gradeCreateRequest.getAcademicId())
                .orElseThrow(() -> new NotFoundException("Akademisyen bulunamadı: ID " + gradeCreateRequest.getAcademicId()));
        AcademicYear academicYear = academicYearRepository.findById(gradeCreateRequest.getAcademicYearId())
                .orElseThrow(() -> new NotFoundException("Akademik yıl bulunamadı: ID " + gradeCreateRequest.getAcademicYearId()));

        Grade grade = this.modelMapperService.forRequest().map(gradeCreateRequest, Grade.class);
        
        grade.setStudent(student);
        grade.setCourse(course);    
        grade.setAcademic(academic);
        grade.setAcademicYear(academicYear.getName());

        ApiResponse<CourseWeightResponse> weightResponse = courseWeightService.getByCourseId(course.getId());
        if (!weightResponse.isSuccess() || weightResponse.getData() == null) {
            throw new BusinessException("Ders için ağırlık tanımları bulunamadı: ID " + course.getId());
        }
        CourseWeightResponse courseWeights = weightResponse.getData();
        if (courseWeights.getMidtermWeight() == null) {
            throw new BusinessException("Ders için vize ağırlığı tanımlanmamış: ID " + course.getId());
        }
        grade.setMidtermWeight(courseWeights.getMidtermWeight());

        if (courseWeights.getAssignmentWeight() == null) {
            throw new BusinessException("Ders için ödev ağırlığı tanımlanmamış: ID " + course.getId());
        }
        grade.setAssignmentWeight(courseWeights.getAssignmentWeight());

        if (courseWeights.getFinalWeight() == null) {
            throw new BusinessException("Ders için final ağırlığı tanımlanmamış: ID " + course.getId());
        }
        grade.setFinalWeight(courseWeights.getFinalWeight());

        calculateAndSetAverageGrade(grade); 

        gradeRepository.save(grade);
    }

    @Override
    public void update(GradeUpdateRequest gradeUpdateRequest) {
        if (!gradeRepository.existsById(gradeUpdateRequest.getId())) {
            throw new NotFoundException("Güncelleme yapılamadı. ID: "
                    + gradeUpdateRequest.getId() + " ile eşleşen bir not kaydı bulunamadı.");
        }

        Grade existingGrade = gradeRepository.findById(gradeUpdateRequest.getId())
                .orElseThrow(() -> new NotFoundException("Not kaydı bulunamadı! ID: " + gradeUpdateRequest.getId()));
       
        if (gradeUpdateRequest.getMidtermGrade() != null) {
            existingGrade.setMidtermGrade(gradeUpdateRequest.getMidtermGrade());
        }
        if (gradeUpdateRequest.getAssignmentGrade() != null) {
            existingGrade.setAssignmentGrade(gradeUpdateRequest.getAssignmentGrade());
        }
        if (gradeUpdateRequest.getFinalGrade() != null) {
            existingGrade.setFinalGrade(gradeUpdateRequest.getFinalGrade());
        }
        if (gradeUpdateRequest.getMakeupGrade() != null) {
            existingGrade.setMakeupGrade(gradeUpdateRequest.getMakeupGrade());
        }
        if (gradeUpdateRequest.getLetterGrade() != null) {
            existingGrade.setLetterGrade(gradeUpdateRequest.getLetterGrade());
        }
        if (gradeUpdateRequest.getIsPassed() != null) {
            existingGrade.setIsPassed(gradeUpdateRequest.getIsPassed());
        }

        Course courseForUpdate = courseRepository.findById(existingGrade.getCourse().getId())
                .orElseThrow(() -> new NotFoundException("Ders bulunamadı: ID " + existingGrade.getCourse().getId()));
        existingGrade.setCourse(courseForUpdate);   

        calculateAndSetAverageGrade(existingGrade); 

        gradeRepository.save(existingGrade);
    }

    @Override
    public void delete(Long id) {
        if (!gradeRepository.existsById(id)) {
            throw new NotFoundException("Silme işlemi başarısız. ID: " + id + " ile eşleşen bir not kaydı bulunamadı.");
        }
        this.gradeRepository.deleteById(id);
    }

    @Override
    public void updateByStudentNumberAndCourseCode(String studentNumber, String courseCode, GradeUpdateRequest gradeUpdateRequest) {
        List<Grade> grades = gradeRepository.findByStudent_StudentNumberAndCourse_CourseCode(studentNumber, courseCode);

        if (grades.isEmpty()) {
            throw new NotFoundException("Öğrenciye (" + studentNumber + ") ait bu ders (" + courseCode + ") için not bulunamadı.");
        }
        
        if (grades.size() > 1) {
            grades.sort((g1, g2) -> g2.getId().compareTo(g1.getId())); 
        }

        Grade gradeToUpdate = grades.get(0); 
        gradeUpdateRequest.setId(gradeToUpdate.getId()); 
        
        if (gradeUpdateRequest.getMidtermGrade() != null) {
            gradeToUpdate.setMidtermGrade(gradeUpdateRequest.getMidtermGrade());
        }
        if (gradeUpdateRequest.getAssignmentGrade() != null) {
            gradeToUpdate.setAssignmentGrade(gradeUpdateRequest.getAssignmentGrade());
        }
        if (gradeUpdateRequest.getFinalGrade() != null) {
            gradeToUpdate.setFinalGrade(gradeUpdateRequest.getFinalGrade());
        }
        if (gradeUpdateRequest.getMakeupGrade() != null) {
            gradeToUpdate.setMakeupGrade(gradeUpdateRequest.getMakeupGrade());
        }
        if (gradeUpdateRequest.getLetterGrade() != null) {
            gradeToUpdate.setLetterGrade(gradeUpdateRequest.getLetterGrade());
        }
        if (gradeUpdateRequest.getIsPassed() != null) {
            gradeToUpdate.setIsPassed(gradeUpdateRequest.getIsPassed());
        }

        Course courseForCalc = courseRepository.findById(gradeToUpdate.getCourse().getId())
             .orElseThrow(() -> new NotFoundException("Ders bulunamadı: ID " + gradeToUpdate.getCourse().getId()));
        gradeToUpdate.setCourse(courseForCalc); 

        calculateAndSetAverageGrade(gradeToUpdate);
        

        gradeRepository.save(gradeToUpdate);
    }
    
    private GradeResponse mapToResponse(Grade grade) {
        return modelMapperService.forResponse().map(grade, GradeResponse.class);
    }
}
