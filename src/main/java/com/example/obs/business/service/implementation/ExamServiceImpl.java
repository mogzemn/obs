package com.example.obs.business.service.implementation;

import com.example.obs.business.service.ExamService;
import com.example.obs.business.requests.ExamCreateRequest;
import com.example.obs.business.requests.ExamUpdateRequest;
import com.example.obs.business.responses.ExamResponse;
import com.example.obs.core.exceptions.BusinessException;
import com.example.obs.core.exceptions.NotFoundException;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dataAccess.*;
import com.example.obs.model.entity.*;
import com.example.obs.model.entity.Exam;
import com.example.obs.model.entity.User;
import com.example.obs.model.enums.ExamType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final StudentRepository studentRepository;
    private final AcademicYearRepository academicYearRepository;
    private final ModelMapperService modelMapperService;

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponse> getAll() {
        return examRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ExamResponse getById(Long id) {
        return this.examRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Sınav bilgisi bulunamadı! ID: " + id));
    }

    @Override
    public void add(ExamCreateRequest examCreateRequest) {
        Course course = courseRepository.findById(examCreateRequest.getCourseId())
                .orElseThrow(() -> new NotFoundException("Ders bulunamadı! ID: " + examCreateRequest.getCourseId()));
        
        Exam exam = this.modelMapperService.forRequest()
                .map(examCreateRequest, Exam.class);
        
        AcademicYear academicYear = academicYearRepository.findById(examCreateRequest.getAcademicYearId())
                .orElseThrow(() -> new NotFoundException("Akademik Yıl bulunamadı! ID: " + examCreateRequest.getAcademicYearId()));
        exam.setAcademicYear(academicYear);
        exam.setCourse(course);
        
        if(examCreateRequest.getCreatedById() != null) {
            User createdBy = userRepository.findById(examCreateRequest.getCreatedById())
                    .orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı! ID: " + examCreateRequest.getCreatedById()));
            exam.setCreatedBy(createdBy);
        }
        
        examRepository.save(exam);
    }

    @Override
    public void update(ExamUpdateRequest examUpdateRequest) {
        if (!examRepository.existsById(examUpdateRequest.getId())) {
            throw new NotFoundException("Güncelleme yapılamadı. ID: "
                    + examUpdateRequest.getId() + " ile eşleşen bir sınav bulunamadı.");
        }

        Exam existingExam = examRepository.findById(examUpdateRequest.getId())
                .orElseThrow(() -> new NotFoundException("Sınav bulunamadı! ID: " + examUpdateRequest.getId()));

        Course course = courseRepository.findById(examUpdateRequest.getCourseId())
                .orElseThrow(() -> new NotFoundException("Ders bulunamadı! ID: " + examUpdateRequest.getCourseId()));
        
        Exam exam = this.modelMapperService.forRequest()
                .map(examUpdateRequest, Exam.class);

        AcademicYear academicYear = academicYearRepository.findById(examUpdateRequest.getAcademicYearId())
                .orElseThrow(() -> new NotFoundException("Akademik Yıl bulunamadı! ID: " + examUpdateRequest.getAcademicYearId()));
        exam.setAcademicYear(academicYear);
        exam.setCourse(course);
        
        exam.setCreatedBy(existingExam.getCreatedBy());
        exam.setCreatedAt(existingExam.getCreatedAt());
        
        if(examUpdateRequest.getUpdatedById() != null) {
            User updatedBy = userRepository.findById(examUpdateRequest.getUpdatedById())
                    .orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı! ID: " + examUpdateRequest.getUpdatedById()));
            exam.setUpdatedBy(updatedBy);
        }
        
        examRepository.save(exam);
    }

    @Override
    public void delete(Long id) {
        if (!examRepository.existsById(id)) {
            throw new NotFoundException("Silme işlemi başarısız. ID: " + id + " ile eşleşen bir sınav bulunamadı.");
        }
        this.examRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponse> getByCourseId(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Ders bulunamadı! ID: " + courseId));
        
        return examRepository.findByCourse(course).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponse> getByCourseAndSemesterAndAcademicYear(Long courseId, com.example.obs.model.enums.Semester semesterEnum, Long academicYearId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Ders bulunamadı! ID: " + courseId));
        AcademicYear academicYear = academicYearRepository.findById(academicYearId)
                .orElseThrow(() -> new NotFoundException("Akademik Yıl bulunamadı! ID: " + academicYearId));
        
        return examRepository.findByCourseAndSemesterAndAcademicYear(course, semesterEnum, academicYear).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponse> getByCourseAndExamType(Long courseId, ExamType examType) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Ders bulunamadı! ID: " + courseId));
        
        return examRepository.findByCourseAndExamType(course, examType).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponse> getByDateRange(LocalDateTime start, LocalDateTime end) {
        return examRepository.findByExamDateBetween(start, end).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponse> getBySemesterAndAcademicYear(com.example.obs.model.enums.Semester semesterEnum, Long academicYearId) {
        AcademicYear academicYear = academicYearRepository.findById(academicYearId)
                .orElseThrow(() -> new NotFoundException("Akademik Yıl bulunamadı! ID: " + academicYearId));

        return examRepository.findBySemesterAndAcademicYear(semesterEnum, academicYear).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponse> getByDepartmentId(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new NotFoundException("Departman bulunamadı! ID: " + departmentId));
               
        return examRepository.findByCourse_Department(department).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponse> getByStudentId(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Öğrenci bulunamadı! ID: " + studentId));
                   
        return examRepository.findByStudentId(studentId).stream() 
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponse> getByAcademicYearIdAndSemesterEnum(Long academicYearId, com.example.obs.model.enums.Semester semesterEnum) {
        AcademicYear academicYear = academicYearRepository.findById(academicYearId)
                .orElseThrow(() -> new NotFoundException("Akademik Yıl bulunamadı! ID: " + academicYearId));
            
        return examRepository.findByAcademicYearAndSemester(academicYear, semesterEnum).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponse> getAllForAllDepartments() {
        return examRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    private ExamResponse mapToResponse(Exam exam) {
        ExamResponse response = modelMapperService.forResponse().map(exam, ExamResponse.class);
        if (exam.getAcademicYear() != null) {
            response.setAcademicYearName(exam.getAcademicYear().getName());
        }
        return response;
    }
}
