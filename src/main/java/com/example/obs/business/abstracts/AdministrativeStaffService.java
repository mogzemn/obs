package com.example.obs.business.abstracts;

import com.example.obs.business.requests.AdministrativeStaffCreateRequest;
import com.example.obs.business.requests.AdministrativeStaffUpdateRequest;
import com.example.obs.business.requests.FacultyCreateRequest;
import com.example.obs.business.requests.FacultyUpdateRequest;
import com.example.obs.business.responses.AdministrativeStaffResponse;
import com.example.obs.business.responses.FacultyResponse;

import java.util.List;

public interface AdministrativeStaffService {
    List<AdministrativeStaffResponse> getAll();
    AdministrativeStaffResponse getById(Long id);
    void add(AdministrativeStaffCreateRequest administrativeStaffCreateRequest);
    void update(AdministrativeStaffUpdateRequest administrativeStaffUpdateRequest);
    void delete(Long id);
}
