package com.example.obs.business.concretes;

import com.example.obs.business.abstracts.AdministrativeStaffService;
import com.example.obs.business.requests.AdministrativeStaffCreateRequest;
import com.example.obs.business.requests.AdministrativeStaffUpdateRequest;
import com.example.obs.business.responses.AdministrativeStaffResponse;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dateAccess.AdministrativeStaffRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdministrativeStaffManager implements AdministrativeStaffService {
    private AdministrativeStaffRepository administrativeStaffRepository;
    private ModelMapperService modelMapperService;

    @Override
    public List<AdministrativeStaffResponse> getAll() {
        return List.of();
    }

    @Override
    public AdministrativeStaffResponse getById(Long id) {
        return null;
    }

    @Override
    public void add(AdministrativeStaffCreateRequest administrativeStaffCreateRequest) {

    }

    @Override
    public void update(AdministrativeStaffUpdateRequest administrativeStaffUpdateRequest) {

    }

    @Override
    public void delete(Long id) {

    }
}
