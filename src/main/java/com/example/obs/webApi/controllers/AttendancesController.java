package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.AttendanceCreateRequest;
import com.example.obs.business.requests.AttendanceUpdateRequest;
import com.example.obs.business.responses.AttendanceResponse;
import com.example.obs.business.service.AttendanceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendances")
@AllArgsConstructor
public class AttendancesController {

    private AttendanceService attendanceService;

    @GetMapping("/all")
    public List<AttendanceResponse> getAll() {
        return attendanceService.getAll();
    }

    @GetMapping("/{id}")
    public AttendanceResponse getById(@PathVariable int id) {
        return attendanceService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void add(@RequestBody AttendanceCreateRequest attendanceCreateRequest) {
        this.attendanceService.add(attendanceCreateRequest);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody AttendanceUpdateRequest attendanceUpdateRequest) {
        attendanceUpdateRequest.setId(id);
        this.attendanceService.update(attendanceUpdateRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        this.attendanceService.delete(id);
    }
}