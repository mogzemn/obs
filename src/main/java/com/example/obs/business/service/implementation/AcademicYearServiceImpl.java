package com.example.obs.business.service.implementation;

import com.example.obs.business.requests.AcademicYearCreateRequest;
import com.example.obs.business.requests.AcademicYearUpdateRequest;
import com.example.obs.business.responses.AcademicYearResponse;
import com.example.obs.business.service.AcademicYearService;
import com.example.obs.core.exceptions.BusinessException;
import com.example.obs.core.exceptions.NotFoundException;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dataAccess.AcademicYearRepository;
import com.example.obs.model.entity.AcademicYear;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class AcademicYearServiceImpl implements AcademicYearService {

    private final AcademicYearRepository academicYearRepository;
    private final ModelMapperService modelMapperService;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public AcademicYearResponse add(AcademicYearCreateRequest request) {
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        
        validateAcademicYearDates(startDate, endDate);
        checkIfAcademicYearExists(startDate, endDate);

        AcademicYear academicYear = this.modelMapperService.forRequest()
        .map(request, AcademicYear.class);
        academicYear = this.academicYearRepository.save(academicYear);
        return mapToResponse(academicYear);
    }

    @Override
    public AcademicYearResponse update(AcademicYearUpdateRequest request) {
        if (!academicYearRepository.existsById(request.getId())) {
            throw new NotFoundException("Akademik Yıl bulunamadı: ID " + request.getId());
        }

        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        
        validateAcademicYearDates(startDate, endDate);

        AcademicYear academicYear = this.modelMapperService.forRequest().map(request, AcademicYear.class);
        academicYear = this.academicYearRepository.save(academicYear);
        return mapToResponse(academicYear);
    }

    @Override
    public void delete(Long id) {
        if (!academicYearRepository.existsById(id)) {
            throw new NotFoundException("Akademik Yıl bulunamadı: ID " + id);
        }
        this.academicYearRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public AcademicYearResponse getById(Long id) {
        AcademicYear academicYear = academicYearRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Akademik Yıl bulunamadı: ID " + id));
        return mapToResponse(academicYear);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicYearResponse> getAll() {
        List<AcademicYear> academicYears = academicYearRepository.findAll();
        return academicYears.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AcademicYearResponse getByName(String name) {
        AcademicYear academicYear = academicYearRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Akademik Yıl bulunamadı: İsim " + name));
        return mapToResponse(academicYear);
    }

    @Override
    @Transactional(readOnly = true)
    public AcademicYearResponse getByStartYearAndEndYear(Integer startYear, Integer endYear) {
        AcademicYear academicYear = academicYearRepository.findByStartYearAndEndYear(startYear, endYear)
                .orElseThrow(() -> new NotFoundException("Akademik Yıl bulunamadı: " + startYear + "-" + endYear));
        return mapToResponse(academicYear);
    }

    @Override
    @Transactional(readOnly = true)
    public AcademicYearResponse findByDate(LocalDate date) {
        AcademicYear academicYear = academicYearRepository.findByDate(date)
                .orElseThrow(() -> new NotFoundException("Bu tarihte (" + date + ") bir akademik yıl bulunamadı."));
        return mapToResponse(academicYear);
    }

    private void validateAcademicYearDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new BusinessException("Başlangıç ve bitiş tarihleri boş olamaz.");
        }
        
        if (!startDate.isBefore(endDate)) {
            throw new BusinessException("Başlangıç tarihi (" + startDate.format(DATE_FORMATTER) + 
                    "), bitiş tarihinden (" + endDate.format(DATE_FORMATTER) + ") önce olmalıdır.");
        }
        
        LocalDate minEndDate = startDate.plusMonths(3);
        if (endDate.isBefore(minEndDate)) {
            throw new BusinessException("Akademik yıl en az 3 ay sürmelidir. Bitiş tarihi en erken " + 
                    minEndDate.format(DATE_FORMATTER) + " olabilir.");
        }
    }

    private void checkIfAcademicYearExists(LocalDate startDate, LocalDate endDate) {
        academicYearRepository.findByStartYearAndEndYear(startDate.getYear(), endDate.getYear())
                .ifPresent(ay -> {
                    throw new BusinessException("Bu yıllara sahip bir akademik yıl zaten mevcut: " + 
                            startDate.getYear() + "-" + endDate.getYear());
                });
        
        List<AcademicYear> overlappingYears = academicYearRepository.findAll().stream()
                .filter(ay -> 
                    (startDate.isAfter(ay.getStartDate()) && startDate.isBefore(ay.getEndDate())) ||
                    (endDate.isAfter(ay.getStartDate()) && endDate.isBefore(ay.getEndDate())) ||
                    (startDate.isBefore(ay.getStartDate()) && endDate.isAfter(ay.getEndDate())) ||
                    (startDate.isAfter(ay.getStartDate()) && endDate.isBefore(ay.getEndDate())))
                .collect(Collectors.toList());
        
        if (!overlappingYears.isEmpty()) {
            throw new BusinessException("Girilen tarih aralığı (" + startDate.format(DATE_FORMATTER) + 
                    " - " + endDate.format(DATE_FORMATTER) + "), mevcut akademik yıllarla çakışıyor.");
        }
    }

    private AcademicYearResponse mapToResponse(AcademicYear academicYear) {
    
        AcademicYearResponse response = modelMapperService.forResponse().map(academicYear, AcademicYearResponse.class);
        return response;
    }
}
