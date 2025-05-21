package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.AnnouncementCreateRequest;
import com.example.obs.business.requests.AnnouncementUpdateRequest;
import com.example.obs.business.responses.AnnouncementResponse;
import com.example.obs.business.service.AnnouncementService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@AllArgsConstructor
public class AnnouncementsController {

    private final AnnouncementService announcementService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AnnouncementResponse> add(@Valid @RequestBody AnnouncementCreateRequest request) {
        AnnouncementResponse response = announcementService.add(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AnnouncementResponse> update(@PathVariable Long id, @Valid @RequestBody AnnouncementUpdateRequest request) {
        request.setId(id);
        AnnouncementResponse response = announcementService.update(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        announcementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AnnouncementResponse> getById(@PathVariable Long id) {
        AnnouncementResponse response = announcementService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AnnouncementResponse>> getAll() {
        List<AnnouncementResponse> responses = announcementService.getAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/active")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AnnouncementResponse>> getAllActive() {
        List<AnnouncementResponse> responses = announcementService.getAllActive();
        return ResponseEntity.ok(responses);
    }
}
