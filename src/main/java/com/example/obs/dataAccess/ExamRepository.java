package com.example.obs.dataAccess;
import com.example.obs.model.entity.*;
import com.example.obs.model.enums.ExamType;
import com.example.obs.model.enums.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByCourse(Course course);
    List<Exam> findByCourseAndExamType(Course course, ExamType examType);
    List<Exam> findByExamDateBetween(LocalDateTime start, LocalDateTime end);
    List<Exam> findByCourse_Department(Department department);
    List<Exam> findByAcademicYearAndSemester(AcademicYear academicYear, Semester examSemesterEnum);
    @Query("SELECT e FROM Exam e JOIN e.course c JOIN StudentCourse sc ON sc.course = c WHERE sc.student.id = :studentId")
    List<Exam> findByStudentId(@Param("studentId") Long studentId);
    List<Exam> findByCourseAndSemesterAndAcademicYear(Course course, com.example.obs.model.enums.Semester semester, AcademicYear academicYear);
    List<Exam> findBySemesterAndAcademicYear(com.example.obs.model.enums.Semester semester, AcademicYear academicYear);

}
