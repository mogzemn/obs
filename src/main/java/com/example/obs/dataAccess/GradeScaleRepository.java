package com.example.obs.dataAccess;

import com.example.obs.model.entity.GradeScale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeScaleRepository extends JpaRepository<GradeScale, Long> {
    List<GradeScale> findByIsActiveTrue();
    List<GradeScale> findByIsDefaultTrue();
    List<GradeScale> findByAcademicDepartmentId(Long departmentId);
    List<GradeScale> findByCourseId(Long courseId);
    List<GradeScale> findByAcademicId(Long academicId);
    Optional<GradeScale> findByAcademicDepartmentIdAndIsDefaultTrue(Long departmentId);
    Optional<GradeScale> findByCourseIdAndIsDefaultTrue(Long courseId);
}
