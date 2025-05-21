package com.example.obs.business.service.implementation;

import com.example.obs.business.service.GradeScaleService;
import com.example.obs.business.requests.GradeScaleCreateRequest;
import com.example.obs.business.requests.GradeScaleUpdateRequest;
import com.example.obs.business.responses.GradeScaleResponse;
import com.example.obs.core.exceptions.BusinessException;
import com.example.obs.core.exceptions.NotFoundException;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dataAccess.GradeScaleRepository;
import com.example.obs.dataAccess.UserRepository;
import com.example.obs.model.entity.GradeScale;
import com.example.obs.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class GradeScaleServiceImpl implements GradeScaleService {
    private final GradeScaleRepository gradeScaleRepository;
    private final UserRepository userRepository;
    private final ModelMapperService modelMapperService;

    @Override
    @Transactional(readOnly = true)
    public List<GradeScaleResponse> getAll() {
        return gradeScaleRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public GradeScaleResponse getById(Long id) {
        return this.gradeScaleRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Not ölçeği bilgisi bulunamadı! ID: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GradeScaleResponse> getAllActive() {
        return gradeScaleRepository.findByIsActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GradeScaleResponse> getAllDefault() {
        return gradeScaleRepository.findByIsDefaultTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GradeScaleResponse> getByDepartmentId(Long departmentId) {
        return gradeScaleRepository.findByAcademicDepartmentId(departmentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GradeScaleResponse> getByCourseId(Long courseId) {
        return gradeScaleRepository.findByCourseId(courseId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GradeScaleResponse> getByAcademicId(Long academicId) {
        return gradeScaleRepository.findByAcademicId(academicId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public GradeScaleResponse getDefaultByDepartmentId(Long departmentId) {
        return this.gradeScaleRepository.findByAcademicDepartmentIdAndIsDefaultTrue(departmentId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Bölüm için varsayılan not ölçeği bulunamadı! Bölüm ID: " + departmentId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public GradeScaleResponse getDefaultByCourseId(Long courseId) {
        return this.gradeScaleRepository.findByCourseIdAndIsDefaultTrue(courseId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Ders için varsayılan not ölçeği bulunamadı! Ders ID: " + courseId));
    }

    @Override
    public void add(GradeScaleCreateRequest gradeScaleCreateRequest) {
        GradeScale gradeScale = this.modelMapperService.forRequest()
                .map(gradeScaleCreateRequest, GradeScale.class);

        User createdBy = userRepository.findById(gradeScaleCreateRequest.getCreatedById())
                .orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı! ID: " + gradeScaleCreateRequest.getCreatedById()));
        
        gradeScale.setCreatedBy(createdBy);
        
        if (gradeScale.getAaMin() == null) gradeScale.setAaMin(0.90);
        if (gradeScale.getBaMin() == null) gradeScale.setBaMin(0.85);
        if (gradeScale.getBbMin() == null) gradeScale.setBbMin(0.80);
        if (gradeScale.getCbMin() == null) gradeScale.setCbMin(0.70);
        if (gradeScale.getCcMin() == null) gradeScale.setCcMin(0.60);
        if (gradeScale.getDcMin() == null) gradeScale.setDcMin(0.55);
        if (gradeScale.getDdMin() == null) gradeScale.setDdMin(0.50);
        if (gradeScale.getFfMin() == null) gradeScale.setFfMin(0.49);
        
        gradeScaleRepository.save(gradeScale);
    }

    @Override
    public void update(GradeScaleUpdateRequest gradeScaleUpdateRequest) {
        if (!gradeScaleRepository.existsById(gradeScaleUpdateRequest.getId())) {
            throw new NotFoundException("Güncelleme yapılamadı. ID: "
                    + gradeScaleUpdateRequest.getId() + " ile eşleşen bir not ölçeği bulunamadı.");
        }

        GradeScale existingGradeScale = gradeScaleRepository.findById(gradeScaleUpdateRequest.getId())
                .orElseThrow(() -> new NotFoundException("Not ölçeği bulunamadı! ID: " + gradeScaleUpdateRequest.getId()));

        GradeScale updatedGradeScale = this.modelMapperService.forRequest()
                .map(gradeScaleUpdateRequest, GradeScale.class);
        
        User updatedBy = userRepository.findById(gradeScaleUpdateRequest.getUpdatedById())
                .orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı! ID: " + gradeScaleUpdateRequest.getUpdatedById()));
        
        updatedGradeScale.setUpdatedBy(updatedBy);
        updatedGradeScale.setCreatedBy(existingGradeScale.getCreatedBy());
        updatedGradeScale.setCreatedAt(existingGradeScale.getCreatedAt());

        gradeScaleRepository.save(updatedGradeScale);
    }

    @Override
    public void delete(Long id) {
        if (!gradeScaleRepository.existsById(id)) {
            throw new NotFoundException("Silme işlemi başarısız. ID: " + id + " ile eşleşen bir not ölçeği bulunamadı.");
        }
        this.gradeScaleRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public GradeScale findApplicableScale(Long courseId, Long departmentId) {
        if (courseId != null) {
            Optional<GradeScale> courseScale = gradeScaleRepository.findByCourseIdAndIsDefaultTrue(courseId);
            if (courseScale.isPresent()) {
                return courseScale.get();
            }
        }
        
        if (departmentId != null) {
            Optional<GradeScale> departmentScale = gradeScaleRepository.findByAcademicDepartmentIdAndIsDefaultTrue(departmentId);
            if (departmentScale.isPresent()) {
                return departmentScale.get();
            }
        }
        
        List<GradeScale> defaultScales = gradeScaleRepository.findByIsDefaultTrue();
        if (!defaultScales.isEmpty()) {
            return defaultScales.get(0);
        }
        
        return createDefaultScale();
    }
    
    private GradeScale createDefaultScale() {
        GradeScale scale = new GradeScale();
        scale.setName("Varsayılan Ölçek");
        scale.setIsActive(true);
        scale.setIsDefault(true);
        
        scale.setAaMin(0.90);
        scale.setBaMin(0.85);
        scale.setBbMin(0.80);
        scale.setCbMin(0.70);
        scale.setCcMin(0.60);
        scale.setDcMin(0.55);
        scale.setDdMin(0.50);
        scale.setFfMin(0.49);
        
        return scale;
    }
    
    private GradeScaleResponse mapToResponse(GradeScale gradeScale) {
        return modelMapperService.forResponse().map(gradeScale, GradeScaleResponse.class);
    }
}
