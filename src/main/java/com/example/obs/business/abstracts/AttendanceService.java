package com.example.obs.business.abstracts;

import com.example.obs.business.requests.AttendanceCreateRequest;
import com.example.obs.business.requests.AttendanceUpdateRequest;
import com.example.obs.business.requests.FacultyCreateRequest;
import com.example.obs.business.requests.FacultyUpdateRequest;
import com.example.obs.business.responses.AttendanceResponse;
import com.example.obs.business.responses.FacultyResponse;

import java.util.List;

public interface AttendanceService {
    List<AttendanceResponse> getAll();
    AttendanceResponse getById(Long id);
    void add(AttendanceCreateRequest attendanceCreateRequest);
    void update(AttendanceUpdateRequest attendanceUpdateRequest);
    void delete(Long id);
}
