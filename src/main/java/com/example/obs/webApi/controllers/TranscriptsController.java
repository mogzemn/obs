package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.TranscriptCreateRequest;
import com.example.obs.business.requests.TranscriptUpdateRequest;
import com.example.obs.business.responses.TranscriptResponse;
import com.example.obs.business.service.TranscriptService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transcripts")
@AllArgsConstructor
public class TranscriptsController {

    private final TranscriptService transcriptService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('student_affairs:manage_transcripts', 'admin:manage_students', 'system:manage_all')")
    public ResponseEntity<TranscriptResponse> add(@Valid @RequestBody TranscriptCreateRequest createRequest) {
        TranscriptResponse response = transcriptService.add(createRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('student_affairs:manage_transcripts', 'admin:manage_students', 'system:manage_all')")
    public ResponseEntity<TranscriptResponse> update(@Valid @RequestBody TranscriptUpdateRequest updateRequest) {
        TranscriptResponse response = transcriptService.update(updateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('student_affairs:manage_transcripts', 'admin:manage_students', 'system:manage_all')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transcriptService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("(@securityUtils.isOwnTranscript(#id) and hasAuthority('student:read_own_grades')) or hasAuthority('academic:read_student_info') or hasAnyRole('ADMIN', 'STUDENT_AFFAIRS_STAFF') or hasAuthority('system:manage_all')")
    public ResponseEntity<TranscriptResponse> getById(@PathVariable Long id) {
        TranscriptResponse response = transcriptService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT_AFFAIRS_STAFF') or hasAuthority('system:manage_all')")
    public ResponseEntity<List<TranscriptResponse>> getAll() {
        List<TranscriptResponse> responses = transcriptService.getAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("(@securityUtils.isOwnStudentId(#studentId) and hasAuthority('student:read_own_grades')) or hasAuthority('academic:read_student_info') or hasAnyRole('ADMIN', 'STUDENT_AFFAIRS_STAFF') or hasAuthority('system:manage_all')")
    public ResponseEntity<List<TranscriptResponse>> getByStudentId(@PathVariable Long studentId) {
        List<TranscriptResponse> responses = transcriptService.getByStudentId(studentId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/student/{studentId}/latest")
    @PreAuthorize("(@securityUtils.isOwnStudentId(#studentId) and hasAuthority('student:read_own_grades')) or hasAuthority('academic:read_student_info') or hasAnyRole('ADMIN', 'STUDENT_AFFAIRS_STAFF') or hasAuthority('system:manage_all')")
    public ResponseEntity<TranscriptResponse> getLatestByStudentId(@PathVariable Long studentId) {
        TranscriptResponse response = transcriptService.getLatestByStudentId(studentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}/date")
    @PreAuthorize("(@securityUtils.isOwnStudentId(#studentId) and hasAuthority('student:read_own_grades')) or hasAuthority('academic:read_student_info') or hasAnyRole('ADMIN', 'STUDENT_AFFAIRS_STAFF') or hasAuthority('system:manage_all')")
    public ResponseEntity<TranscriptResponse> getByStudentIdAndDate(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        TranscriptResponse response = transcriptService.getByStudentIdAndDate(studentId, date);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/student/{studentId}/calculate-gpa")
    @PreAuthorize("(@securityUtils.isOwnStudentId(#studentId) and hasAuthority('student:read_own_grades')) or hasAuthority('academic:read_student_info') or hasAnyRole('ADMIN', 'STUDENT_AFFAIRS_STAFF') or hasAuthority('system:manage_all')")
    public ResponseEntity<Double> calculateGpaForStudent(@PathVariable Long studentId) {
        Double gpa = transcriptService.calculateGpaForStudent(studentId);
        return ResponseEntity.ok(gpa);
    }
    
    @GetMapping("/student/{studentId}/calculate-gpa-until")
    @PreAuthorize("(@securityUtils.isOwnStudentId(#studentId) and hasAuthority('student:read_own_grades')) or hasAuthority('academic:read_student_info') or hasAnyRole('ADMIN', 'STUDENT_AFFAIRS_STAFF') or hasAuthority('system:manage_all')")
    public ResponseEntity<Double> calculateGpaForStudentUntilDate(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Double gpa = transcriptService.calculateGpaForStudentUntilDate(studentId, date.atStartOfDay());
        return ResponseEntity.ok(gpa);
    }
    
    @PostMapping("/student/{studentId}/create-transcript")
    @PreAuthorize("hasAnyAuthority('student_affairs:manage_transcripts', 'admin:manage_students', 'system:manage_all')")
    public ResponseEntity<TranscriptResponse> calculateAndCreateTranscript(@PathVariable Long studentId) {
        TranscriptResponse response = transcriptService.calculateAndCreateTranscript(studentId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PostMapping("/calculate-all")
    @PreAuthorize("hasAnyAuthority('student_affairs:manage_transcripts', 'admin:manage_students', 'system:manage_all')")
    public ResponseEntity<Void> calculateAndCreateTranscriptsForAllStudents() {
        transcriptService.calculateAndCreateTranscriptsForAllStudents();
        return ResponseEntity.noContent().build();
    }
}
