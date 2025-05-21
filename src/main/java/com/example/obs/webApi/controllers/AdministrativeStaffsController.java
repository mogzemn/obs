package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.AdministrativeStaffCreateRequest;
import com.example.obs.business.requests.AdministrativeStaffUpdateRequest;
import com.example.obs.business.responses.AdministrativeStaffResponse;
import com.example.obs.business.service.AdministrativeStaffService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administrative-staffs")
@AllArgsConstructor
public class AdministrativeStaffsController {

    private AdministrativeStaffService administrativeStaffService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATIVE_MANAGER') or hasAuthority('system:manage_all')")
    public List<AdministrativeStaffResponse> getAll() {
        return administrativeStaffService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATIVE_MANAGER') or hasAuthority('system:manage_all') or (@securityUtils.isOwnAdministrativeStaffId(#id) and hasAuthority('admin:read_own'))")
    public AdministrativeStaffResponse getById(@PathVariable Long id) {
        return administrativeStaffService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATIVE_MANAGER') or hasAuthority('system:manage_all')")
    public void add(@RequestBody AdministrativeStaffCreateRequest administrativeStaffCreateRequest) {
        this.administrativeStaffService.add(administrativeStaffCreateRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATIVE_MANAGER') or hasAuthority('system:manage_all') or (@securityUtils.isOwnAdministrativeStaffId(#id) and hasAuthority('admin:update_own'))")
    public void update(@PathVariable Long id, @RequestBody AdministrativeStaffUpdateRequest administrativeStaffUpdateRequest) {
        administrativeStaffUpdateRequest.setId(id);
        this.administrativeStaffService.update(administrativeStaffUpdateRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATIVE_MANAGER') or hasAuthority('system:manage_all')")
    public void delete(@PathVariable Long id) {
        this.administrativeStaffService.delete(id);
    }
}
