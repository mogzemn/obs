package com.example.obs.business.service.implementation;

import com.example.obs.business.service.FacultyService;
import com.example.obs.business.requests.FacultyCreateRequest;
import com.example.obs.business.requests.FacultyUpdateRequest;
import com.example.obs.business.responses.FacultyResponse;
import com.example.obs.core.exceptions.NotFoundException;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.core.utilities.numbergeneration.FacultyCodeGenerator;
import com.example.obs.dataAccess.FacultyRepository;
import com.example.obs.model.entity.Faculty;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;
    private final ModelMapperService modelMapperService;
    private final FacultyCodeGenerator facultyCodeGenerator;

    @Override
    @Transactional(readOnly = true)
    public List<FacultyResponse> getAll() {
        return facultyRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FacultyResponse getById(Long id) {
        return this.facultyRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Fakülte bilgisi bulunamadı! ID: " + id));
    }

    @Override
    public void add(FacultyCreateRequest facultyCreateRequest) {
        Faculty faculty = this.modelMapperService.forRequest()
                .map(facultyCreateRequest, Faculty.class);
        faculty.setFacultyCode(facultyCodeGenerator.generateNextCode());
        facultyRepository.save(faculty);
    }

    @Override
    public void update(FacultyUpdateRequest facultyUpdateRequest) {
        if (!facultyRepository.existsById(facultyUpdateRequest.getId())) {
            throw new NotFoundException("Güncelleme yapılamadı. ID: "
                    + facultyUpdateRequest.getId() + " ile eşleşen bir fakülte bulunamadı.");
        }
        Faculty existingFaculty = facultyRepository.findById(facultyUpdateRequest.getId())
                .orElseThrow(() -> new NotFoundException("Fakülte bilgisi bulunamadı! ID: " + facultyUpdateRequest.getId()));

        Faculty faculty = this.modelMapperService.forRequest()
                .map(facultyUpdateRequest, Faculty.class);

        faculty.setFacultyCode(existingFaculty.getFacultyCode());
        
        facultyRepository.save(faculty);
    }

    @Override
    public void delete(Long id) {
        if (!facultyRepository.existsById(id)) {
            throw new NotFoundException("Silme işlemi başarısız. ID: " + id + " ile eşleşen bir fakülte bulunamadı.");
        }
        this.facultyRepository.deleteById(id);
    }

    private FacultyResponse mapToResponse(Faculty faculty) {
        return modelMapperService.forResponse().map(faculty, FacultyResponse.class);
    }
}
