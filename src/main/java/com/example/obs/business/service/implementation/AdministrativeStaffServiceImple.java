package com.example.obs.business.service.implementation;

import com.example.obs.business.service.AdministrativeStaffService;
import com.example.obs.business.requests.AdministrativeStaffCreateRequest;
import com.example.obs.business.requests.AdministrativeStaffUpdateRequest;
import com.example.obs.business.responses.AdministrativeStaffResponse;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dateAccess.AdministrativeStaffRepository;
import com.example.obs.model.entity.AdministrativeStaff;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdministrativeStaffServiceImple implements AdministrativeStaffService {
    private AdministrativeStaffRepository administrativeStaffRepository;
    private ModelMapperService modelMapperService;

    @Override
    public List<AdministrativeStaffResponse> getAll() {
        List<AdministrativeStaff> admin = administrativeStaffRepository.findAll();

        List<AdministrativeStaffResponse> responses = admin.stream()
                .map(administrativeStaff -> this.modelMapperService.forResponse()
                        .map(administrativeStaff ,AdministrativeStaffResponse.class)).collect(Collectors.toList());
        return responses;
    }

    @Override
    public AdministrativeStaffResponse getById(Long id) {
       AdministrativeStaff admin = administrativeStaffRepository.findById(id).orElseThrow();

       AdministrativeStaffResponse response = this.modelMapperService.forResponse()
               .map(admin,AdministrativeStaffResponse.class);
        return response;
    }

    @Override
    public void add(AdministrativeStaffCreateRequest administrativeStaffCreateRequest) {
        AdministrativeStaff admin = this.modelMapperService.forRequest()
                .map(administrativeStaffCreateRequest,AdministrativeStaff.class);

        this.administrativeStaffRepository.save(admin);

    }

    @Override
    public void update(AdministrativeStaffUpdateRequest administrativeStaffUpdateRequest) {
        AdministrativeStaff admin = this.modelMapperService.forRequest()
                .map(administrativeStaffUpdateRequest,AdministrativeStaff.class);

        this.administrativeStaffRepository.save(admin);
    }

    @Override
    public void delete(Long id) {
        this.administrativeStaffRepository.deleteById(id);

    }
}
