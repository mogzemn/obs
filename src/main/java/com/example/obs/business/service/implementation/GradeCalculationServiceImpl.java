package com.example.obs.business.service.implementation;

import com.example.obs.business.service.GradeCalculationService;
import com.example.obs.dateAccess.GradeRepository;
import com.example.obs.dateAccess.StudentRepository;
import com.example.obs.dateAccess.TranscriptRepository;
import com.example.obs.model.entity.Grade;
import com.example.obs.model.entity.Student;
import com.example.obs.model.entity.Transcript;
import com.example.obs.model.entity.TranscriptItem;
import com.example.obs.model.enums.LetterGrade;
import com.example.obs.model.enums.Semester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class GradeCalculationServiceImpl implements GradeCalculationService {

    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;
    private final TranscriptRepository transcriptRepository;
    
    @Autowired
    public GradeCalculationServiceImpl(
            StudentRepository studentRepository,
            GradeRepository gradeRepository,
            TranscriptRepository transcriptRepository) {
        this.studentRepository = studentRepository;
        this.gradeRepository = gradeRepository;
        this.transcriptRepository = transcriptRepository;
    }

    @Override
    @Transactional
    public Transcript calculateAndUpdateStudentGPA(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Öğrenci bulunamadı: " + studentId));
        
        Transcript transcript = student.getTranscript();
        if (transcript == null) {
            transcript = new Transcript();
            transcript.setStudent(student);
            transcript.setGpa(0.0);
            transcript.setCgpa(0.0);
            transcript.setTotalCredits(0);
            transcript.setTotalEcts(0);
            transcript.setCompletedCredits(0);
            transcript.setCompletedEcts(0);
            transcript.setGeneratedAt(LocalDateTime.now());
            transcript.setOfficial(false);
            student.setTranscript(transcript);
        }
        

        List<Grade> grades = gradeRepository.findByStudent(student);
        

        updateTranscriptItems(transcript, grades);
        

        transcript.calculateGPA();
        transcript.calculateCGPA();
        transcript.setGeneratedAt(LocalDateTime.now());
        
        return transcriptRepository.save(transcript);
    }

    private void updateTranscriptItems(Transcript transcript, List<Grade> grades) {

        if (transcript.getItems() == null) {
            transcript.setItems(new ArrayList<TranscriptItem>());
        } else {

            transcript.getItems().clear();
        }
        

        List<TranscriptItem> items = grades.stream()
                .map(grade -> {
                    TranscriptItem item = new TranscriptItem();
                    item.setTranscript(transcript);
                    item.setCourse(grade.getCourse());
                    item.setGrade(grade);
                    item.setAcademicTerm(grade.getAcademicYear() + " " + grade.getSemester().toString());
                    item.setYear(Integer.parseInt(grade.getAcademicYear().split("-")[0]));
                    return item;
                })
                .collect(Collectors.toList());
        
        transcript.getItems().addAll(items);
    }

    @Override
    public LetterGrade calculateLetterGrade(double numericGrade) {
        return LetterGrade.fromNumericGrade(numericGrade);
    }

    @Override
    public BigDecimal calculateWeightedGrade(BigDecimal midtermGrade, BigDecimal finalGrade, 
                                        BigDecimal assignmentGrade, BigDecimal makeupGrade,
                                        BigDecimal midtermWeight, BigDecimal finalWeight, 
                                        BigDecimal assignmentWeight) {
        
        if (midtermGrade == null || finalGrade == null) {
            return null;
        }
        

        BigDecimal effectiveFinalGrade = (makeupGrade != null) ? makeupGrade : finalGrade;
        

        BigDecimal weightedMidterm = midtermGrade.multiply(midtermWeight);
        BigDecimal weightedAssignment = (assignmentGrade != null) ? 
                assignmentGrade.multiply(assignmentWeight) : BigDecimal.ZERO;
        BigDecimal weightedFinal = effectiveFinalGrade.multiply(finalWeight);
        
        BigDecimal weightSum = midtermWeight.add(finalWeight);
        if (assignmentGrade != null) {
            weightSum = weightSum.add(assignmentWeight);
        }
        
        return weightedMidterm.add(weightedAssignment).add(weightedFinal)
                .divide(weightSum, 2, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional
    public Transcript calculateSemesterGPA(Long studentId, String academicYear, String semester) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Öğrenci bulunamadı: " + studentId));
        
        Semester semesterEnum = Semester.valueOf(semester);
        

        List<Grade> semesterGrades = gradeRepository.findByStudentAndAcademicYearAndSemester(
                student, academicYear, semesterEnum);
        

        Transcript semesterTranscript = new Transcript();
        semesterTranscript.setStudent(student);
        semesterTranscript.setGpa(0.0);
        semesterTranscript.setCgpa(0.0);
        semesterTranscript.setTotalCredits(0);
        semesterTranscript.setTotalEcts(0);
        semesterTranscript.setCompletedCredits(0);
        semesterTranscript.setCompletedEcts(0);
        semesterTranscript.setGeneratedAt(LocalDateTime.now());
        semesterTranscript.setOfficial(false);
        semesterTranscript.setItems(new ArrayList<TranscriptItem>());
        

        updateTranscriptItems(semesterTranscript, semesterGrades);
        

        semesterTranscript.calculateGPA();
        semesterTranscript.calculateCGPA();
        
        return semesterTranscript;
    }
} 