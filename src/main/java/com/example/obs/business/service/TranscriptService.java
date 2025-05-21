package com.example.obs.business.service;

import com.example.obs.business.requests.TranscriptCreateRequest;
import com.example.obs.business.requests.TranscriptUpdateRequest;
import com.example.obs.business.responses.TranscriptResponse;

import java.time.LocalDate;
import java.time.LocalDateTime; 
import java.util.List;

public interface TranscriptService {
    TranscriptResponse add(TranscriptCreateRequest createRequest);
    TranscriptResponse update(TranscriptUpdateRequest updateRequest);
    void delete(Long id);
    TranscriptResponse getById(Long id);
    List<TranscriptResponse> getAll();
    List<TranscriptResponse> getByStudentId(Long studentId);
    
    TranscriptResponse getLatestByStudentId(Long studentId);

    TranscriptResponse getByStudentIdAndDate(Long studentId, LocalDate date);

    Double calculateGpaForStudent(Long studentId);
    
    Double calculateGpaForStudentUntilDate(Long studentId, LocalDateTime date); 
    
    TranscriptResponse calculateAndCreateTranscript(Long studentId);
    
    void calculateAndCreateTranscriptsForAllStudents();
}
