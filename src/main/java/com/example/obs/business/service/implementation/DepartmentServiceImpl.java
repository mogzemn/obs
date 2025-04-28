package com.example.obs.business.service.implementation;

import com.example.obs.business.service.DepartmentService;
import com.example.obs.business.requests.DepartmentCreateRequest;
import com.example.obs.business.requests.DepartmentUpdateRequest;
import com.example.obs.business.responses.DepartmentResponse;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dateAccess.DepartmentRepository;
import com.example.obs.model.entity.Department;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private DepartmentRepository departmentRepository;
    private ModelMapperService modelMapperService;

    @Override
    public List<DepartmentResponse> getAll() {
        List<Department> departments = departmentRepository.findAll();

        List<DepartmentResponse> responses = departments.stream()
                .map(department -> this.modelMapperService.forResponse()
                        .map(department, DepartmentResponse.class)).collect(Collectors.toList());

        return responses;
    }

    @Override
    public DepartmentResponse getById(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow();

        DepartmentResponse response = this.modelMapperService.forResponse()
                .map(department, DepartmentResponse.class);

        return response;
    }

    @Override
    public void add(DepartmentCreateRequest departmentCreateRequest) {
        Department department = this.modelMapperService.forRequest()
                .map(departmentCreateRequest, Department.class);

        departmentRepository.save(department);
    }

    @Override
    public void update(DepartmentUpdateRequest departmentUpdateRequest) {
        Department department = this.modelMapperService.forRequest()
                .map(departmentUpdateRequest, Department.class);

        departmentRepository.save(department);
    }

    @Override
    public void delete(Long id) {
        this.departmentRepository.deleteById(id);
    }
}