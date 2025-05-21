package com.example.obs.dataAccess;

import com.example.obs.model.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    @Query("SELECT f FROM Faculty f ORDER BY f.facultyCode DESC")
    Optional<Faculty> findTopByOrderByCodeDesc();
}
