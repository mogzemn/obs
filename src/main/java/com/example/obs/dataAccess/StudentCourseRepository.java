package com.example.obs.dataAccess;

import com.example.obs.model.entity.StudentCourse;
import com.example.obs.model.enums.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
    
    List<StudentCourse> findByStudentId(Long studentId);
    List<StudentCourse> findByCourseId(Long courseId);
    List<StudentCourse> findByStudentIdAndIsActiveTrue(Long studentId);
    List<StudentCourse> findByCourseIdAndIsActiveTrue(Long courseId);
    List<StudentCourse> findByStudentIdAndSemester(Long studentId, Semester semester);
    List<StudentCourse> findByCourseIdAndSemester(Long courseId, Semester semester);
    Optional<StudentCourse> findByStudentIdAndCourseIdAndSemester(Long studentId, Long courseId, Semester semester);
    List<StudentCourse> findByIsCompletedTrue();
    List<StudentCourse> findByStudentIdAndIsCompletedTrue(Long studentId);
    List<StudentCourse> findByStudentIdAndIsPassed(Long studentId, Boolean isPassed);
    Optional<StudentCourse> findByStudentIdAndCourseId(Long studentId, Long courseId);
}
