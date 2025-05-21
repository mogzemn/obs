package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.AcademicCreateRequest;
import com.example.obs.business.requests.AcademicUpdateRequest;
import com.example.obs.business.responses.AcademicResponse;
import com.example.obs.business.service.AcademicService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academics")
@AllArgsConstructor
public class AcademicsController {

    private AcademicService academicService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATIVE_MANAGER', 'ACADEMIC_AFFAIRS_STAFF') or hasAuthority('admin:read_all_academics') or hasAuthority('system:manage_all')")
    public List<AcademicResponse> getAll() {
        return academicService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATIVE_MANAGER', 'ACADEMIC_AFFAIRS_STAFF') or hasAuthority('admin:read_all_academics') or hasAuthority('system:manage_all') or (@securityUtils.isOwnAcademicId(#id) and hasAuthority('academic:read_own'))")
    public AcademicResponse getById(@PathVariable Long id) {
        return academicService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATIVE_MANAGER', 'ACADEMIC_AFFAIRS_STAFF') or hasAuthority('admin:manage_academics') or hasAuthority('acad_affairs:create_academic') or hasAuthority('system:manage_all')")
    public ResponseEntity<?> add(@RequestBody AcademicCreateRequest academicCreateRequest) {
        try {

            if (academicCreateRequest.getUser() != null && academicCreateRequest.getUser().getEmail() != null) {

                String email = academicCreateRequest.getUser().getEmail();
                if (email.matches(".*[çğıöşüÇĞİÖŞÜ].*")) {
                    return ResponseEntity.badRequest().body(
                        "E-posta adresi Türkçe karakterler içermemelidir. Lütfen çğıöşüÇĞİÖŞÜ karakterlerini kullanmadan tekrar deneyiniz.");
                }
            }
            
            this.academicService.add(academicCreateRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Akademisyen oluşturulurken bir hata oluştu: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATIVE_MANAGER', 'ACADEMIC_AFFAIRS_STAFF') or hasAuthority('admin:manage_academics') or hasAuthority('acad_affairs:update_academic') or hasAuthority('system:manage_all') or (@securityUtils.isOwnAcademicId(#id) and hasAuthority('academic:update_own'))")
    public void update(@PathVariable Long id, @RequestBody AcademicUpdateRequest academicUpdateRequest) {
        academicUpdateRequest.setId(id);
        this.academicService.update(academicUpdateRequest);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATIVE_MANAGER', 'ACADEMIC_AFFAIRS_STAFF') or hasAuthority('admin:manage_academics') or hasAuthority('system:manage_all')")
    public void delete(@PathVariable Long id) {
        this.academicService.delete(id);
    }
}
