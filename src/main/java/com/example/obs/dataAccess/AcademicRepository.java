package com.example.obs.dataAccess;

import com.example.obs.model.entity.Academic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcademicRepository extends JpaRepository<Academic, Long> {
    Optional<Academic> findByAcademicNumber(String academicNumber);
    boolean existsByAcademicNumber(String academicNumber);
    Optional<Academic> findByUser_Id(Long userId);
    Optional<Academic> findByUser_Email(String email);
}
