package com.example.obs.business.service.implementation;

import com.example.obs.business.service.AttendanceService;
import com.example.obs.business.requests.AttendanceCreateRequest;
import com.example.obs.business.requests.AttendanceUpdateRequest;
import com.example.obs.business.responses.AttendanceResponse;
import com.example.obs.core.exceptions.BusinessException;
import com.example.obs.core.exceptions.NotFoundException;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dataAccess.AcademicYearRepository;
import com.example.obs.dataAccess.AttendanceRepository;
import com.example.obs.dataAccess.UserRepository;
import com.example.obs.model.entity.AcademicYear;
import com.example.obs.model.entity.Attendance;
import com.example.obs.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final ModelMapperService modelMapperService;
    private final AcademicYearRepository academicYearRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAll() {
        return attendanceRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceResponse getById(Long id) {
        return this.attendanceRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Yoklama bilgisi bulunamadı! ID: " + id));
    }

    @Override
    public void add(AttendanceCreateRequest attendanceCreateRequest) {
        AcademicYear academicYear = academicYearRepository.findById(attendanceCreateRequest.getAcademicYearId())
                .orElseThrow(() -> new NotFoundException("Akademik yıl bulunamadı: ID " + 
                        attendanceCreateRequest.getAcademicYearId()));

        Attendance attendance = this.modelMapperService.forRequest()
                .map(attendanceCreateRequest, Attendance.class);

        attendance.setAcademicYear(academicYear.getName());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("User not authenticated");
        }
        String currentPrincipalName = authentication.getName();
        User currentUser = userRepository.findByEmail(currentPrincipalName)
                .orElseThrow(() -> new NotFoundException("User not found: " + currentPrincipalName));
        attendance.setCreatedBy(currentUser);

        attendanceRepository.save(attendance);
    }

    @Override
    public void update(AttendanceUpdateRequest attendanceUpdateRequest) {
        Attendance existingAttendance = attendanceRepository.findById(attendanceUpdateRequest.getId())
                .orElseThrow(() -> new NotFoundException("Güncelleme yapılamadı. ID: "
                        + attendanceUpdateRequest.getId() + " ile eşleşen bir yoklama kaydı bulunamadı."));

        Attendance attendance = this.modelMapperService.forRequest()
                .map(attendanceUpdateRequest, Attendance.class);
                
        attendance.setStudent(existingAttendance.getStudent());
        attendance.setCourse(existingAttendance.getCourse());
        attendance.setAcademicYear(existingAttendance.getAcademicYear());

        attendanceRepository.save(attendance);
    }

    @Override
    public void delete(Long id) {
        if (!attendanceRepository.existsById(id)) {
            throw new NotFoundException("Silme işlemi başarısız. ID: " + id + " ile eşleşen bir yoklama kaydı bulunamadı.");
        }
        this.attendanceRepository.deleteById(id);
    }
    
    private AttendanceResponse mapToResponse(Attendance attendance) {
        return modelMapperService.forResponse().map(attendance, AttendanceResponse.class);
    }
}
