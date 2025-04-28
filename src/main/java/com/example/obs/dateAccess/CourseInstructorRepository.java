package com.example.obs.dateAccess;

import com.example.obs.model.entity.CourseInstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseInstructorRepository extends JpaRepository<CourseInstructor, Long> {
}
