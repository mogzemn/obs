package com.example.obs.dataAccess;

import com.example.obs.model.entity.CourseWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseWeightRepository extends JpaRepository<CourseWeight, Long> {
    Optional<CourseWeight> findByCourseId(Long courseId);
}
