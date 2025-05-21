package com.example.obs.business.service.implementation;

import com.example.obs.business.service.StudentCourseService;
import com.example.obs.business.requests.StudentCourseCreateRequest;
import com.example.obs.business.requests.StudentCourseUpdateRequest;
import com.example.obs.business.responses.StudentCourseResponse;
import com.example.obs.core.exceptions.BusinessException;
import com.example.obs.core.exceptions.NotFoundException;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dataAccess.StudentCourseRepository;
import com.example.obs.model.enums.Semester;
import com.example.obs.model.entity.StudentCourse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class StudentCourseServiceImpl implements StudentCourseService {
    private final StudentCourseRepository studentCourseRepository;
    private final ModelMapperService modelMapperService;

    @Override
    @Transactional(readOnly = true)
    public List<StudentCourseResponse> getAll() {
        return studentCourseRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StudentCourseResponse getById(Long id) {
        return this.studentCourseRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Öğrenci ders kaydı bulunamadı! ID: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<StudentCourseResponse> getByStudentId(Long studentId) {
        return studentCourseRepository.findByStudentId(studentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<StudentCourseResponse> getByCourseId(Long courseId) {
        return studentCourseRepository.findByCourseId(courseId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<StudentCourseResponse> getActiveByStudentId(Long studentId) {
        return studentCourseRepository.findByStudentIdAndIsActiveTrue(studentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<StudentCourseResponse> getActiveByCourseId(Long courseId) {
        return studentCourseRepository.findByCourseIdAndIsActiveTrue(courseId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<StudentCourseResponse> getByStudentIdAndSemester(Long studentId, Semester semester) {
        return studentCourseRepository.findByStudentIdAndSemester(studentId, semester).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<StudentCourseResponse> getByCourseIdAndSemester(Long courseId, Semester semester) {
        return studentCourseRepository.findByCourseIdAndSemester(courseId, semester).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public StudentCourseResponse getByStudentIdAndCourseIdAndSemester(Long studentId, Long courseId, Semester semester) {
        return this.studentCourseRepository.findByStudentIdAndCourseIdAndSemester(studentId, courseId, semester)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Belirtilen öğrenci, ders ve dönem için kayıt bulunamadı!"));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<StudentCourseResponse> getAllCompleted() {
        return studentCourseRepository.findByIsCompletedTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<StudentCourseResponse> getCompletedByStudentId(Long studentId) {
        return studentCourseRepository.findByStudentIdAndIsCompletedTrue(studentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<StudentCourseResponse> getByStudentIdAndIsPassed(Long studentId, Boolean isPassed) {
        return studentCourseRepository.findByStudentIdAndIsPassed(studentId, isPassed).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void add(StudentCourseCreateRequest studentCourseCreateRequest) {
        StudentCourse studentCourse = this.modelMapperService.forRequest()
                .map(studentCourseCreateRequest, StudentCourse.class);
        
        studentCourse.setIsActive(true);
        studentCourse.setIsCompleted(false);

        studentCourseRepository.save(studentCourse);
    }

    @Override
    public void update(StudentCourseUpdateRequest studentCourseUpdateRequest) {
        if (!studentCourseRepository.existsById(studentCourseUpdateRequest.getId())) {
            throw new NotFoundException("Güncelleme yapılamadı. ID: "
                    + studentCourseUpdateRequest.getId() + " ile eşleşen bir öğrenci ders kaydı bulunamadı.");
        }
        
        StudentCourse existingStudentCourse = studentCourseRepository.findById(studentCourseUpdateRequest.getId())
                .orElseThrow(() -> new NotFoundException("Öğrenci ders kaydı bulunamadı! ID: " + studentCourseUpdateRequest.getId()));

        StudentCourse studentCourse = this.modelMapperService.forRequest()
                .map(studentCourseUpdateRequest, StudentCourse.class);
                
        studentCourse.setStudent(existingStudentCourse.getStudent());
        studentCourse.setCourse(existingStudentCourse.getCourse());
        
        studentCourseRepository.save(studentCourse);
    }

    @Override
    public void delete(Long id) {
        if (!studentCourseRepository.existsById(id)) {
            throw new NotFoundException("Silme işlemi başarısız. ID: " + id + " ile eşleşen bir öğrenci ders kaydı bulunamadı.");
        }
        this.studentCourseRepository.deleteById(id);
    }
    
    private StudentCourseResponse mapToResponse(StudentCourse studentCourse) {
        return modelMapperService.forResponse().map(studentCourse, StudentCourseResponse.class);
    }
}
