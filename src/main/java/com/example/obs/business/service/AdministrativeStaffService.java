package com.example.obs.business.service;

import com.example.obs.business.requests.AdministrativeStaffCreateRequest;
import com.example.obs.business.requests.AdministrativeStaffUpdateRequest;
import com.example.obs.business.responses.AdministrativeStaffResponse;

import java.util.List;

public interface AdministrativeStaffService {
    List<AdministrativeStaffResponse> getAll();
    AdministrativeStaffResponse getById(Long id);
    void add(AdministrativeStaffCreateRequest administrativeStaffCreateRequest);
    void update(AdministrativeStaffUpdateRequest administrativeStaffUpdateRequest);
    void delete(Long id);
}
