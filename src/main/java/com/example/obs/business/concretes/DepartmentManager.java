package com.example.obs.business.concretes;

import com.example.obs.business.abstracts.DepartmentService;
import com.example.obs.business.requests.DepartmentCreateRequest;
import com.example.obs.business.requests.DepartmentUpdateRequest;
import com.example.obs.business.responses.DepartmentResponse;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dateAccess.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DepartmentManager implements DepartmentService {
    private DepartmentRepository departmentRepository;
    private ModelMapperService modelMapperService;

    @Override
    public List<DepartmentResponse> getAll() {
        return List.of();
    }

    @Override
    public DepartmentResponse getById(Long id) {
        return null;
    }

    @Override
    public void add(DepartmentCreateRequest departmentCreateRequest) {

    }

    @Override
    public void update(DepartmentUpdateRequest departmentUpdateRequest) {

    }

    @Override
    public void delete(Long id) {

    }
}
