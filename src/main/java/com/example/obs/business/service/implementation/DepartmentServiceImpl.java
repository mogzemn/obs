package com.example.obs.business.service.implementation;

import com.example.obs.business.service.DepartmentService;
import com.example.obs.business.requests.DepartmentCreateRequest;
import com.example.obs.business.requests.DepartmentUpdateRequest;
import com.example.obs.business.responses.DepartmentResponse;
import com.example.obs.core.exceptions.BusinessException;
import com.example.obs.core.exceptions.NotFoundException;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.core.utilities.numbergeneration.DepartmentCodeGenerator;
import com.example.obs.dataAccess.DepartmentRepository;
import com.example.obs.dataAccess.FacultyRepository;
import com.example.obs.dataAccess.AcademicRepository;
import com.example.obs.model.entity.Department;
import com.example.obs.model.entity.Faculty;
import com.example.obs.model.entity.Academic;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final ModelMapperService modelMapperService;
    private final DepartmentCodeGenerator departmentCodeGenerator;
    private final FacultyRepository facultyRepository;
    private final AcademicRepository academicRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAll() {
        return departmentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getById(Long id) {
        return this.departmentRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Bölüm bilgisi bulunamadı! ID: " + id));
    }

    @Override
    public void add(DepartmentCreateRequest departmentCreateRequest) {
        if (departmentRepository.existsByDepartmentNameIgnoreCase(departmentCreateRequest.getDepartmentName())) {
            throw new BusinessException("Bu isimde bir bölüm zaten mevcut: " + departmentCreateRequest.getDepartmentName());
        }

        Department department = this.modelMapperService.forRequest()
                .map(departmentCreateRequest, Department.class);
        department.setId(null);

        if (departmentCreateRequest.getFacultyId() != null) {
            Faculty faculty = facultyRepository.findById(departmentCreateRequest.getFacultyId())
                    .orElseThrow(() -> new NotFoundException("Fakülte bulunamadı! ID: " + departmentCreateRequest.getFacultyId()));
            department.setFaculty(faculty);
        } else {
            throw new BusinessException("Fakülte ID'si boş olamaz!");
        }

        department.setDepartmentCode(departmentCodeGenerator.generateNextCode());
        departmentRepository.save(department);
    }

    @Override
    @Transactional
    public void update(DepartmentUpdateRequest departmentUpdateRequest) {
        try {
            Department existingDepartment = departmentRepository.findById(departmentUpdateRequest.getId())
                    .orElseThrow(() -> new NotFoundException("Güncelleme yapılamadı. ID: " 
                            + departmentUpdateRequest.getId() + " ile eşleşen bir bölüm bulunamadı."));
            
            Faculty faculty = null;
            if (departmentUpdateRequest.getFacultyId() != null) {
                faculty = facultyRepository.findById(departmentUpdateRequest.getFacultyId())
                        .orElseThrow(() -> new NotFoundException("Belirtilen fakülte bulunamadı: ID " 
                                + departmentUpdateRequest.getFacultyId()));
            }
            
            if (departmentUpdateRequest.getDepartmentName() != null && 
                !existingDepartment.getDepartmentName().equals(departmentUpdateRequest.getDepartmentName()) && 
                departmentRepository.existsByDepartmentNameIgnoreCase(departmentUpdateRequest.getDepartmentName())) {
                throw new BusinessException("Bu isimde bir bölüm zaten mevcut: " 
                        + departmentUpdateRequest.getDepartmentName());
            }
            
            if (departmentUpdateRequest.getDepartmentName() != null) {
                existingDepartment.setDepartmentName(departmentUpdateRequest.getDepartmentName());
            }
            
            if (departmentUpdateRequest.getDepartmentCode() != null) {
                existingDepartment.setDepartmentCode(departmentUpdateRequest.getDepartmentCode());
            }
            
            if (faculty != null) {
                existingDepartment.setFaculty(faculty);
            }
            
            if (departmentUpdateRequest.getHeadOfDepartmentId() != null) {
                Academic head = academicRepository.findById(departmentUpdateRequest.getHeadOfDepartmentId())
                        .orElseThrow(() -> new NotFoundException("Belirtilen akademisyen bulunamadı: ID " 
                                + departmentUpdateRequest.getHeadOfDepartmentId()));
                existingDepartment.setHead(head);
            }
            
            if (departmentUpdateRequest.getIsActive() != null) {
                existingDepartment.setIsActive(departmentUpdateRequest.getIsActive());
            }
            
            departmentRepository.save(existingDepartment);
            
        } catch (Exception ex) {
            throw new BusinessException("Bölüm güncellenirken bir hata oluştu: " + ex.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new NotFoundException("Silme işlemi başarısız. ID: " + id + " ile eşleşen bir bölüm bulunamadı.");
        }
        this.departmentRepository.deleteById(id);
    }
    
    private DepartmentResponse mapToResponse(Department department) {
        return modelMapperService.forResponse().map(department, DepartmentResponse.class);
    }
}
