package com.example.obs.dateAccess;

import com.example.obs.model.entity.Student;
import com.example.obs.model.entity.Transcript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TranscriptRepository extends JpaRepository<Transcript, Long> {
    
    /**
     * Öğrenciye ait transkripti bulur
     * @param student Öğrenci
     * @return Öğrencinin transkripti
     */
    Optional<Transcript> findByStudent(Student student);
} 