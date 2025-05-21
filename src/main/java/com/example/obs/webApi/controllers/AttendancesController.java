package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.AttendanceCreateRequest;
import com.example.obs.business.requests.AttendanceUpdateRequest;
import com.example.obs.business.responses.AttendanceResponse;
import com.example.obs.business.service.AttendanceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendances")
@AllArgsConstructor
public class AttendancesController {

    private AttendanceService attendanceService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN') or hasAuthority('system:manage_all')")
    public List<AttendanceResponse> getAll() {
        return attendanceService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('academic:manage_attendance', 'student:read_own_attendance', 'system:manage_all')")
    public AttendanceResponse getById(@PathVariable Long id) {
        return attendanceService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('academic:manage_attendance', 'system:manage_all')")
    public void add(@RequestBody AttendanceCreateRequest attendanceCreateRequest) {
        this.attendanceService.add(attendanceCreateRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('academic:manage_attendance', 'system:manage_all')")
    public void update(@PathVariable Long id, @RequestBody AttendanceUpdateRequest attendanceUpdateRequest) {
        attendanceUpdateRequest.setId(id);
        this.attendanceService.update(attendanceUpdateRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('academic:manage_attendance', 'system:manage_all')")
    public void delete(@PathVariable Long id) {
        this.attendanceService.delete(id);
    }
}
