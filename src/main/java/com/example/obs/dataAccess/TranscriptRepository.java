package com.example.obs.dataAccess;

import com.example.obs.model.entity.Transcript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TranscriptRepository extends JpaRepository<Transcript, Long> {
    Optional<Transcript> findByStudentIdAndTranscriptDate(Long studentId, java.time.LocalDate transcriptDate);
    List<Transcript> findAllByStudentIdOrderByTranscriptDateDesc(Long studentId);
}
