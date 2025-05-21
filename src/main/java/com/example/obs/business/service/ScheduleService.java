package com.example.obs.business.service;

import com.example.obs.business.requests.ScheduleCreateRequest;
import com.example.obs.business.requests.ScheduleUpdateRequest;
import com.example.obs.business.responses.ScheduleResponse;

import java.util.List;

public interface ScheduleService {
    ScheduleResponse add(ScheduleCreateRequest createRequest);
    ScheduleResponse update(ScheduleUpdateRequest updateRequest);
    void delete(Long id);
    List<ScheduleResponse> getAll();
    ScheduleResponse getById(Long id);
    List<ScheduleResponse> getByCourseId(Long courseId);
    List<ScheduleResponse> getByCourseInstructorId(Long courseInstructorId);
}
