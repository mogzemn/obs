package com.example.obs.business.service;

import com.example.obs.business.requests.AcademicYearCreateRequest;
import com.example.obs.business.requests.AcademicYearUpdateRequest;
import com.example.obs.business.responses.AcademicYearResponse;

import java.time.LocalDate;
import java.util.List;

public interface AcademicYearService {
    AcademicYearResponse add(AcademicYearCreateRequest request);
    AcademicYearResponse update(AcademicYearUpdateRequest request);
    void delete(Long id);
    AcademicYearResponse getById(Long id);
    List<AcademicYearResponse> getAll();
    AcademicYearResponse getByName(String name);
    AcademicYearResponse getByStartYearAndEndYear(Integer startYear, Integer endYear);
    
    AcademicYearResponse findByDate(LocalDate date);
}
