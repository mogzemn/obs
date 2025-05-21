package com.example.obs.dataAccess;

import com.example.obs.model.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime; 
import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentId(Long studentId);

    List<Grade> findByStudent_StudentNumberAndCourse_CourseCode(String studentNumber, String courseCode);
    
    List<Grade> findByStudentIdAndIsPassed(Long studentId, Boolean isPassed);
    
    List<Grade> findByStudentIdAndIsPassedAndCreatedAtBefore(Long studentId, Boolean isPassed, LocalDateTime date); 
}
