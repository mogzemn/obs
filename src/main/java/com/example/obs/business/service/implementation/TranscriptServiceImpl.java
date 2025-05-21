package com.example.obs.business.service.implementation;

import com.example.obs.business.requests.TranscriptCreateRequest;
import com.example.obs.business.requests.TranscriptUpdateRequest;
import com.example.obs.business.responses.TranscriptResponse;
import com.example.obs.business.service.TranscriptService;
import com.example.obs.core.utilities.calculations.GpaCalculator;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dataAccess.GradeRepository;
import com.example.obs.dataAccess.StudentRepository;
import com.example.obs.dataAccess.TranscriptRepository;
import com.example.obs.model.entity.Grade;
import com.example.obs.model.entity.Student;
import com.example.obs.model.entity.Transcript;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;     
import java.time.LocalDateTime; 
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class TranscriptServiceImpl implements TranscriptService {

    private final TranscriptRepository transcriptRepository;
    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;
    private final ModelMapperService modelMapperService;

    @Override
    public TranscriptResponse add(TranscriptCreateRequest createRequest) {
        Student student = studentRepository.findById(createRequest.getStudentId())
                .orElseThrow(() -> new RuntimeException("Öğrenci bulunamadı, ID: " + createRequest.getStudentId()));

        Double gpa = calculateGpaForStudentUntilDate(student.getId(), createRequest.getTranscriptDate().atStartOfDay());

        Transcript transcript = Transcript.builder()
                .student(student)
                .gpa(gpa)
                .transcriptDate(createRequest.getTranscriptDate())
                .build();

        transcriptRepository.save(transcript);
        return mapToResponse(transcript);
    }

    @Override
    public TranscriptResponse update(TranscriptUpdateRequest updateRequest) {
        Transcript transcript = transcriptRepository.findById(updateRequest.getId())
                .orElseThrow(() -> new RuntimeException("Transkript bulunamadı, ID: " + updateRequest.getId()));

        
        if (updateRequest.getGpa() != null) {
            transcript.setGpa(updateRequest.getGpa());
        } else {
                
            Double calculatedGpa = calculateGpaForStudentUntilDate(transcript.getStudent().getId(), transcript.getTranscriptDate().atStartOfDay());
            transcript.setGpa(calculatedGpa);
        }

        transcriptRepository.save(transcript);
        return mapToResponse(transcript);
    }

    @Override
    public void delete(Long id) {
        Transcript transcript = transcriptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transkript bulunamadı, ID: " + id));
        transcriptRepository.delete(transcript);
    }

    @Override
    @Transactional(readOnly = true)
    public TranscriptResponse getById(Long id) {
        Transcript transcript = transcriptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transkript bulunamadı, ID: " + id));
        return mapToResponse(transcript);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TranscriptResponse> getAll() {
        return transcriptRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TranscriptResponse> getByStudentId(Long studentId) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Öğrenci bulunamadı, ID: " + studentId));
        return transcriptRepository.findAllByStudentIdOrderByTranscriptDateDesc(studentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public TranscriptResponse getLatestByStudentId(Long studentId) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Öğrenci bulunamadı, ID: " + studentId));
        
        return transcriptRepository.findAllByStudentIdOrderByTranscriptDateDesc(studentId).stream()
                .findFirst()
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Öğrenci için transkript bulunamadı, ID: " + studentId));
    }

    @Override
    @Transactional(readOnly = true)
    public TranscriptResponse getByStudentIdAndDate(Long studentId, LocalDate date) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Öğrenci bulunamadı, ID: " + studentId));
        Transcript transcript = transcriptRepository.findByStudentIdAndTranscriptDate(studentId, date)
                .orElseThrow(() -> new RuntimeException("Öğrenci " + studentId + " için " + date + " tarihinde transkript bulunamadı."));
        return mapToResponse(transcript);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double calculateGpaForStudent(Long studentId) {
            
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Öğrenci bulunamadı, ID: " + studentId));
        
        
        List<Grade> passedGrades = gradeRepository.findByStudentIdAndIsPassed(studentId, true);
        
        return GpaCalculator.calculateGpa(passedGrades);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double calculateGpaForStudentUntilDate(Long studentId, LocalDateTime date) { 
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Öğrenci bulunamadı, ID: " + studentId));
        
        List<Grade> passedGradesUntilDate = gradeRepository.findByStudentIdAndIsPassedAndCreatedAtBefore(
                studentId, true, date);
        
        return GpaCalculator.calculateGpa(passedGradesUntilDate);
    }
    
    @Override
    public TranscriptResponse calculateAndCreateTranscript(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Öğrenci bulunamadı, ID: " + studentId));
        
        LocalDate today = LocalDate.now();
        Double gpa = calculateGpaForStudent(studentId);
        
        Transcript transcript = Transcript.builder()
                .student(student)
                .gpa(gpa)
                .transcriptDate(today)
                .build();
        
        transcriptRepository.save(transcript);
        return mapToResponse(transcript);
    }
    
    @Override
    public void calculateAndCreateTranscriptsForAllStudents() {
        List<Student> allStudents = studentRepository.findAll();
        
        for (Student student : allStudents) {
            try {
                calculateAndCreateTranscript(student.getId());
            } catch (Exception e) {
                System.err.println("Error calculating transcript for student ID " + student.getId() + ": " + e.getMessage());
            }
        }
    }

    private TranscriptResponse mapToResponse(Transcript transcript) {
        return modelMapperService.forResponse().map(transcript, TranscriptResponse.class);
    }
}
