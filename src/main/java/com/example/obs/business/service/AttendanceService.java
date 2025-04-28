package com.example.obs.business.service;

import com.example.obs.business.requests.AttendanceCreateRequest;
import com.example.obs.business.requests.AttendanceUpdateRequest;
import com.example.obs.business.responses.AttendanceResponse;

import java.util.List;

public interface AttendanceService {
    List<AttendanceResponse> getAll();
    AttendanceResponse getById(Long id);
    void add(AttendanceCreateRequest attendanceCreateRequest);
    void update(AttendanceUpdateRequest attendanceUpdateRequest);
    void delete(Long id);
}
